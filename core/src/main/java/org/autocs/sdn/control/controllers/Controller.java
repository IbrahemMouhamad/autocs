/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */
package org.autocs.sdn.control.controllers;

import java.util.Map;

import org.autocs.sdn.control.network.virtual.VirtualTopology;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.network.switches.Switch;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;

/**
 * Represents SDN Controller
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public interface Controller {

    /**
     * An attribute that implements the Null Object Design Pattern for
     * {@link Controller}
     * objects.
     */
    Controller NULL = new ControllerNull();

    /**
     * Builds the routing tables in all network elements {@link Switch}s and
     * {@link NetworkHost}s
     */
    void buildRoutingTables();

    /**
     * Builds the forwarding tables in all network elements {@link Switch}s and
     * {@link NetworkHost}s
     */
    void buildForwardingTables(DatacenterBroker user);

    /**
     * Gets tha datacenter where the controller is installed
     */
    NetworkDatacenter getDatacenter();

    /**
     * Gets the mapping between every user and their virtual topology
     *
     * @return
     */
    Map<DatacenterBroker, VirtualTopology> getUserToVirtualTopologyMap();

}
