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

import org.cloudbus.cloudsim.datacenters.DatacenterSimple;

/**
 * Collects overall metrics of different entities
 *
 * @author Ibrahem Mouhamad
 * @since Simulator Package 1.0.0
 */

public class Metrics {
    private double datacentersCpuUtilizationAverage;
    private List<DatacenterMetrics> datacentersMetrics;

    public Metrics() {
    }

    public Metrics(List<DatacenterSimple> datacenters) {
        // collect metrics for all datacenters
        this.datacentersMetrics = datacenters.stream()
                .map(datacenter -> new DatacenterMetrics(datacenter)).toList();
        this.setDatacentersCpuUtilizationAverage();
    }

    /*
     * Gets average cpu utilization across all datacenters
     */
    private void setDatacentersCpuUtilizationAverage() {
        this.datacentersCpuUtilizationAverage = this.datacentersMetrics.stream()
                .mapToDouble(datacenterMetric -> datacenterMetric.getCpuUtilizationAverage())
                .average()
                .orElse(0);
    }

    public double getDatacentersCpuUtilizationAverage() {
        return datacentersCpuUtilizationAverage;
    }

    /*
     * public Map<String, Double> getMetrics() {
     * Map<String, Double> metrics = new HashMap<>();
     * // overall metrics
     * metrics.put("Hosts Cpu Utilization", this.datacentersCpuUtilizationAverage);
     * // datacenter-specific metrics
     * datacenterMetrics.stream()
     * .map(datacenterMetric -> datacenterMetric.getMetrics())
     * .forEach(datacenterMetricMap -> datacenterMetricMap.entrySet()
     * .forEach(entry -> metrics.put(entry.getKey(), entry.getValue())));
     * return metrics;
     * }
     */

    public List<DatacenterMetrics> getDatacentersMetrics() {
        return datacentersMetrics;
    }

    public void setDatacentersMetrics(List<DatacenterMetrics> datacentersMetrics) {
        this.datacentersMetrics = datacentersMetrics;
    }
}
