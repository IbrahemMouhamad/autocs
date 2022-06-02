/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.data.networkelement;

import org.autocs.sdn.control.tables.ForwardingTable;
import org.autocs.sdn.control.tables.RoutingTable;

/**
 * A class that implements the Null Object Design Pattern for
 * {@link NetworkElement}.
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public class NetworkElementNull implements NetworkElement {

    @Override
    public int getPorts() {
        return 0;
    }

    @Override
    public RoutingTable getRoutingTable() {
        return null;
    }

    @Override
    public ForwardingTable getForwardingTable() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public double getNetworkElementIdleInterval() {
        return 0;
    }

    @Override
    public double getLastBusyTime() {
        return 0;
    }

    @Override
    public boolean isNetworkElementIdle() {
        return false;
    }
}
