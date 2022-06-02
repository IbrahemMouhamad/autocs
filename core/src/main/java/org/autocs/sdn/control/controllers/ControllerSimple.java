/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.control.controllers;

import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.network.switches.EdgeSwitch;
import org.cloudbus.cloudsim.network.switches.Switch;
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology;
import org.cloudbus.cloudsim.network.topologies.TopologicalLink;

import java.util.List;

import org.autocs.sdn.control.network.physical.PhysicalLink;
import org.autocs.sdn.control.network.virtual.VirtualLink;
import org.autocs.sdn.data.networkelement.NetworkElement;

/**
 * Simple SDN controller which uses {@link BriteNetworkTopology}, which is
 * generated using a file in
 * <a href="http://www.cs.bu.edu/brite/user_manual/node29.html">BRITE
 * format</a>, to build routing tables in all {@link NetworkElement}s in one
 * {@link NetworkDatacenter}.
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public class ControllerSimple extends AbstractedController<BriteNetworkTopology> {

    public ControllerSimple(NetworkDatacenter datacenter) {
        super(datacenter);
    }

    /**
     * {@inheritDoc}
     */
    void buildRoutingTablesInHosts(BriteNetworkTopology topology) {
        for (NetworkHost host : this.getDatacenter().getHostList()) {
            /**
             * Here we consider that there is only one edge switch and the delay between the
             * host and the edge switch is ignored, so we set the topological link to null
             * and add a default
             * routing from host to edge switch (destination = null)
             */
            PhysicalLink link = new PhysicalLink(
                    host,
                    host.getEdgeSwitch(),
                    Math.min(host.getBw().getCapacity(), (long) host.getEdgeSwitch().getDownlinkBandwidth()));
            host.getRoutingTable().addRule(null, link);
            // add loopback link
            host.getRoutingTable().addRule(host, new PhysicalLink(host, host, host.getBw().getCapacity()));
        }
    }

    /**
     * {@inheritDoc}
     */
    void buildRoutingTablesInSwitches(BriteNetworkTopology topology) {
        buildDefaultRoutingForSwitches(topology);

        // Edge switch -> then we should add routing rules to the connected hosts
        for (Switch switchNode : this.getDatacenter().getSwitchMap()) {
            // Edge switch LEVEL = 2
            if (switchNode.getLevel() == 2) {
                // get all connected hosts and add rule for them.
                for (NetworkHost host : ((EdgeSwitch) switchNode).getHostList()) {
                    PhysicalLink link = new PhysicalLink(
                            switchNode,
                            host,
                            Math.min(host.getBw().getCapacity(), (long) switchNode.getDownlinkBandwidth()));
                    switchNode.getRoutingTable().addRule(host, link);
                }
            }
        }

        // Aggregate switch -> then we should add routing rules to the downlink hosts
        // through their edge switch
        for (Switch switchNode : this.getDatacenter().getSwitchMap()) {
            if (switchNode.getLevel() == 1) { // Aggregate switch LEVEL = 1
                // iterate through already known destination for this aggregate switch
                for (NetworkElement edgeSwitch : switchNode.getRoutingTable().getKnownDestination()) {
                    // double check if it is a switch (Aggregate switches are
                    // connected to other switches only)
                    // Edge switch LEVEL = 2
                    if (edgeSwitch instanceof Switch && ((Switch) edgeSwitch).getLevel() == 2) {
                        // get all connected hosts and add rule for them.
                        for (NetworkHost host : ((EdgeSwitch) edgeSwitch).getHostList()) {
                            PhysicalLink link = new PhysicalLink(
                                    switchNode,
                                    edgeSwitch,
                                    topology.getTopologicalLink((SimEntity) switchNode, (SimEntity) edgeSwitch));
                            switchNode.getRoutingTable().addRule(host, link);
                        }
                    }
                }
            }
        }

        // Root switch -> then we should add routing rules to the downlink hosts
        // through their aggregate switch
        for (Switch switchNode : this.getDatacenter().getSwitchMap()) {
            if (switchNode.getLevel() == 0) { // Aggregate switch LEVEL = 0
                // iterate through already known destination for this root switch
                for (NetworkElement aggregateSwitch : switchNode.getRoutingTable().getKnownDestination()) {
                    // double check if it is a switch (Root switches are
                    // connected to other switches only)
                    // Aggregate switch LEVEL = 1
                    if (aggregateSwitch instanceof Switch && ((Switch) aggregateSwitch).getLevel() == 1) {
                        // get all known destination for this aggregate switch
                        for (NetworkElement networkHost : aggregateSwitch.getRoutingTable().getKnownDestination()) {
                            // double check if it is a network host
                            if (networkHost instanceof NetworkHost) {
                                PhysicalLink link = new PhysicalLink(
                                        switchNode,
                                        aggregateSwitch,
                                        topology.getTopologicalLink((SimEntity) switchNode,
                                                (SimEntity) aggregateSwitch));
                                switchNode.getRoutingTable().addRule(networkHost, link);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Builds the routing table for each switch by adding the directly connected
     * node,
     * then update the routing table to include default routing rule (destination =
     * null).
     * For switches, the default routing link is a link to up level switch.
     * If there are more than one up level switch, then route to first one.
     *
     * @param topology
     */
    public void buildDefaultRoutingForSwitches(BriteNetworkTopology topology) {
        for (Switch switchNode : this.getDatacenter().getSwitchMap()) {
            // get up links where switchNode is the source node
            for (final TopologicalLink edge : topology.getUpLinks(switchNode)) {
                SimEntity destination = topology.getSimEntityByBriteId(edge.getDestNodeID());
                PhysicalLink link = new PhysicalLink(switchNode, (NetworkElement) destination, edge);
                switchNode.getRoutingTable().addRule((NetworkElement) destination, link);
            }

            // update the routing table to set the default routing
            // iterate through already known destination for this aggregate switch
            for (NetworkElement destination : switchNode.getRoutingTable().getKnownDestination()) {
                // double check if it is a switch and is is an up level switch
                if (destination instanceof Switch && ((Switch) destination).getLevel() < switchNode.getLevel()) {
                    PhysicalLink link = new PhysicalLink(
                            switchNode,
                            destination,
                            topology.getTopologicalLink((SimEntity) switchNode,
                                    (SimEntity) destination));
                    // add default route rule
                    switchNode.getRoutingTable().addRule(null, link);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deployVirtualLinkBasedOnShortestPath(SimEntity src, SimEntity dest,
            VirtualLink virtualLink, NetworkHost finalDestination) {

        BriteNetworkTopology topology = (BriteNetworkTopology) this.getDatacenter().getSimulation()
                .getNetworkTopology();
        List<Integer> shortestPath = this.getDatacenter().getSimulation().getNetworkTopology()
                .getShortestPath(topology.getSimEntityBriteId(src), topology.getSimEntityBriteId(dest));

        // ToDO: get the bottleneck link bandwidth and check the requirement before
        // adding the rules
        for (int nodeId : shortestPath) {
            SimEntity node = topology.getSimEntityByBriteId(nodeId);
            // Here we are dealing with a network element
            if (node instanceof NetworkElement) {
                // gets the next node index in the path
                int nextHopIndex = shortestPath.indexOf(nodeId) + 1;
                // if there is a next node
                if (shortestPath.size() > nextHopIndex) {
                    SimEntity nextHop = topology.getSimEntityByBriteId(shortestPath.get(nextHopIndex));
                    // add rule to the current node
                    addForwardingRule(virtualLink, ((NetworkElement) node).getForwardingTable(),
                            ((NetworkElement) node), (NetworkElement) nextHop, false);
                } else {
                    // we already in the last item in the path
                    // then we add rule to the final destination
                    addForwardingRule(virtualLink, ((NetworkElement) node).getForwardingTable(),
                            ((NetworkElement) node), finalDestination, false);
                }
            }
        }
    }
}
