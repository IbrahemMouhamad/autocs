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
 * Represents a Network Element (NE).
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public interface NetworkElement {
    /**
     * An attribute that implements the Null Object Design Pattern for
     * {@link NetworkElement}
     * objects.
     */
    NetworkElement NULL = new NetworkElementNull();

    /**
     * Gets the overall number of {@link Port}s the network element has.
     *
     * @return the network element's number of ports
     */
    int getPorts();

    /**
     * Gets the routing table of this network element
     *
     * @return
     */
    RoutingTable getRoutingTable();

    /**
     * Gets the forwarding table of this network element
     *
     * @return
     */
    ForwardingTable getForwardingTable();

    /**
     * Gets the network element name
     *
     * @return
     */
    String getName();

    /**
     * Gets the network element id
     *
     * @return
     */
    long getId();

    /**
     * Gets the time interval the NetworkElement has been idle.
     *
     * @return the idle time interval (in seconds) or 0 if the NetworkElement is not
     *         idle
     */
    double getNetworkElementIdleInterval();

    /**
     * Gets the last time the NetworkElement was running some process.
     *
     * @return the last busy time (in seconds)
     */
    double getLastBusyTime();

    /**
     * Checks if the Machine is currently idle.
     *
     * @return true if the Machine currently idle, false otherwise
     */
    boolean isNetworkElementIdle();
}
