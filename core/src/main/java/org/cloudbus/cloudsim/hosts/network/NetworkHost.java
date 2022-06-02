/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */
package org.cloudbus.cloudsim.hosts.network;

import org.cloudbus.cloudsim.core.CloudSimTag;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.hosts.HostSuitability;
import org.cloudbus.cloudsim.network.HostPacket;
import org.cloudbus.cloudsim.network.VmPacket;
import org.cloudbus.cloudsim.network.switches.EdgeSwitch;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.schedulers.cloudlet.network.CloudletTaskScheduler;
import org.cloudbus.cloudsim.schedulers.cloudlet.network.CloudletTaskSchedulerSimple;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.vms.Vm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.autocs.sdn.data.networkelement.NetworkElement;
import org.autocs.sdn.control.tables.RoutingTable;
import org.autocs.sdn.control.network.physical.PhysicalLink;
import org.autocs.sdn.control.tables.ForwardingTable;

/**
 * NetworkHost class extends {@link HostSimple} to support simulation of
 * networked datacenters. It executes actions related to management of packets
 * (sent and received) other than that of virtual machines (e.g., creation and
 * destruction). A host has a defined policy for provisioning memory and bw, as
 * well as an allocation policy for PE's to virtual machines.
 * Adding a new feature to support flow control by using {@link RoutingTable}
 * and {@link ForwardingTable}
 *
 * <p>
 * Please refer to following publication for more details:
 * <ul>
 * <li>
 * <a href="https://doi.org/10.1109/UCC.2011.24">
 * Saurabh Kumar Garg and Rajkumar Buyya, NetworkCloudSim: Modelling Parallel
 * Applications in Cloud Simulations, Proceedings of the 4th IEEE/ACM
 * International Conference on Utility and Cloud Computing (UCC 2011, IEEE CS
 * Press, USA), Melbourne, Australia, December 5-7, 2011.
 * </a>
 * </li>
 * </ul>
 * </p>
 *
 * @author Saurabh Kumar Garg
 * @since CloudSim Toolkit 3.0
 */
