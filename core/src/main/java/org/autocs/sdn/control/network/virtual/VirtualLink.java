/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.control.network.virtual;

import org.cloudbus.cloudsim.core.Identifiable;
import org.cloudbus.cloudsim.vms.network.NetworkVm;;

/**
 * Represents a virtual link between two {@link NetworkVm}s, it defined by the
 * user and contains the source and destination {@link NetworkVm} ids, and the
 * required bandwidth.
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public class VirtualLink implements Identifiable {
    /**
     * source {@link NetworkVm} id
     */
    private long srcId;

    /**
     * destination {@link NetworkVm} int
     */
    private long dstId;

    /**
     * the user requirement regarding the link bandwidth in (Mb/s)
     */
    private double requiredBandwidth;

    private long id;

    /**
     * the virtual topology where this link is located
     */
    private VirtualTopology virtualTopology;

    public VirtualLink(final long src, final long dst, final double requiredBandwidth, final long id) {
        this.srcId = src;
        this.dstId = dst;
        this.requiredBandwidth = requiredBandwidth;
        this.id = id;
    }

    /**
     * unique identifier for this link
     */
    @Override
    public long getId() {
        return id;
    }

    public long getSrcId() {
        return srcId;
    }

    public long getDstId() {
        return dstId;
    }

    public double getRequiredBandwidth() {
        return requiredBandwidth;
    }

    public void setRequiredBandwidth(long requiredBandwidth) {
        this.requiredBandwidth = requiredBandwidth;
    }

    public VirtualTopology getVirtualTopology() {
        return virtualTopology;
    }

    public void setVirtualTopology(VirtualTopology virtualTopology) {
        this.virtualTopology = virtualTopology;
    }

}
