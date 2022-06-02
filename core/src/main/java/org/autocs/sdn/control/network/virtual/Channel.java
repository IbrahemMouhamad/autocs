/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.control.network.virtual;

import org.cloudbus.cloudsim.vms.network.NetworkVm;

import org.autocs.sdn.control.network.physical.PhysicalLink;

/**
 * Represents a virtual channel between two {@link NetworkVm}s, it contains
 * the
 * {@link VirtualLink} to access the user requirements.
 * 
 * It will be used by {@link PhysicalLink} to manage the bandwidth sharing
 * between different users.
 * 
 * For one {@link VirtualLink} it will be defined a number of channels which are
 * located in the route links from source and destination {@link NetworkVm}s.
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public class Channel {

    /**
     * instance of the {@link VirtualLink}
     */
    private VirtualLink virtualLink;

    /**
     * the previous time this channel was used
     */
    private double previousTime;

    public Channel(final VirtualLink virtualLink) {
        this.virtualLink = virtualLink;
        this.previousTime = -1;
    }

    public VirtualLink getVirtualLink() {
        return virtualLink;
    }

    /**
     * Bandwidth to be managed for this channel.
     * its capacity should be equal to {@link VirtualLink} required Bandwidth.
     */
    public double getBw() {
        return this.virtualLink != null ? this.virtualLink.getRequiredBandwidth() : 0.0;
    }

    /**
     * Gets the previous time this channel was used
     * 
     * @return
     */
    public double getPreviousTime() {
        return previousTime;
    }

    /**
     * Gets the amount of bytes should be passed
     * through this channel from the last used time
     */
    public long amountToBeProcessed(double currentTime) {
        // first time to use the channel
        if (this.previousTime == -1) {
            this.previousTime = currentTime;
            return Math.round(((getBw() * 1024 * 1024) / 8));
        } else {
            double timeSpent = currentTime - this.previousTime;
            return Math.round(timeSpent * ((getBw() * 1024 * 1024) / 8));
        }
    }
}
