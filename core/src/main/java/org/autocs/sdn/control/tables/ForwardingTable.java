/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.control.tables;

import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.network.NetworkPacket;
import org.cloudbus.cloudsim.network.switches.Switch;
import org.autocs.sdn.data.networkelement.NetworkElement;

/**
 * A flow control table that contain information about the next
 * {@link NetworkElement} to forward a {@link NetworkPacket} to
 * 
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public class ForwardingTable extends AbstractedTable<ForwardingKey, NetworkElement> {

    public ForwardingTable() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public void addRule(final long src, final long dest, final long flowId, NetworkElement nextHop) {
        ForwardingKey key = new ForwardingKey(src, dest, flowId);
        this.getTable().put(key, nextHop);
    }

    /**
     * {@inheritDoc}
     */
    public void print() {
        for (ForwardingKey key : this.getTable().keySet()) {
            String destString = "";
            NetworkElement dest = this.getTable().get(key);
            // switch
            if (this.getTable().get(key) instanceof Switch) {
                destString = "Switch (" + ((Switch) dest).getName() + ")";
            }
            // Host
            if (this.getTable().get(key) instanceof NetworkHost) {
                destString = "NetworkHost (" + ((Host) dest).getClass().getSimpleName() + ")";
            }
            System.out.println("(src: " + key.getSrc() + ", dest: " + key.getDest() + ", flowId: " + key.getFlowId()
                    + ")  --->  " + destString);
            System.out.println();
        }
    }

    /**
     * Gets a destination for a given flow
     *
     * @param key
     * @param value
     */
    public NetworkElement resolve(ForwardingKey key) {
        return this.getTable().get(key);
    }
}