public class NetworkHost extends HostSimple implements NetworkElement {
    public static final NetworkHost NULL = new NetworkHost();
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkHost.class.getSimpleName());

    private int totalDataTransferBytes;

    /**
     * A buffer of packets to send for VMs inside this Host.
     */
    private final List<HostPacket> pktsToSendForLocalVms;

    /**
     * A buffer of packets to send for VMs outside this Host.
     */
    private final List<HostPacket> pktsToSendForExternalVms;

    /**
     * List of received packets.
     */
    private final List<HostPacket> hostPktsReceived;

    /**
     * Edge switch in which the Host is connected.
     */
    private EdgeSwitch edgeSwitch;

    /**
     * host's routing table
     */
    private RoutingTable routingTable;

    /**
     * host's forwarding table
     */
    private ForwardingTable forwardingTable;

    /**
     * Creates and powers on a NetworkHost using a {@link VmSchedulerSpaceShared} as
     * default.
     *
     * @param ram     the RAM capacity in Megabytes
     * @param bw      the Bandwidth (BW) capacity in Megabits/s
     * @param storage the storage capacity in Megabytes
     * @param peList  the host's {@link Pe} list
     */
    public NetworkHost(final long ram, final long bw, final long storage, final List<Pe> peList) {
        super(ram, bw, storage, peList);
        hostPktsReceived = new ArrayList<>();
        pktsToSendForExternalVms = new ArrayList<>();
        pktsToSendForLocalVms = new ArrayList<>();
        routingTable = new RoutingTable();
        forwardingTable = new ForwardingTable();
    }

    /**
     * Creates an empty host.
     */
    private NetworkHost() {
        this(0, 0, 0, List.of(Pe.NULL));
        setId(-1);
    }

    @Override
    public double updateProcessing(final double currentTime) {
        final double timeOfNextFinishingCloudlet = super.updateProcessing(currentTime);
        receivePackets();
        sendAllPacketListsOfAllVms();

        return timeOfNextFinishingCloudlet;
    }

    /**
     * Receives packets and forwards them to targeting VMs and respective Cloudlets.
     */
    private void receivePackets() {
        for (final HostPacket hostPkt : hostPktsReceived) {
            receivePacket(hostPkt.getVmPacket());
        }

        hostPktsReceived.clear();
    }

    private void receivePacket(final VmPacket vmPacket) {
        final Vm destinationVm = receiveVmPacket(vmPacket);
        // Checks if the destinationVm is inside this host
        if (getVmList().contains(destinationVm)) {
            final CloudletTaskScheduler taskScheduler = getVmPacketScheduler(destinationVm);
            taskScheduler.addPacketToListOfPacketsSentFromVm(vmPacket);
            LOGGER.trace(
                    "{}: {}: {} received pkt with {} bytes from {} in {} and forwarded it to {} in {}",
                    getSimulation().clockStr(), getClass().getSimpleName(), this,
                    vmPacket.getSize(), vmPacket.getSenderCloudlet(), vmPacket.getSource(),
                    vmPacket.getReceiverCloudlet(), vmPacket.getDestination());
        } else
            LOGGER.warn(
                    "{}: {}: Destination {} was not found inside {}",
                    getSimulation().clockStr(), getClass(), vmPacket.getDestination(), this);
    }

    /**
     * Receives a packet that is targeting some VM
     * and sets the packet receive time.
     *
     * @param vmPacket the {@link VmPacket} to receive
     * @return the targeting VM
     */
    private Vm receiveVmPacket(final VmPacket vmPacket) {
        vmPacket.setReceiveTime(getSimulation().clock());
        return vmPacket.getDestination();
    }

    /**
     * Gets all packet lists of all VMs placed into the host and send them all.
     * It checks whether a packet belongs to a local VM or to a VM hosted on other
     * machine.
     */
    private void sendAllPacketListsOfAllVms() {
        getVmList().forEach(this::collectListOfPacketsToSendFromVm);
        sendPacketsToLocalVms();
        sendPacketsToExternalVms();
    }

    /**
     * Gets the packets from the local packets buffer and sends them
     * to VMs inside this host.
     */
    private void sendPacketsToLocalVms() {
        for (final HostPacket hostPkt : pktsToSendForLocalVms) {
            hostPkt.setSendTime(hostPkt.getReceiveTime());
            final Vm destinationVm = receiveVmPacket(hostPkt.getVmPacket());
            // insert the packet in received list
            getVmPacketScheduler(destinationVm).addPacketToListOfPacketsSentFromVm(hostPkt.getVmPacket());
        }

        if (!pktsToSendForLocalVms.isEmpty()) {
            for (final Vm vm : getVmList()) {
                vm.updateProcessing(getVmScheduler().getAllocatedMips(vm));
            }
        }

        pktsToSendForLocalVms.clear();
    }

    /**
     * Sends packets from the local packets buffer to VMs outside this host.
     */
    private void sendPacketsToExternalVms() {
        pktsToSendForExternalVms.forEach(pkt -> totalDataTransferBytes += pkt.getSize());
        // here we get the bw from the defined channel in the defined physical link in
        // the routing table
        PhysicalLink link = this.getRoutingTable().resolve(null);
        // send packets through the flow channel using the requested bandwidth
        for (final var flowId : link.getFlowToChannelsMapping().keySet()) {
            // get channel bandwidth from the physical link
            final double bw = link.getChannelByFlowId(flowId).getVirtualLink().getRequiredBandwidth();
            forwardPacketsToEdgeSwitch(
                    pktsToSendForExternalVms.stream().filter(pkt -> pkt.getVmPacket().getFlowId() == flowId).toList(),
                    bw);
        }

        pktsToSendForExternalVms.clear();
    }

    private void forwardPacketsToEdgeSwitch(final List<HostPacket> packetList, final double bandwidth) {
        if (packetList.size() > 0) {
            final double delay = getEdgeSwitch().packetTransferDelay(packetList.get(0), bandwidth, packetList.size());
            // if delay lower that scheduling interval then sent all packets at once
            if (delay < this.getDatacenter().getSchedulingInterval()) {
                for (final HostPacket pkt : packetList) {
                    getSimulation().send(
                            getDatacenter(), getEdgeSwitch(),
                            delay, CloudSimTag.NETWORK_EVENT_UP, pkt);
                }
            } else {
                // calculate number of packets to send every {@link
                // Datacenter::getSchedulingInterval()}
                final int packetsInSecond = (int) Math.floor(packetList.size() / delay);
                // final int batchSize = (int) (packetsInSecond *
                // this.getDatacenter().getSchedulingInterval());
                sendPacketsInBatches(packetList, packetsInSecond > 0 ? packetsInSecond : 1);
            }
        }
    }

    /**
     * Sends the packets in batches, this will allow simulate sending packets the
     * real way and will help in drawing traffic graph if the state history is
     * enabled
     */
    private void sendPacketsInBatches(final List<HostPacket> packetList, final int batchSize) {
        int start = 0;
        int end = batchSize;
        int count = packetList.size() / batchSize;
        final int remainder = packetList.size() % batchSize;
        int counter = 0;
        for (int i = 0; i < count; i++) {
            for (counter = start; counter < end; counter++) {
                getSimulation().send(
                        getDatacenter(), getEdgeSwitch(),
                        (i + 1), CloudSimTag.NETWORK_EVENT_UP, packetList.get(counter));
            }
            start = start + batchSize;
            end = end + batchSize;
        }
        if (remainder != 0) {
            end = end - batchSize + remainder;
            for (counter = start; counter < end; counter++) {
                getSimulation().send(
                        getDatacenter(), getEdgeSwitch(),
                        (count + 1), CloudSimTag.NETWORK_EVENT_UP, packetList.get(counter));
            }
        }
    }

    private CloudletTaskScheduler getVmPacketScheduler(final Vm vm) {
        return vm.getCloudletScheduler().getTaskScheduler();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * <b>It also creates and sets a {@link CloudletTaskScheduler} for each
     * Vm that doesn't have one already.</b>
     * </p>
     * 
     * @param vm {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public HostSuitability createVm(final Vm vm) {
        final HostSuitability suitability = super.createVm(vm);
        setPacketScheduler(vm);
        return suitability;
    }

    private void setPacketScheduler(final Vm vm) {
        final var scheduler = vm.getCloudletScheduler();
        if (!scheduler.isThereTaskScheduler()) {
            scheduler.setTaskScheduler(new CloudletTaskSchedulerSimple());
        }
    }

    /**
     * Collects all packets of a specific packet list from a Vm
     * in order to get them together to be sent.
     *
     * @param sourceVm the VM from where the packets will be sent
     */
    private void collectListOfPacketsToSendFromVm(final Vm sourceVm) {
        final CloudletTaskScheduler taskScheduler = getVmPacketScheduler(sourceVm);
        for (final VmPacket vmPkt : taskScheduler.getVmPacketsToSend()) {
            collectPacketToSendFromVm(vmPkt);
        }

        taskScheduler.clearVmPacketsToSend();
    }

    /**
     * Collects a specific packet from a given Vm
     * in order to get it together with other packets to be sent.
     *
     * @param vmPkt a packet to be sent from a Vm to another one
     * @see #collectListOfPacketsToSendFromVm(Vm)
     */
    private void collectPacketToSendFromVm(final VmPacket vmPkt) {
        final var hostPkt = new HostPacket(this, vmPkt);
        final Vm receiverVm = vmPkt.getDestination();

        // If the VM is inside this Host, the packet doesn't travel through the network
        final var pktsToSendList = getVmList().contains(receiverVm) ? pktsToSendForLocalVms : pktsToSendForExternalVms;
        pktsToSendList.add(hostPkt);
    }

    public int getTotalDataTransferBytes() {
        return totalDataTransferBytes;
    }

    /**
     * Adds a packet to the list of received packets in order
     * to further submit them to the respective target VMs and Cloudlets.
     *
     * @param hostPacket received network packet
     */
    public void addReceivedNetworkPacket(final HostPacket hostPacket) {
        hostPktsReceived.add(hostPacket);
    }

    /**
     * Gets the Switch the Host is directly connected to.
     * 
     * @return
     */
    public EdgeSwitch getEdgeSwitch() {
        return edgeSwitch;
    }

    /**
     * Sets the Switch the Host is directly connected to.
     * This method is to be called only by the
     * {@link EdgeSwitch#connectHost(NetworkHost)} method.
     * 
     * @param edgeSwitch the Switch to set
     * @return
     */
    public void setEdgeSwitch(final EdgeSwitch edgeSwitch) {
        this.edgeSwitch = edgeSwitch;
    }

    public RoutingTable getRoutingTable() {
        return routingTable;
    }

    public ForwardingTable getForwardingTable() {
        return forwardingTable;
    }

    @Override
    public int getPorts() {
        return 1;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public double getNetworkElementIdleInterval() {
        return this.getIdleInterval();
    }

    @Override
    public boolean isNetworkElementIdle() {
        return this.isIdle();
    }

}
