/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.control.controllers;

import java.util.HashMap;
import java.util.Map;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.network.topologies.NetworkTopology;

import org.autocs.sdn.control.network.virtual.Channel;
import org.autocs.sdn.control.network.virtual.VirtualLink;
import org.autocs.sdn.control.network.virtual.VirtualTopology;
import org.autocs.sdn.control.tables.ForwardingTable;
import org.autocs.sdn.data.networkelement.NetworkElement;
import org.autocs.sdn.control.network.physical.PhysicalLink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract class for implementing SDN Controller
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public abstract class AbstractedController<T extends NetworkTopology> implements Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractedController.class.getSimpleName());

    /**
     * datacenter object where the controller was installed
     */
    private final NetworkDatacenter datacenter;

    /**
     * user to virtual topology mapping, we assume that every user,
     * {@link DatacenterBroker}, own one virtual topology
     */
    private Map<DatacenterBroker, VirtualTopology> userToVirtualTopologyMap;

    public AbstractedController(NetworkDatacenter datacenter) {
        this.datacenter = datacenter;
        this.userToVirtualTopologyMap = new HashMap<>();
        // We assume that the network topology is already created
        this.buildRoutingTables();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void buildRoutingTables() {
        this.buildRoutingTablesInHosts((T) this.datacenter.getSimulation().getNetworkTopology());
        this.buildRoutingTablesInSwitches((T) this.datacenter.getSimulation().getNetworkTopology());
    }

    /**
     * Builds the routing tables in all {@link Switch}s
     */
    abstract void buildRoutingTablesInSwitches(T topology);

    /**
     * Builds the routing tables in all {@link NetworkHost}s
     */
    abstract void buildRoutingTablesInHosts(T topology);

    /**
     * {@inheritDoc}
     */
    public void buildForwardingTables(DatacenterBroker user) {
        this.deployVirtualTopology(getUserToVirtualTopologyMap().get(user));
    }

    /**
     * Builds forwarding tables in all network elements using the virtual topology
     * which is owned by one user
     *
     * @param virtualTopology
     */
    private void deployVirtualTopology(VirtualTopology virtualTopology) {
        for (VirtualLink link : virtualTopology.getAllLinks()) {
            deployVirtualLink(link);
        }
    }

    private void deployVirtualLink(VirtualLink link) {
        NetworkHost srcHost = link.getVirtualTopology().getNetworkVmById(link.getSrcId()).getHost();
        NetworkHost destHost = link.getVirtualTopology().getNetworkVmById(link.getDstId()).getHost();
        if (srcHost == null || destHost == null) {
            return;
        }

        /**
         * first case: the source and destination vms in the same host -> add loopback
         */
        if (srcHost.equals(destHost)) {
            addForwardingRule(link, srcHost.getForwardingTable(), srcHost, srcHost, false);
            return;
        }

        // Add rule in the source host to forward to its edge switch
        addForwardingRule(link, srcHost.getForwardingTable(), srcHost, srcHost.getEdgeSwitch(), true);

        /**
         * second case: the source and destination vms in different hosts, which they
         * directly connected to the same edge switch
         */
        if (srcHost.getEdgeSwitch().equals(destHost.getEdgeSwitch())) {
            // Add rule to their edge switch
            addForwardingRule(link, srcHost.getEdgeSwitch().getForwardingTable(), srcHost.getEdgeSwitch(), destHost,
                    true);
            return;
        }

        /**
         * third case: the source and destination vms in different hosts, which they
         * directly connected to different edge switches
         *
         * Here we should get the shortest path from the physical topology and add
         * forwarding rule for each node with this path
         */
        deployVirtualLinkBasedOnShortestPath(srcHost.getEdgeSwitch(), destHost.getEdgeSwitch(),
                link, destHost);

    }

    /**
     * Tries to adding new forwarding rule
     *
     * @param vLink
     * @param table
     * @param src
     * @param dest
     * @return
     */
    protected void addForwardingRule(final VirtualLink vLink, final ForwardingTable table, final NetworkElement src,
            final NetworkElement dest, final boolean useDefaultRoute) {
        // get the physical link to the destination
        PhysicalLink Plink = null;
        if (!useDefaultRoute) {
            Plink = src.getRoutingTable().resolve(dest);
        } else {
            Plink = src.getRoutingTable().resolve(null);
        }

        if (Plink == null) {
            LOGGER.error(
                    "No physical link between {} and {} was found when trying to add forwarding rule for flow(Id={}).",
                    src.getName(), dest.getName(), vLink.getId());
        } else if (deployChannelToPhysicalLink(vLink, Plink)) {
            table.addRule(vLink.getSrcId(), vLink.getDstId(), vLink.getId(),
                    dest);
        } else {
            LOGGER.error("No bandwidth available to deploy flow(Id={} in the link between {} and {}", vLink.getId(),
                    src.getName(), dest.getName());
        }
    }

    /**
     * Tries to deploy a channel in a physical link
     *
     * @param vLink
     * @param Plink
     * @return
     */
    private boolean deployChannelToPhysicalLink(VirtualLink vLink, PhysicalLink Plink) {
        Channel channel = new Channel(vLink);
        return Plink.addChanel(channel, vLink.getId());
    }

    /**
     * Gets the shortest path between two nodes and adds forwarding rules in each
     * node for a specific virtual link
     */
    abstract public void deployVirtualLinkBasedOnShortestPath(SimEntity srcId, SimEntity destId,
            VirtualLink virtualLink, NetworkHost finalDestination);

    public Map<DatacenterBroker, VirtualTopology> getUserToVirtualTopologyMap() {
        return userToVirtualTopologyMap;
    }

    @Override
    public NetworkDatacenter getDatacenter() {
        return this.datacenter;
    }

    public void deployUserApplication(DatacenterBroker broker, VirtualTopology virtualTopology) {
        this.userToVirtualTopologyMap.put(broker, virtualTopology);
        buildForwardingTables(broker);
    }

}
