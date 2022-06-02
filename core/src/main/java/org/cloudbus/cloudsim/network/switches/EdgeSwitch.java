/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */
package org.cloudbus.cloudsim.network.switches;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.autocs.sdn.control.network.physical.PhysicalLink;
import org.autocs.sdn.control.tables.ForwardingKey;
import org.autocs.sdn.data.networkelement.NetworkElement;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.network.HostPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an Edge Switch in a Datacenter network, which can be connected to
 * {@link NetworkHost}s.
 * It interacts with other Datacenter in order to exchange packets.
 *
 * <br>
 * Please refer to following publication for more details:<br>
 * <ul>
 * <li>
 * <a href="https://doi.org/10.1109/UCC.2011.24">
 * Saurabh Kumar Garg and Rajkumar Buyya, NetworkCloudSim: Modelling Parallel
 * Applications in Cloud Simulations, Proceedings of the 4th IEEE/ACM
 * International Conference on Utility and Cloud Computing (UCC 2011, IEEE CS
 * Press, USA), Melbourne, Australia, December 5-7, 2011.
 * </a>
 * </ul>
 *
 * @author Saurabh Kumar Garg
 * @author Manoel Campos da Silva Filho
 * @author Ibrahem Mouhamad
 * @since AutoCS Core Package 1.0.0
 */
public class EdgeSwitch extends AbstractSwitch {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSwitch.class.getSimpleName());
    /**
     * The level (layer) of the switch in the network topology.
     */
    public static final int LEVEL = 2;

    /**
     * Default number of ports that defines the number of
     * {@link Host} that can be connected to the switch.
     */
    public static final int PORTS = 4;

    /**
     * Default downlink bandwidth of EdgeSwitch in Megabits/s.
     * It also represents the uplink bandwidth of connected hosts.
     */
    private static final long DEF_DOWNLINK_BW = 100 * 8;

    /**
     * Default switching delay in milliseconds.
     */
    private static final double DEF_SWITCHING_DELAY = 0.00157;

    /**
     * List of hosts connected to the switch.
     */
    private final List<NetworkHost> hostList;

    /**
     * Instantiates a EdgeSwitch specifying Datacenter that are connected to its
     * downlink and uplink ports, and corresponding bandwidths. In this switch,
     * downlink ports aren't connected to other switch but to hosts.
     *
     * @param simulation the CloudSim instance that represents the simulation the
     *                   Entity belongs
     * @param dc         The Datacenter where the switch is connected to
     */
    public EdgeSwitch(final CloudSim simulation, final NetworkDatacenter dc) {
        super(simulation, dc);

        this.hostList = new ArrayList<>();
        setUplinkBandwidth(AggregateSwitch.DOWNLINK_BW);
        setDownlinkBandwidth(DEF_DOWNLINK_BW);
        setSwitchingDelay(DEF_SWITCHING_DELAY);
        setPorts(PORTS);
    }

    @Override
    protected void processPacketDown(final SimEvent evt) {
        super.processPacketDown(evt);

        // packet is to be received by host
        final HostPacket pkt = extractReceivedHostPacket(evt);
        // packetProcessingUsingRoutingTable(pkt, false);
        packetProcessingUsingForwardingTable(pkt, false);
    }

    private HostPacket extractReceivedHostPacket(final SimEvent evt) {
        final var pkt = (HostPacket) evt.getData();
        final var receiverVm = pkt.getVmPacket().getDestination();
        final var host = getVmHost(receiverVm);
        pkt.setDestination(host);
        return pkt;
    }

    @Override
    protected void processPacketUp(final SimEvent evt) {
        super.processPacketUp(evt);

        /*
         * packet is received from host and to be sent to
         * aggregate level or to another host in the same level
         */
        final HostPacket pkt = extractReceivedHostPacket(evt);
        // packetProcessingUsingRoutingTable(pkt, true);
        packetProcessingUsingForwardingTable(pkt, true);
    }

    /**
     * use forwarding table to process the traffic when SDN is enabled
     *
     * @param pkt
     */
    private void packetProcessingUsingForwardingTable(HostPacket pkt, final boolean toUp) {
        ForwardingKey forwardingKey = new ForwardingKey(
                pkt.getVmPacket().getSource().getId(),
                pkt.getVmPacket().getDestination().getId(),
                pkt.getVmPacket().getFlowId());

        NetworkElement node = this.getForwardingTable().resolve(forwardingKey);

        if (node != null && node instanceof NetworkHost) {
            // to host
            addPacketToSendToHost((NetworkHost) node, pkt);
            return;
        } else if (node != null && node instanceof Switch) {
            // to switch
            addPacketToSendToUplinkSwitch((Switch) node, pkt);
            return;
        } else if (toUp) {
            forwardToDefaultRoute(pkt);
            return;
        }
        LOGGER.error("destination unreachable for packet from Vm-{} to Vm-{} with flow(Id={}).",
                forwardingKey.getSrc(), forwardingKey.getDest(), forwardingKey.getFlowId());
    }

    /**
     * Uses routing table to process the traffic when SDN not enabled
     *
     * @param pkt
     */
    private void packetProcessingUsingRoutingTable(HostPacket pkt, final boolean toUp) {
        // resolve the destination host using the routing table if it is a downlink host
        PhysicalLink linkToHost = this.getRoutingTable().resolve(pkt.getDestination());
        // send packet to the host using the first link
        // ToDo: how to select the proper link
        if (linkToHost != null) {
            // packet needs to go to a host which is connected directly to switch
            addPacketToSendToHost(pkt.getDestination(), pkt);
            return;
        } else if (toUp) {
            forwardToDefaultRoute(pkt);
            return;
        }
        LOGGER.error("destination unreachable for packet from {} to {}.",
                pkt.getSource().getName(), pkt.getDestination().getName());
    }

    @Override
    public int getLevel() {
        return LEVEL;
    }

    /**
     * Gets a <b>read-only</b> list of Hosts connected to the switch.
     *
     * @return
     */
    public List<NetworkHost> getHostList() {
        return Collections.unmodifiableList(hostList);
    }

    /**
     * Connects a {@link NetworkHost} to the switch, by adding it to the
     * {@link #getHostList()}.
     *
     * @param host the host to be connected to the switch
     */
    public void connectHost(final NetworkHost host) {
        hostList.add(Objects.requireNonNull(host));
        host.setEdgeSwitch(this);
    }

    /**
     * Disconnects a {@link NetworkHost} from the switch, by removing it from the
     * {@link #getHostList()}.
     *
     * @param host the host to be disconnected from the switch
     * @return true if the Host was connected to the switch, false otherwise
     */
    public boolean disconnectHost(final NetworkHost host) {
        if (hostList.remove(host)) {
            host.setEdgeSwitch(null);
            return true;
        }

        return false;
    }
}
