/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */
package org.cloudbus.cloudsim.network.switches;

import java.util.Optional;

import org.autocs.sdn.control.network.physical.PhysicalLink;
import org.autocs.sdn.control.tables.ForwardingKey;
import org.autocs.sdn.data.networkelement.NetworkElement;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.network.HostPacket;
import org.cloudbus.cloudsim.util.BytesConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Allows simulating a Root switch which connects Datacenters to
 * an external network. It interacts with other Datacenter in order to exchange
 * packets.
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
 * @author Manoel Campos da Silva Filho
 * @author Ibrahem Mouhamad
 * @since AutoCS Core Package 1.0.0
 */
public class RootSwitch extends AbstractSwitch {
    /**
     * The level (layer) of the switch in the network topology.
     */
    public static final int LEVEL = 0;

    /**
     * Default number of root switch ports that defines the number of
     * {@link AggregateSwitch} that can be connected to it.
     */
    public static final int PORTS = 1;

    /**
     * Default switching delay in milliseconds.
     */
    public static final double SWITCHING_DELAY = 0.00285;

    /**
     * The downlink bandwidth of RootSwitch in Megabits/s.
     * It also represents the uplink bandwidth of connected aggregation Datacenter.
     */
    public static final long DOWNLINK_BW = (long) BytesConversion.gigaToMega(40 * 8); // 40000 Megabits (40 Gigabits)

    private static final Logger LOGGER = LoggerFactory.getLogger(RootSwitch.class.getSimpleName());

    /**
     * Instantiates a Root Switch specifying what other Datacenter are connected
     * to its downlink ports, and corresponding bandwidths.
     *
     * @param simulation the CloudSim instance that represents the simulation the
     *                   Entity belongs
     * @param dc         the Datacenter where the switch is connected to
     */
    public RootSwitch(final CloudSim simulation, final NetworkDatacenter dc) {
        super(simulation, dc);
        setDownlinkBandwidth(DOWNLINK_BW);
        setSwitchingDelay(SWITCHING_DELAY);
        setPorts(PORTS);
    }

    @Override
    protected void processPacketUp(final SimEvent evt) {
        super.processPacketUp(evt);
        final var netPkt = (HostPacket) evt.getData();
        // processPacketUpUsingRoutingTable(netPkt);
        packetProcessingUsingForwardingTable(netPkt);
    }

    /**
     * use forwarding table to process the traffic when SDN is enabled
     *
     * @param netPkt
     */
    private void packetProcessingUsingForwardingTable(HostPacket netPkt) {
        ForwardingKey forwardingKey = new ForwardingKey(
                netPkt.getVmPacket().getSource().getId(),
                netPkt.getVmPacket().getDestination().getId(),
                netPkt.getVmPacket().getFlowId());

        NetworkElement node = this.getForwardingTable().resolve(forwardingKey);

        if (node != null && node instanceof Switch) {
            // to down switch
            if (((Switch) node).getLevel() > this.getLevel()) {
                addPacketToSendToDownlinkSwitch((Switch) node, netPkt);
                return;
            }
        } else {
            forwardToDefaultRoute(netPkt);
        }
        LOGGER.error("destination unreachable for packet from Vm-{} to Vm-{} with flow(Id={}).",
                forwardingKey.getSrc(), forwardingKey.getDest(), forwardingKey.getFlowId());
    }

    /**
     * Uses routing table to process the traffic when SDN not enabled
     *
     * @param netPkt
     */
    private void processPacketUpUsingRoutingTable(HostPacket netPkt) {
        // resolve the destination host using the routing table if it is a downlink host
        PhysicalLink linkToHost = this.getRoutingTable().resolve(netPkt.getDestination());
        // ToDo: how to select the proper link
        if (linkToHost != null) {
            // packet needs to go to a downlink host through an edge switch
            addPacketToSendToDownlinkSwitch((Switch) linkToHost.getDest(), netPkt);
            return;
        } else {
            forwardToDefaultRoute(netPkt);
        }
        LOGGER.error("destination unreachable for packet from {} to {}.",
                netPkt.getSource().getName(), netPkt.getDestination().getName());
    }

    /**
     * Finds which aggregate switch is connected to a given edge switch
     *
     * @param edgeSwitch the id of the edge switch to find the aggregate switch
     *                   that it is connected to
     * @return an {@link Optional} with the aggregate switch that is connected to
     *         the given
     *         edge switch; or an empty Optional if not found.
     */
    private Optional<Switch> findAggregateConnectedToEdgeSwitch(final Switch edgeSwitch) {
        // List of Aggregate Switches connected to this Root Switch
        final var aggregateSwitchList = getDownlinkSwitches();
        return aggregateSwitchList
                .stream()
                .filter(aggregateSw -> isEdgeConnectedToAggregatedSwitch(edgeSwitch, aggregateSw))
                .findFirst();
    }

    private boolean isEdgeConnectedToAggregatedSwitch(final Switch edgeSwitch, final Switch aggregateSw) {
        // List of Edge Switches connected to the given Aggregate Switch
        final var edgeSwitchList = aggregateSw.getDownlinkSwitches();
        return edgeSwitchList.stream().anyMatch(edgeSwitch::equals);
    }

    @Override
    public int getLevel() {
        return LEVEL;
    }
}
