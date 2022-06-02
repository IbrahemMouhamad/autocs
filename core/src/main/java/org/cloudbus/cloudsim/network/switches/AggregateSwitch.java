/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */
package org.cloudbus.cloudsim.network.switches;

import java.util.List;

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
 * This class represents an Aggregate Switch in a Datacenter network.
 * It interacts with other Datacenter in order to exchange packets.
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
public class AggregateSwitch extends AbstractSwitch {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSwitch.class.getSimpleName());
    /**
     * The level (layer) of the switch in the network topology.
     */
    public static final int LEVEL = 1;

    /**
     * Default delay of {@link AggregateSwitch} in milliseconds.
     */
    public static final double SWITCHING_DELAY = 0.00245;

    /**
     * Default downlink bandwidth of {@link AggregateSwitch} in Megabits/s.
     * It also represents the uplink bandwidth of connected edge Datacenter.
     */
    public static final long DOWNLINK_BW = (long) BytesConversion.MEGA * 100 * 8;

    /**
     * Default number of aggregation switch ports that defines the number of
     * {@link EdgeSwitch} that can be connected to it.
     */
    public static final int PORTS = 1;

    /**
     * Instantiates a Aggregate AbstractSwitch specifying the Datacenter that are
     * connected to its downlink and uplink ports and corresponding bandwidths.
     *
     * @param simulation the CloudSim instance that represents the simulation the
     *                   Entity belongs
     * @param dc         The Datacenter where the switch is connected to
     */
    public AggregateSwitch(final CloudSim simulation, final NetworkDatacenter dc) {
        super(simulation, dc);
        setUplinkBandwidth(RootSwitch.DOWNLINK_BW);
        setDownlinkBandwidth(DOWNLINK_BW);
        setSwitchingDelay(SWITCHING_DELAY);
        setPorts(PORTS);
    }

    @Override
    protected void processPacketDown(final SimEvent evt) {
        /*
         * packet is coming from root switch,
         * so it needs to be sent to edge switch
         */
        super.processPacketDown(evt);
        final HostPacket netPkt = (HostPacket) evt.getData();
        // packetProcessingUsingRoutingTable(netPkt, false);
        packetProcessingUsingForwardingTable(netPkt, false);
    }

    /**
     * use forwarding table to process the traffic when SDN is enabled
     *
     * @param netPkt
     */
    private void packetProcessingUsingForwardingTable(HostPacket netPkt, final boolean isUp) {
        ForwardingKey forwardingKey = new ForwardingKey(
                netPkt.getVmPacket().getSource().getId(),
                netPkt.getVmPacket().getDestination().getId(),
                netPkt.getVmPacket().getFlowId());

        NetworkElement node = this.getForwardingTable().resolve(forwardingKey);

        if (node != null && node instanceof Switch) {
            // to upper switch
            if (((Switch) node).getLevel() < this.getLevel()) {
                addPacketToSendToUplinkSwitch((Switch) node, netPkt);
                return;
            }
            addPacketToSendToDownlinkSwitch((Switch) node, netPkt);
            return;
        } else if (isUp) {
            forwardToDefaultRoute(netPkt);
            return;
        }
        LOGGER.error("destination unreachable for packet from Vm-{} to Vm-{} with flow(Id={}).",
                forwardingKey.getSrc(), forwardingKey.getDest(), forwardingKey.getFlowId());
    }

    /**
     * Uses routing table to process the traffic when SDN is not enabled
     *
     * @param netPkt
     */
    private void packetProcessingUsingRoutingTable(HostPacket netPkt, final boolean toUp) {
        // resolve the destination host using the routing table if it is a downlink host
        PhysicalLink linkToHost = this.getRoutingTable().resolve(netPkt.getDestination());
        // ToDo: how to select the proper link
        if (linkToHost != null) {
            // packet needs to go to a downlink host through an edge switch
            addPacketToSendToDownlinkSwitch((Switch) linkToHost.getDest(), netPkt);
            return;
        } else if (toUp) {
            forwardToDefaultRoute(netPkt);
            return;
        }
        LOGGER.error("destination unreachable for packet from {} to {}.",
                netPkt.getSource().getName(), netPkt.getDestination().getName());
    }

    @Override
    protected void processPacketUp(final SimEvent evt) {
        // packet is coming from edge router, so it needs to be sent to either root or
        // another edge switch
        super.processPacketUp(evt);
        final HostPacket netPkt = (HostPacket) evt.getData();
        // packetProcessingUsingRoutingTable(netPkt, true);
        packetProcessingUsingForwardingTable(netPkt, true);

    }

    /**
     * Checks if the Aggregate switch is connected to a given Edge switch.
     * 
     * @param edgeSwitch the id of the edge switch to check if the aggregate switch
     *                   is connected to
     * @return true if the edge switch was found, false otherwise
     */
    private boolean findConnectedEdgeSwitch(final Switch edgeSwitch) {
        return getDownlinkSwitches().stream().anyMatch(edgeSwitch::equals);
    }

    @Override
    public int getLevel() {
        return LEVEL;
    }
}
