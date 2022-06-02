/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */
package org.cloudbus.cloudsim.network.switches;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimEntity;
import org.cloudbus.cloudsim.core.CloudSimTag;
import org.cloudbus.cloudsim.core.events.PredicateType;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.network.HostPacket;
import org.cloudbus.cloudsim.vms.Vm;

import org.autocs.sdn.data.networkelement.switches.SwitchResourceStats;
import org.autocs.sdn.data.networkelement.NetworkElement;
import org.autocs.sdn.data.networkelement.resources.NetworkElementStateEntry;
import org.autocs.sdn.power.models.PowerModelSwitch;
import org.autocs.sdn.control.tables.RoutingTable;
import org.autocs.sdn.control.network.physical.PhysicalLink;
import org.autocs.sdn.control.tables.ForwardingTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static org.cloudbus.cloudsim.util.BytesConversion.bytesToMegaBits;
import static java.util.Objects.requireNonNull;

/**
 * An abstract class for implementing Network {@link Switch}es.
 *
 * @author Saurabh Kumar Garg
 * @author Manoel Campos da Silva Filho
 * @author Ibrahem Mouhamad
 * @since AutoCS Core Package 1.0.
 */
public abstract class AbstractSwitch extends CloudSimEntity implements Switch {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSwitch.class.getSimpleName());

    /**
     * Map of packets sent to Datacenter on the uplink, where each key is a switch
     * and the corresponding value is the list of packets to sent to that switch.
     */
    private final Map<Switch, List<HostPacket>> uplinkSwitchPacketMap;

    /**
     * Map of packets sent to Datacenter on the downlink, where each key is a
     * switch and the corresponding value is the list of packets to sent to that
     * switch.
     */
    private final Map<Switch, List<HostPacket>> downlinkSwitchPacketMap;

    /**
     * Map of packets sent to hosts connected in the switch, where each key is a
     * host and the corresponding value is the list of packets to sent to that host.
     */
    private final Map<NetworkHost, List<HostPacket>> packetToHostMap;

    /**
     * List of uplink Datacenter.
     */
    private final List<Switch> uplinkSwitches;

    /**
     * List of downlink Datacenter.
     */
    private final List<Switch> downlinkSwitches;

    /** @see #getUplinkBandwidth() */
    private double uplinkBandwidth;

    /** @see #getDownlinkBandwidth() */
    private double downlinkBandwidth;

    /** @see #getPorts() */
    private int ports;

    /** @see #getDatacenter() */
    private NetworkDatacenter datacenter;

    /** @see #getSwitchingDelay() */
    private double switchingDelay;

    /** @see #getPowerModel() */
    private PowerModelSwitch powerModel;

    private boolean stateHistoryEnabled;

    /** @see #getStateHistory() */
    private final List<NetworkElementStateEntry> stateHistory;

    protected SwitchResourceStats utilizationStats;
    private long uploadSoFar;
    private long downloadSoFar;

    /** @see #getLastBusyTime() */
    private double lastBusyTime;

    /**
     * switch's routing table
     */
    private RoutingTable routingTable;

    /**
     * switch's forwarding table
     */
    private ForwardingTable forwardingTable;

    public AbstractSwitch(final CloudSim simulation, final NetworkDatacenter dc) {
        super(simulation);
        this.packetToHostMap = new HashMap<>();
        this.uplinkSwitchPacketMap = new HashMap<>();
        this.downlinkSwitchPacketMap = new HashMap<>();
        this.downlinkSwitches = new ArrayList<>();
        this.uplinkSwitches = new ArrayList<>();
        this.datacenter = Objects.requireNonNull(dc);
        this.powerModel = PowerModelSwitch.NULL;
        this.stateHistory = new LinkedList<>();
        this.utilizationStats = SwitchResourceStats.NULL;
        this.uploadSoFar = 0;
        this.downloadSoFar = 0;
        this.routingTable = new RoutingTable();
        this.forwardingTable = new ForwardingTable();
    }

    @Override
    protected void startInternal() {
        LOGGER.info("{} is starting...", getName());
        schedule(CloudSimTag.DC_LIST_REQUEST);
        lastBusyTime = this.getStartTime();
    }

    @Override
    public void processEvent(final SimEvent evt) {
        switch (evt.getTag()) {
            case NETWORK_EVENT_UP -> processPacketUp(evt);
            case NETWORK_EVENT_DOWN -> processPacketDown(evt);
            case NETWORK_EVENT_SEND -> processPacketForward();
            case NETWORK_EVENT_HOST -> processHostPacket(evt);
            default ->
                LOGGER.trace("{}: {}: Unknown event {} received.", getSimulation().clockStr(), this, evt.getTag());
        }
        addStateHistory(evt.getTime());
        if (utilizationStats.add(evt.getTime())) {
            this.uploadSoFar = 0;
            this.downloadSoFar = 0;
        }
        lastBusyTime = evt.getTime();
    }

    /**
     * Process a packet sent to a host.
     *
     * @param evt the packet sent
     */
    protected void processHostPacket(final SimEvent evt) {
        if (evt.getData() instanceof HostPacket pkt) {
            final NetworkHost host = pkt.getDestination();
            host.addReceivedNetworkPacket(pkt);
        } else
            throw new IllegalStateException("NETWORK_EVENT_HOST SimEvent data must be a HostPacket");
    }

    /**
     * Sends a packet from uplink to Datacenter connected through a downlink port.
     *
     * @param evt event/packet to process
     */
    protected void processPacketDown(final SimEvent evt) {
        // Packet coming from up level router has to send downward.
        getSimulation().cancelAll(this, new PredicateType(CloudSimTag.NETWORK_EVENT_SEND));
        schedule(this, getSwitchingDelay(), CloudSimTag.NETWORK_EVENT_SEND);
    }

    /**
     * Gets the Host where a VM is placed.
     *
     * @param vm the VM to get its Host
     * @return the Host where the VM is placed
     */
    protected NetworkHost getVmHost(final Vm vm) {
        return (NetworkHost) vm.getHost();
    }

    /**
     * Sends a packet from down switch or host to Datacenter connected through an
     * uplink port.
     *
     * @param evt Event/packet to process
     */
    protected void processPacketUp(final SimEvent evt) {
        // Packet coming from down level router has to be sent up.
        getSimulation().cancelAll(this, new PredicateType(CloudSimTag.NETWORK_EVENT_SEND));
        schedule(this, switchingDelay, CloudSimTag.NETWORK_EVENT_SEND);
    }

    /**
     * Sends a packet to hosts connected to the switch.
     */
    private void processPacketForward() {
        forwardPacketsToDownlinkSwitches();
        forwardPacketsToUplinkSwitches();
        forwardPacketsToHosts();
    }

    /**
     * Gets the list of packets to be sent to each Downlink Switch
     * and forward them.
     *
     * @see #downlinkSwitchPacketMap
     */
    private void forwardPacketsToDownlinkSwitches() {
        for (final var targetSwitch : downlinkSwitchPacketMap.keySet()) {
            final var hostPktList = getDownlinkSwitchPacketList(targetSwitch);
            hostPktList.forEach(hostPkt -> this.downloadSoFar += hostPkt.getSize());
            applyChannelConstraints(targetSwitch, hostPktList, CloudSimTag.NETWORK_EVENT_DOWN);
        }
    }

    /**
     * Gets the list of packets to be sent to each Uplink Switch
     * and forward them.
     *
     * @see #uplinkSwitchPacketMap
     */
    private void forwardPacketsToUplinkSwitches() {
        for (final var targetSwitch : uplinkSwitchPacketMap.keySet()) {
            final var hostPktList = getUplinkSwitchPacketList(targetSwitch);
            hostPktList.forEach(hostPkt -> this.uploadSoFar += hostPkt.getSize());
            applyChannelConstraints(targetSwitch, hostPktList, CloudSimTag.NETWORK_EVENT_UP);
        }
    }

    /**
     * Applies channel constraint regarding bandwidth which may delay the
     * transmission
     *
     * @param targetSwitch
     * @param hostPktList
     * @param tag
     */
    public void applyChannelConstraints(final NetworkElement targetSwitch, List<HostPacket> hostPktList,
            final CloudSimTag tag) {
        // here we get the bw from the defined channel in the defined physical link in
        // the routing table
        PhysicalLink link = this.getRoutingTable().resolve(targetSwitch);
        for (final var flowId : link.getFlowToChannelsMapping().keySet()) {
            final double bw = link.getChannelByFlowId(flowId).getVirtualLink().getRequiredBandwidth();
            if (tag == CloudSimTag.NETWORK_EVENT_HOST) {
                forwardPacketsToSwitch(this,
                        hostPktList.stream().filter(pkt -> pkt.getVmPacket().getFlowId() == flowId).toList(),
                        bw, tag);
            } else {
                forwardPacketsToSwitch((Switch) targetSwitch,
                        hostPktList.stream().filter(pkt -> pkt.getVmPacket().getFlowId() == flowId).toList(), bw, tag);
            }
        }
        hostPktList.clear();
    }

    private void forwardPacketsToSwitch(
            final Switch destinationSwitch, final List<HostPacket> packetList,
            final double bandwidth, final CloudSimTag tag) {
        if (packetList.size() > 0) {
            final double delay = packetTransferDelay(packetList.get(0), bandwidth, packetList.size());
            // if delay lower that scheduling interval then sent all packets at once
            if (delay < this.getDatacenter().getSchedulingInterval()) {
                for (final HostPacket pkt : packetList) {
                    send(destinationSwitch, delay, tag, pkt);
                }
            } else {
                // calculate number of packets to send every {@link
                // Datacenter::getSchedulingInterval()}
                final int packetsInSecond = (int) Math.floor(packetList.size() / delay);
                // final int batchSize = (int) (packetsInSecond *
                // this.getDatacenter().getSchedulingInterval());
                sendPacketsInBatches(packetList, packetsInSecond > 0 ? packetsInSecond : 1, destinationSwitch, tag);
            }
        }
    }

    /**
     * Sends the packets in batches, this will allow simulate sending packets the
     * real way and will help in drawing traffic graph if the state history is
     * enabled
     */
    private void sendPacketsInBatches(final List<HostPacket> packetList, final int batchSize,
            final Switch destinationSwitch, final CloudSimTag tag) {
        int start = 0;
        int end = batchSize;
        int count = packetList.size() / batchSize;
        final int remainder = packetList.size() % batchSize;
        int counter = 0;
        for (int i = 0; i < count; i++) {
            for (counter = start; counter < end; counter++) {
                send(destinationSwitch, (i + 1), tag,
                        packetList.get(counter));
            }
            start = start + batchSize;
            end = end + batchSize;
        }
        if (remainder != 0) {
            end = end - batchSize + remainder;
            for (counter = start; counter < end; counter++) {
                send(destinationSwitch, (count + 1), tag,
                        packetList.get(counter));
            }
        }
    }

    /**
     * Gets the list of packets to be sent to each Host
     * and forward them.
     *
     * @see #packetToHostMap
     */
    private void forwardPacketsToHosts() {
        for (final NetworkHost host : packetToHostMap.keySet()) {
            final var hostPktList = getHostPacketList(host);
            hostPktList.forEach(hostPkt -> this.downloadSoFar += hostPkt.getSize());
            applyChannelConstraints(host, hostPktList, CloudSimTag.NETWORK_EVENT_HOST);
        }
    }

    @Override
    public double downlinkTransferDelay(final HostPacket packet, final int simultaneousPackets) {
        return packetTransferDelay(packet, downlinkBandwidth, simultaneousPackets);
    }

    @Override
    public double uplinkTransferDelay(final HostPacket packet, final int simultaneousPackets) {
        return packetTransferDelay(packet, uplinkBandwidth, simultaneousPackets);
    }

    /**
     * Computes the network delay for sending a packet through the network,
     * considering that a list of packets will be sent simultaneously.
     *
     * @param netPkt              the packet to be sent
     * @param bwCapacity          the total bandwidth capacity (in Megabits/s)
     * @param simultaneousPackets number of packets to be simultaneously sent
     * @return the expected time to transfer the packet through the network (in
     *         seconds)
     */
    public double packetTransferDelay(
            final HostPacket netPkt, final double bwCapacity, final int simultaneousPackets) {
        return bytesToMegaBits(netPkt.getSize()) / bandwidthByPacket(bwCapacity, simultaneousPackets);
    }

    /**
     * Considering a list of packets to be sent,
     * gets the amount of available bandwidth for each packet,
     * assuming that the bandwidth is shared equally among all packets.
     *
     * @param bwCapacity          the total bandwidth capacity to share among
     *                            the packets to be sent (in Megabits/s)
     * @param simultaneousPackets number of packets to be simultaneously sent
     * @return the available bandwidth for each packet in the list of packets to
     *         send (in Megabits/s)
     *         or the total bandwidth capacity if the packet list has 0 or 1 element
     */
    protected double bandwidthByPacket(final double bwCapacity, final int simultaneousPackets) {
        return simultaneousPackets == 0 ? bwCapacity : bwCapacity / (double) simultaneousPackets;
    }

    @Override
    public void shutdown() {
        super.shutdown();
        LOGGER.info("{} is shutting down...", getName());
    }

    @Override
    public double getUplinkBandwidth() {
        return uplinkBandwidth;
    }

    @Override
    public final void setUplinkBandwidth(double uplinkBandwidth) {
        this.uplinkBandwidth = uplinkBandwidth;
    }

    @Override
    public double getDownlinkBandwidth() {
        return downlinkBandwidth;
    }

    @Override
    public final void setDownlinkBandwidth(double downlinkBandwidth) {
        this.downlinkBandwidth = downlinkBandwidth;
    }

    @Override
    public int getPorts() {
        return ports;
    }

    @Override
    public final void setPorts(final int ports) {
        this.ports = ports;
    }

    @Override
    public double getSwitchingDelay() {
        return switchingDelay;
    }

    @Override
    public final void setSwitchingDelay(final double switchingDelay) {
        this.switchingDelay = switchingDelay;
    }

    @Override
    public List<Switch> getUplinkSwitches() {
        return uplinkSwitches;
    }

    @Override
    public List<Switch> getDownlinkSwitches() {
        return downlinkSwitches;
    }

    /**
     * Gets the list of packets to be sent to a downlink switch.
     *
     * @param downlinkSwitch the id of the switch to get the list of packets to send
     * @return the list of packets to be sent to the given switch.
     */
    protected List<HostPacket> getDownlinkSwitchPacketList(final Switch downlinkSwitch) {
        return downlinkSwitchPacketMap.getOrDefault(downlinkSwitch, new ArrayList<>());
    }

    /**
     * Gets the list of packets to be sent to an uplink switch.
     *
     * @param uplinkSwitch the switch to get the list of packets to send
     * @return the list of packets to be sent to the given switch.
     */
    protected List<HostPacket> getUplinkSwitchPacketList(final Switch uplinkSwitch) {
        return uplinkSwitchPacketMap.getOrDefault(uplinkSwitch, new ArrayList<>());
    }

    /**
     * Gets the list of packets to be sent to a host.
     *
     * @param host the host to get the list of packets to send
     * @return the list of packets to be sent to the given host.
     */
    protected List<HostPacket> getHostPacketList(final NetworkHost host) {
        return packetToHostMap.getOrDefault(host, new ArrayList<>());
    }

    /**
     * Adds a packet that will be sent to a downlink {@link Switch}.
     *
     * @param downlinkSwitch the target switch
     * @param packet         the packet to be sent
     */
    protected void addPacketToSendToDownlinkSwitch(final Switch downlinkSwitch, final HostPacket packet) {
        computeMapValue(downlinkSwitchPacketMap, downlinkSwitch, packet);
    }

    protected void addPacketToBeSentToFirstUplinkSwitch(final HostPacket netPkt) {
        final Switch uplinkSw = getUplinkSwitches().get(0);
        addPacketToSendToUplinkSwitch(uplinkSw, netPkt);
    }

    /**
     * Adds a packet that will be sent to a uplink {@link Switch}.
     *
     * @param uplinkSwitch the target switch
     * @param packet       the packet to be sent
     */
    protected void addPacketToSendToUplinkSwitch(final Switch uplinkSwitch, final HostPacket packet) {
        computeMapValue(uplinkSwitchPacketMap, uplinkSwitch, packet);
    }

    /**
     * Adds a packet that will be sent to a {@link NetworkHost}.
     *
     * @param host   the target {@link NetworkHost}
     * @param packet the packet to be sent
     */
    protected void addPacketToSendToHost(final NetworkHost host, final HostPacket packet) {
        computeMapValue(packetToHostMap, host, packet);
    }

    /**
     * Computes a value for a multi-map, a map where values are a List.
     *
     * @param map        the map to compute a value (to add a value to a List mapped
     *                   to a key)
     * @param key        the key to access the mapped List
     * @param valueToAdd the value to add to the List
     * @param <K>        type of the map key
     * @param <V>        type of the map value
     */
    private <K, V> void computeMapValue(final Map<K, List<V>> map, final K key, final V valueToAdd) {
        map.compute(key, (k, list) -> list == null ? new ArrayList<>() : list).add(valueToAdd);
    }

    @Override
    public NetworkDatacenter getDatacenter() {
        return datacenter;
    }

    @Override
    public void setDatacenter(final NetworkDatacenter datacenter) {
        this.datacenter = datacenter;
    }

    @Override
    public PowerModelSwitch getPowerModel() {
        return powerModel;
    }

    @Override
    public final void setPowerModel(final PowerModelSwitch powerModel) {
        requireNonNull(powerModel,
                "powerModel cannot be null. You could provide a " +
                        PowerModelSwitch.class.getSimpleName() + ".NULL instead.");

        if (powerModel.getSwitch() != null && powerModel.getSwitch() != Switch.NULL
                && !this.equals(powerModel.getSwitch())) {
            throw new IllegalStateException(
                    "The given PowerModel is already assigned to another Switch. Each Host must have its own PowerModel instance.");
        }

        this.powerModel = powerModel;
        powerModel.setSwitch(this);
    }

    @Override
    public void enableStateHistory() {
        this.stateHistoryEnabled = true;
    }

    @Override
    public void disableStateHistory() {
        this.stateHistoryEnabled = false;
    }

    @Override
    public boolean isStateHistoryEnabled() {
        return this.stateHistoryEnabled;
    }

    @Override
    public List<NetworkElementStateEntry> getStateHistory() {
        return Collections.unmodifiableList(this.stateHistory);
    }

    @Override
    public SwitchResourceStats getNetworkElementStats() {
        return this.utilizationStats;
    }

    @Override
    public void enableUtilizationStats() {
        if (utilizationStats != null && utilizationStats != utilizationStats.NULL) {
            return;
        }

        this.utilizationStats = new SwitchResourceStats(this, Switch::getSateEntrySoFar);
    }

    @Override
    public NetworkElementStateEntry getSateEntrySoFar() {
        return new NetworkElementStateEntry(this.getSimulation().clock(), 0, this.uploadSoFar,
                0, this.downloadSoFar);
    }

    private void addStateHistory(final double currentTime) {
        if (!stateHistoryEnabled) {
            return;
        }

        addStateHistoryEntry(currentTime, 0, this.uploadSoFar, 0, this.downloadSoFar);

    }

    /**
     * Adds a switch state history entry.
     *
     * @param time              the time
     * @param upChannelsSoFar   the number of up channels
     * @param uploadSoFar       the amount of uploaded bytes through uplinks
     * @param downChannelsSoFar the number of down channels
     * @param downloadSoFar     the amount of downloaded bytes through downlinks
     */
    private void addStateHistoryEntry(
            final double time,
            final long upChannelsSoFar,
            final long uploadSoFar,
            final long downChannelsSoFar,
            final long downloadSoFar) {
        final var newState = new NetworkElementStateEntry(time, upChannelsSoFar, uploadSoFar, downChannelsSoFar,
                downloadSoFar);
        if (!stateHistory.isEmpty()) {
            final NetworkElementStateEntry previousState = stateHistory.get(stateHistory.size() - 1);
            if (previousState.time() == time) {
                stateHistory.set(stateHistory.size() - 1, newState);
                return;
            }
        }

        stateHistory.add(newState);
        this.uploadSoFar = 0;
        this.downloadSoFar = 0;
    }

    public RoutingTable getRoutingTable() {
        return routingTable;
    }

    public ForwardingTable getForwardingTable() {
        return forwardingTable;
    }

    protected void forwardToDefaultRoute(HostPacket pkt) {
        // the destination host is not known, send to default route
        PhysicalLink defaultRouteLink = this.getRoutingTable().resolve(null);
        // ToDo: how to select the proper link
        if (defaultRouteLink != null) {
            // packet needs to go to through the default routing links
            addPacketToSendToUplinkSwitch((Switch) defaultRouteLink.getDest(), pkt);
        }
    }

    public double getLastBusyTime() {
        return this.lastBusyTime;
    }

    /**
     * {@inheritDoc}
     */
    public double getNetworkElementIdleInterval() {
        return getSimulation().clock() - getLastBusyTime();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isNetworkElementIdle() {
        return getNetworkElementIdleInterval() > 0;
    }
}
