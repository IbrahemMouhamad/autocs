/*
 * Title:        Simulator Package
 * Description:  Simulator package of Auto Cloud Simulator (AutoCS) to extends the functionality of some CloudSim Plus classes
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.cloudbus.cloudsim.datacenters;

/**
 * A java record to keep historic CPU utilization data about all hosts in the
 * datacenter.
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS Core Package 1.0.0
 * @param time           the time the data in this history entry is related to
 * @param allocatedMips  the total MIPS allocated from all PEs of all hosts, t
 *                       running VMs, at the recorded time
 * @param requestedMips  the total MIPS requested by running VMs to all PEs of
 *                       all hosts at the recorded time
 * @param ramUtilization the current utilization of memory (in Megabytes) of
 *                       all hosts at the recorded time
 * @param bwUtilization  the current utilization of bw (in Megabits/s) of
 *                       all hosts at the recorded time
 * @param activeHosts    the number of active hosts at the recorded time
 */

public record DatacenterStateHistoryEntry(double time, double allocatedMips, double requestedMips,
        double ramUtilization, double bwUtilization,
        double activeHosts) {

    /**
     * Gets the time the data in this history entry is related to.
     *
     * @return
     */
    public double time() {
        return time;
    }

    /**
     * Gets the total MIPS allocated from all PEs of all hosts, to running VMs, at
     * the recorded time.
     *
     * @return the allocated mips
     */
    public double allocatedMips() {
        return allocatedMips;
    }

    /**
     * Gets the total MIPS requested by running VMs to all PEs of all hosts at the
     * recorded time.
     *
     * @return the requested mips
     */
    public double requestedMips() {
        return requestedMips;
    }

    /**
     * Gets the percentage (in scale from 0 to 1) of allocated MIPS from the total
     * requested.
     *
     * @return
     */
    public double percentUsage() {
        return requestedMips > 0 ? allocatedMips / requestedMips : 0;
    }

    /**
     * Gets the utilization of memory (in Megabytes).
     *
     * @return
     */
    public double ramUtilization() {
        return ramUtilization;
    }

    /**
     * Gets the utilization of bw (in Megabits/s).
     *
     * @return
     */
    public double bwUtilization() {
        return bwUtilization;
    }

    /**
     * Gets the number of active hosts at the recorded time
     *
     * @return true if is active, false otherwise
     */
    public double activeHosts() {
        return activeHosts;
    }

    @Override
    public String toString() {
        final var msg = "Time: %6.1f | Requested: %10.0f MIPS | Allocated: %10.0f MIPS | Used: %3.0f%% Active Hosts: %s%n";
        return String.format(msg, time, requestedMips, allocatedMips, percentUsage() * 100, activeHosts);
    }
}
