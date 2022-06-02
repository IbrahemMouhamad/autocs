/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.control.tables;

import java.util.ArrayList;
import java.util.List;

import org.autocs.sdn.control.network.physical.PhysicalLink;
import org.autocs.sdn.data.networkelement.NetworkElement;

/**
 * A flow control table that contain information about the next hop of a
 * {@link NetworkElement}
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public class RoutingTable extends AbstractedTable<NetworkElement, List<PhysicalLink>> {

    public RoutingTable() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public void addRule(NetworkElement destination, PhysicalLink link) {
        if (this.getTable().get(destination) == null) {
            this.getTable().put(destination, new ArrayList<>());
        }
        this.getTable().get(destination).add(link);
    }

    /**
     * Gets a list of already known {@link NetworkElement} destinations
     */
    public List<NetworkElement> getKnownDestination() {
        return new ArrayList<NetworkElement>(getTable().keySet());
    }

    /**
     * {@inheritDoc}
     */
    public void print() {
        for (NetworkElement key : this.getTable().keySet()) {
            if (key != null) {
                System.out.println("-> " + key.getName() + " : {");
            } else {
                System.out.println("-> default Routing : {");
            }
            for (PhysicalLink link : this.getTable().get(key)) {
                System.out.println("src: " + link.getSrc().getName() + ", dest: " + link.getDest().getName());
            }
            System.out.println("}");
            System.out.println();
        }
    }

    /**
     *
     * Gets only on {@link PhysicalLink}, for simplicity we return the first one
     */
    public PhysicalLink resolve(NetworkElement key) {
        if (this.getTable().get(key) != null)
            return this.getTable().get(key).stream()
                    .findFirst()
                    .orElse(null);
        return null;
    }
}
