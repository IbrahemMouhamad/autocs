// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

package org.autocs.core.testbeds;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudsimplus.testbeds.ConfidenceInterval;

public class Metrics {

    private Map<String, Map<String, Double>> metricsPerBrokers;
    private Map<String, Map<String, Double>> metricsPerDatacenters;

    public Metrics() {
    }

    public Metrics(ScenarioHolder scenarioHolder, List<String> enabledMetrics) {
        this.metricsPerBrokers = new LinkedHashMap<>();
        this.metricsPerDatacenters = new LinkedHashMap<>();
        collectMetrics(enabledMetrics, scenarioHolder);
    }

    public Metrics(List<ConfidenceInterval> confidenceIntervals) {
        this.metricsPerBrokers = this.getMetricsPerBrokers(confidenceIntervals);
        this.metricsPerDatacenters = this.getMetricsPerDatacenters(confidenceIntervals);
    }

    private void collectMetrics(List<String> enabledMetrics, ScenarioHolder scenarioHolder) {
        enabledMetrics.forEach(enabledMetric -> {
            switch (enabledMetric) {
                case "Submitted Cloudlets":
                    this.getTotalSubmittedCloudletsCount(scenarioHolder.getBrokers());
                    break;
                case "Created Cloudlets":
                    this.getTotalCreatedCloudletsCount(scenarioHolder.getBrokers());
                    break;
                case "Waiting Cloudlets":
                    this.getTotalWaitingCloudletsCount(scenarioHolder.getBrokers());
                    break;
                case "Finished Cloudlets":
                    this.getTotalFinishedCloudletsCount(scenarioHolder.getBrokers());
                    break;
                case "Created Vms":
                    this.getTotalCreatedVMsCount(scenarioHolder.getBrokers());
                    break;
                case "Failed Vms":
                    this.getTotalFailedVMsCount(scenarioHolder.getBrokers());
                    break;
                case "Waiting Vms":
                    this.getTotalWaitingVMsCount(scenarioHolder.getBrokers());
                    break;
                case "Processed Load":
                    this.getTotalProcessedLoadLength(scenarioHolder.getBrokers());
                    break;
                case "Wait Time":
                    this.getWaitTimeAverage(scenarioHolder.getBrokers());
                    break;
                case "Completion Time":
                    this.getCompletionTimeAverage(scenarioHolder.getBrokers());
                    break;
                case "Host Cpu Utilization":
                    this.getHostCPUUtilizationAverage(scenarioHolder.getDatacenters());
                    break;
                case "Vm Cpu Utilization":
                    this.getVmCPUUtilizationAverage(scenarioHolder.getBrokers());
                    break;
                case "Power Consumption":
                    this.getPowerConsumption(scenarioHolder.getDatacenters());
                    break;
                case "Active Hosts Number":
                    this.getActiveHostsNumber(scenarioHolder.getDatacenters());
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * Get the number of submitted Cloudlets of a broker
     *
     * @return the number of submitted Cloudlets
     */
    public double getSubmittedCloudletsCount(DatacenterBroker broker) {
        return broker.getCloudletSubmittedList().size();
    }

    /**
     * Add submitted cloudlets metrics
     */
    public void getTotalSubmittedCloudletsCount(Map<String, DatacenterBroker> brokers) {
        final SummaryStatistics totalCount = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        brokers.entrySet().stream()
                .map(entry -> {
                    double value = this.getSubmittedCloudletsCount(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalCount::addValue);
        values.put("Total", totalCount.getSum());
        metricsPerBrokers.put("Submitted Cloudlets", values);
    }

    /**
     * Get the number of created Cloudlets of a broker
     *
     * @return the number of created Cloudlets
     */
    public double getCreatedCloudletsCount(DatacenterBroker broker) {
        return broker.getCloudletCreatedList().size();
    }

    /**
     * Add created cloudlets metrics
     */
    public void getTotalCreatedCloudletsCount(Map<String, DatacenterBroker> brokers) {
        final SummaryStatistics totalCount = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        brokers.entrySet().stream()
                .map(entry -> {
                    double value = this.getCreatedCloudletsCount(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalCount::addValue);
        values.put("Total", totalCount.getSum());
        metricsPerBrokers.put("Created Cloudlets", values);
    }

    /**
     * Get the number of waiting Cloudlets of a broker
     *
     * @return the number of waiting Cloudlets
     */
    public double getWaitingCloudletsCount(DatacenterBroker broker) {
        return broker.getCloudletWaitingList().size();
    }

    /**
     * Add waiting cloudlets metrics
     */
    public void getTotalWaitingCloudletsCount(Map<String, DatacenterBroker> brokers) {
        final SummaryStatistics totalCount = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        brokers.entrySet().stream()
                .map(entry -> {
                    double value = this.getWaitingCloudletsCount(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalCount::addValue);
        values.put("Total", totalCount.getSum());
        metricsPerBrokers.put("Waiting Cloudlets", values);
    }

    /**
     * Gets the number of finished Cloudlets of a broker
     *
     * @return the number of finished Cloudlets
     */
    public double getFinishedCloudletsCount(DatacenterBroker broker) {
        return broker.getCloudletFinishedList().size();
    }

    /**
     * Add finished cloudlets metrics
     */
    public void getTotalFinishedCloudletsCount(Map<String, DatacenterBroker> brokers) {
        final SummaryStatistics totalCount = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        brokers.entrySet().stream()
                .map(entry -> {
                    double value = this.getFinishedCloudletsCount(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalCount::addValue);
        values.put("Total", totalCount.getSum());
        metricsPerBrokers.put("Finished Cloudlets", values);
    }

    /**
     * Get the number of created VMs of a broker
     *
     * @return the number of created VMs
     */
    public double getCreatedVMCount(DatacenterBroker broker) {
        return broker.getVmCreatedList().size();
    }

    /**
     * Add created VMs metrics
     */
    public void getTotalCreatedVMsCount(Map<String, DatacenterBroker> brokers) {
        final SummaryStatistics totalCount = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        brokers.entrySet().stream()
                .map(entry -> {
                    double value = this.getCreatedVMCount(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalCount::addValue);
        values.put("Total", totalCount.getSum());
        metricsPerBrokers.put("Created VMs", values);
    }

    /**
     * Get the number of failed VMs of a broker
     *
     * @return the number of failed VMs
     */
    public double getFailedVMCount(DatacenterBroker broker) {
        return broker.getVmFailedList().size();
    }

    /**
     * Add failed VMs metrics
     */
    public void getTotalFailedVMsCount(Map<String, DatacenterBroker> brokers) {
        final SummaryStatistics totalCount = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        brokers.entrySet().stream()
                .map(entry -> {
                    double value = this.getFailedVMCount(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalCount::addValue);
        values.put("Total", totalCount.getSum());
        metricsPerBrokers.put("Failed VMs", values);
    }

    /**
     * Get the number of waiting VMs of a broker
     *
     * @return the number of waiting VMs
     */
    public double getWaitingVMCount(DatacenterBroker broker) {
        return broker.getVmWaitingList().size();
    }

    /**
     * Add waiting VMs metrics
     */
    public void getTotalWaitingVMsCount(Map<String, DatacenterBroker> brokers) {
        final SummaryStatistics totalCount = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        brokers.entrySet().stream()
                .map(entry -> {
                    double value = this.getWaitingVMCount(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalCount::addValue);
        values.put("Total", totalCount.getSum());
        metricsPerBrokers.put("Waiting VMs", values);
    }

    /**
     * Gets the total amount of finished load of a broker (in MIPS)
     * which is equal the sum of finished cloudlets length
     */
    public double getProcessedLoadLength(DatacenterBroker broker) {
        final SummaryStatistics processedLoadLength = new SummaryStatistics();
        broker.getCloudletFinishedList().stream()
                .map(cloudlet -> cloudlet.getLength())
                .forEach(processedLoadLength::addValue);

        return processedLoadLength.getSum();
    }

    /**
     * Add processed load metrics
     */
    public void getTotalProcessedLoadLength(Map<String, DatacenterBroker> brokers) {
        final SummaryStatistics totalCount = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        brokers.entrySet().stream()
                .map(entry -> {
                    double value = this.getProcessedLoadLength(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalCount::addValue);
        values.put("Total", totalCount.getSum());
        metricsPerBrokers.put("Processed Load", values);
    }

    /**
     * Gets the average wait time of cloudlets of a broker
     *
     * @return the wait time average
     */
    public double getWaitTimeAveragePerBroker(DatacenterBroker broker) {
        return broker.getCloudletFinishedList().stream().mapToDouble(Cloudlet::getWaitingTime).average()
                .orElse(0);
    }

    /**
     * Add wait time metrics
     */
    public void getWaitTimeAverage(Map<String, DatacenterBroker> brokers) {
        final SummaryStatistics totalAverage = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        brokers.entrySet().stream()
                .map(entry -> {
                    double value = this.getWaitTimeAveragePerBroker(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalAverage::addValue);
        values.put("Total", totalAverage.getMean());
        metricsPerBrokers.put("Wait Time", values);
    }

    /**
     * Computes the task Completion Time average for all finished Cloudlets for a
     * broker
     *
     * @return the Task Completion Time average
     */
    public double getTaskCompletionTimeAveragePerBroker(DatacenterBroker broker) {
        final SummaryStatistics cloudletTaskCompletionTime = new SummaryStatistics();
        broker.getCloudletFinishedList().stream()
                .map(c -> c.getFinishTime() - c.getLastDatacenterArrivalTime())
                .forEach(cloudletTaskCompletionTime::addValue);

        return cloudletTaskCompletionTime.getMean();
    }

    /**
     * Add completion time metrics
     */
    public void getCompletionTimeAverage(Map<String, DatacenterBroker> brokers) {
        final SummaryStatistics totalAverage = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        brokers.entrySet().stream()
                .map(entry -> {
                    double value = this.getTaskCompletionTimeAveragePerBroker(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalAverage::addValue);
        values.put("Total", totalAverage.getMean());
        metricsPerBrokers.put("Completion Time", values);
    }

    /**
     * Gets CPU utilization mean of all hosts in a Datacenter
     */
    public double getHostCPUUtilizationAveragePerDatacenter(Datacenter datacenter) {
        final SummaryStatistics hostsCpuUtilizationAverage = new SummaryStatistics();
        datacenter.getHostList().stream()
                .filter(host -> host.isActive())
                .map(host -> host.getCpuUtilizationStats().getMean()
                        * 100)
                .forEach(hostsCpuUtilizationAverage::addValue);

        return hostsCpuUtilizationAverage.getMean();
    }

    /**
     * Add host CPU utilization metrics
     */
    public void getHostCPUUtilizationAverage(Map<String, Datacenter> datacenters) {
        final SummaryStatistics totalAverage = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        datacenters.entrySet().stream()
                .map(entry -> {
                    double value = this.getHostCPUUtilizationAveragePerDatacenter(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalAverage::addValue);
        values.put("Total", totalAverage.getMean());
        metricsPerDatacenters.put("Host CPU Utilization", values);
    }

    /**
     * Gets CPU utilization mean of all VMs of a broker
     */
    public double getVmCPUUtilizationAveragePerBroker(DatacenterBroker broker) {
        final SummaryStatistics VMsCpuUtilizationAverage = new SummaryStatistics();
        broker.getVmCreatedList().stream().map(vm -> vm.getCpuUtilizationStats().getMean()
                * 100)
                .forEach(VMsCpuUtilizationAverage::addValue);

        return VMsCpuUtilizationAverage.getMean();
    }

    /**
     * Add vm CPU utilization metrics
     */
    public void getVmCPUUtilizationAverage(Map<String, DatacenterBroker> brokers) {
        final SummaryStatistics totalAverage = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        brokers.entrySet().stream()
                .map(entry -> {
                    double value = this.getVmCPUUtilizationAveragePerBroker(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalAverage::addValue);
        values.put("Total", totalAverage.getMean());
        metricsPerBrokers.put("Vm CPU Utilization", values);
    }

    /**
     * Computes the total power consumption of all hosts in a Datacenter
     *
     * @return the power consumption of all hosts
     */
    public double getPowerConsumptionPerDatacenter(Datacenter datacenter) {
        final SummaryStatistics hostsPowerConsumptionTotal = new SummaryStatistics();
        datacenter.getHostList().stream()
                .filter(host -> host.isActive())
                .map(host -> host.getPowerModel().getPower(host.getCpuUtilizationStats().getMean()))
                .forEach(hostsPowerConsumptionTotal::addValue);

        return hostsPowerConsumptionTotal.getSum();
    }

    /**
     * Add power consumption metrics
     */
    public void getPowerConsumption(Map<String, Datacenter> datacenters) {
        final SummaryStatistics totalPower = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        datacenters.entrySet().stream()
                .map(entry -> {
                    double value = this.getPowerConsumptionPerDatacenter(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalPower::addValue);
        values.put("Total", totalPower.getSum());
        metricsPerDatacenters.put("Power Consumption", values);
    }

    /**
     * Gets the number of active hosts in a Datacenter
     *
     * @return the power consumption of all hosts
     */
    public double getActiveHostsNumberPerDatacenter(Datacenter datacenter) {
        final SummaryStatistics hostsNumber = new SummaryStatistics();
        hostsNumber.addValue(
                datacenter.getHostList().stream()
                        .filter(host -> host.isActive())
                        .collect(Collectors.toList()).size());

        return hostsNumber.getSum();
    }

    /**
     * Add active hosts metrics
     */
    public void getActiveHostsNumber(Map<String, Datacenter> datacenters) {
        final SummaryStatistics totalActiveHosts = new SummaryStatistics();
        Map<String, Double> values = new LinkedHashMap<>();
        datacenters.entrySet().stream()
                .map(entry -> {
                    double value = this.getActiveHostsNumberPerDatacenter(entry.getValue());
                    values.put(entry.getKey(), value);
                    return value;
                })
                .forEach(totalActiveHosts::addValue);
        values.put("Total", totalActiveHosts.getSum());
        metricsPerDatacenters.put("Active Hosts Number", values);
    }

    public Map<String, Map<String, Double>> getMetricsPerBrokers() {
        return metricsPerBrokers;
    }

    public Map<String, Map<String, Double>> getMetricsPerDatacenters() {
        return metricsPerDatacenters;
    }

    @JsonIgnore
    public Map<String, Double> getMetricsValue() {
        Map<String, Double> metricsValue = new LinkedHashMap<>();
        metricsPerBrokers.entrySet().stream()
                .forEach(
                        metricEntry -> metricEntry.getValue().entrySet().stream()
                                .forEach(metricsPerBrokerEntry -> metricsValue.put(
                                        "BROKER_" + metricsPerBrokerEntry.getKey() + "_" + metricEntry.getKey(),
                                        metricsPerBrokerEntry.getValue())));
        metricsPerDatacenters.entrySet().stream()
                .forEach(
                        metricEntry -> metricEntry.getValue().entrySet().stream()
                                .forEach(metricsPerDatacenterEntry -> metricsValue.put(
                                        "DATACENTER_" + metricsPerDatacenterEntry.getKey() + "_" + metricEntry.getKey(),
                                        metricsPerDatacenterEntry.getValue())));
        return metricsValue;
    }

    public Map<String, Map<String, Double>> getMetricsPerBrokers(List<ConfidenceInterval> confidenceIntervals) {
        return groupMetricsPerLabel(confidenceIntervals, "BROKER");
    }

    public Map<String, Map<String, Double>> getMetricsPerDatacenters(List<ConfidenceInterval> confidenceIntervals) {
        return groupMetricsPerLabel(confidenceIntervals, "DATACENTER");
    }

    public Map<String, Map<String, Double>> groupMetricsPerLabel(List<ConfidenceInterval> confidenceIntervals,
            String LABEL) {
        Map<String, Map<String, Double>> metricsPerLabel = new LinkedHashMap<>();
        confidenceIntervals.stream()
                .forEach(confidenceInterval -> {
                    final String fullName = confidenceInterval.getMetricName();
                    String[] parts = fullName.split("_");
                    if (parts[0].equals(LABEL)) {
                        final String metricName = parts[2];
                        if (!metricsPerLabel.containsKey(metricName)) {
                            metricsPerLabel.put(metricName, new LinkedHashMap<>());
                        }
                        metricsPerLabel.get(metricName).put(parts[1], confidenceInterval.getValue());
                    }
                });
        return metricsPerLabel;
    }

    public void setMetricsPerBrokers(Map<String, Map<String, Double>> metricsPerBrokers) {
        this.metricsPerBrokers = metricsPerBrokers;
    }

    public void setMetricsPerDatacenters(Map<String, Map<String, Double>> metricsPerDatacenters) {
        this.metricsPerDatacenters = metricsPerDatacenters;
    }
}
