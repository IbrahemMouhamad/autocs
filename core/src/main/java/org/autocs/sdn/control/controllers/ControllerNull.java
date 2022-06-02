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
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEntityNullBase;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;

/**
 * A class that implements the Null Object Design Pattern for {@link Controller}
 * class.
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

final class ControllerNull implements SimEntityNullBase, Controller {
    @Override
    public int compareTo(SimEntity entity) {
        return 0;
    }

    @Override
    public void buildRoutingTables() {
    }

    @Override
    public NetworkDatacenter getDatacenter() {
        return null;
    }

    @Override
    public void buildForwardingTables(DatacenterBroker user) {
    }

    @Override
    public Map<DatacenterBroker, VirtualTopology> getUserToVirtualTopologyMap() {
        return null;
    }
}
