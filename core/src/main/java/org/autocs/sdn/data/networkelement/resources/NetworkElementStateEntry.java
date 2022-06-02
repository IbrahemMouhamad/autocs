/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.data.networkelement.resources;

/*
* Contains the utilization statistics for a given {@link NetworkElement}. It is used to track as a history entry
* @author Ibrahem Mouhamad
* @since AutoCS SDN Package 1.0.0
*/

public record NetworkElementStateEntry(double time, long upChannels, long upload, long downChannels,
        long download) {

    /**
     * Gets the time the data in this history entry is related to.
     *
     * @return
     */
    public double time() {
        return time;
    }

    /**
     * Gets the number of active channels connected to upper level switches at the
     * recorded time.
     *
     * @return
     */
    public long upChannels() {
        return upChannels;
    }

    /**
     * Gets the number of active channels connected to lower level switches at the
     * recorded time including the channels with hosts if the switch is an edge
     * switch
     *
     * @return
     */
    public long downChannels() {
        return downChannels;
    }

    /**
     * Gets the amount of bytes transferred through the switch to upper level at the
     * recorded time.
     *
     * @return
     */
    public long upload() {
        return upload;
    }

    /**
     * Gets the amount of bytes transferred through the switch to lower level at the
     * recorded time including the traffic with hosts if the switch is an edge
     * switch
     *
     * @return
     */
    public long download() {
        return upload;
    }

    @Override
    public String toString() {
        final var msg = "Time: %6.1f |  Up Channels: %d | Upload: %d | Down Channels: %d | Download: %d%n";
        return String.format(msg, time, upChannels, upload, downChannels, download);
    }
}
