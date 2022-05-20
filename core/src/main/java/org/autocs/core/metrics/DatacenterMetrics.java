/*
 * Title:        Simulator Package
 * Description:  Simulator package of Auto Cloud Simulator (AutoCS) to extends the functionality of some CloudSim Plus classes
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.metrics;

import java.util.List;

import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterStateHistoryEntry;

/**
 * Collects {@link Datacenter} metrics
 *
 * @author Ibrahem Mouhamad
 * @since Simulator Package 1.0.0
 */

public class DatacenterMetrics {
    private String name;
    private double cpuUtilizationAverage;
    private List<DatacenterStateHistoryEntry> history;

    public DatacenterMetrics() {
    }

    public DatacenterMetrics(Datacenter datacenter) {
        this.name = datacenter.getName();
        this.setCpuUtilizationAverage(datacenter);
        this.history = datacenter.getStateHistory();
    }

    /*
     * Gets average cpu utilization across all hosts
     */
    private void setCpuUtilizationAverage(Datacenter datacenter) {
        this.cpuUtilizationAverage = datacenter.getHostList().stream()
                .filter(host -> host.isActive())
                .mapToDouble(host -> host.getCpuUtilizationStats().getMean()
                        * 100)
                .average()
                .orElse(0);
    }

    public double getCpuUtilizationAverage() {
        return cpuUtilizationAverage;
    }

    /*
     * public Map<String, Double> getMetrics() {
     * Map<String, Double> metrics = new HashMap<>();
     * metrics.put(datacenterName + " Cpu Utilization", this.cpuUtilizationAverage);
     * return metrics;
     * }
     */

    /*
     * @Override
     * public String toString() {
     * Map<String, Double> metrics = this.getMetrics();
     * String mapAsString = metrics.keySet().stream()
     * .map(key -> key + ": " + metrics.get(key))
     * .collect(Collectors.joining(", "));
     * return mapAsString + " /n";
     * }
     */

    public void setCpuUtilizationAverage(double cpuUtilizationAverage) {
        this.cpuUtilizationAverage = cpuUtilizationAverage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DatacenterStateHistoryEntry> getHistory() {
        return history;
    }

    public void setHistory(List<DatacenterStateHistoryEntry> history) {
        this.history = history;
    }
}
