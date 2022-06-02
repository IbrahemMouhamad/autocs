/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.control.tables;

import java.util.Objects;

import org.cloudbus.cloudsim.vms.Vm;

/**
 * Represent a forwarding table key, where the addresses here are the addresses
 * of the source and the destination {@link Vm}s
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public class ForwardingKey {

    /**
     * source Vm address
     */
    private final long src;

    /**
     * destination Vm address
     */
    private final long dest;

    /**
     * flow identifier
     */
    private final long flowId;

    private int hashCode;

    public ForwardingKey(final long src, final long dest, final long flowId) {
        this.src = src;
        this.dest = dest;
        this.flowId = flowId;
        this.hashCode = Objects.hash(src, dest, flowId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ForwardingKey that = (ForwardingKey) o;
        return src == that.src && dest == that.dest && flowId == that.flowId;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public long getSrc() {
        return src;
    }

    public long getDest() {
        return dest;
    }

    public long getFlowId() {
        return flowId;
    }
}
