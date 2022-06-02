// Copyright 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

package org.autocs.sdn.examples.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.sql.Timestamp;

import org.cloudsimplus.testbeds.ConfidenceInterval;
import org.cloudsimplus.testbeds.Experiment;
import org.cloudsimplus.testbeds.ExperimentRunner;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.joining;

public class BaseExperimentRunner<T extends BaseExperiment> extends ExperimentRunner<Experiment> {
    /**
     * Indicates if each experiment will output execution logs or not.
     */
    protected final boolean experimentVerbose = false;

    /**
     * List of metric names.
     */
    private static final String[] METRIC_NAMES = {
            "Finished Cloudlets", "Created VMs",
            "Wait Time", "Completion Time", "Host CPU Utilization",
            "Hosts Number" };

    /**
     * Different numbers of dynamically created cloudlets.
     */
    protected static final int[] DYNAMIC_CLOUDLETS = { 1000 };

    /**
     * Current numbers of dynamically created cloudlets.
     */
    protected int dynamicCloudlet = 500;

    /**
     * Starts the execution of the experiments the number of times defines in
     * {@link #getSimulationRuns()}.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        String outputFileName = "BaseExperiment-VmAllocationPolicyBestFit-"
                + new Timestamp(System.currentTimeMillis()).getTime()
                + ".csv";
        for (Integer number : DYNAMIC_CLOUDLETS) {
            BaseExperimentRunner<BaseExperiment> runner = new BaseExperimentRunner<BaseExperiment>(9075098589732L);
            runner.dynamicCloudlet = number;
            runner.setVerbose(false).run();
            runner.computeAndExportResultsToCSV(outputFileName);
        }
    }

    protected BaseExperimentRunner(final long baseSeed) {
        super(baseSeed, 1);
    }

    @Override
    protected BaseExperiment createExperimentInternal(int index) {
        BaseExperiment exp = new BaseExperiment(index, this, 9075098589732L);
        exp.setDynamicCloudlets(this.dynamicCloudlet);
        exp.setAfterExperimentFinish(this::afterExperimentFinish).setVerbose(experimentVerbose);
        return exp;
    }

    /**
     * Method automatically called after every experiment finishes running. It
     * performs some post-processing such as collection of data for statistic
     * analysis.
     *
     * @param exp the finished experiment
     */
    protected void afterExperimentFinish(BaseExperiment exp) {
        addMetricValue("Hosts Number", exp.getActiveHostsNumber());
        addMetricValue("Finished Cloudlets", exp.getFinishedCloudletsCount());
        addMetricValue("Created VMs", exp.getCreatedVMCount());
        addMetricValue("Wait Time", exp.getWaitTimeAverage());
        addMetricValue("Completion Time", exp.getTaskCompletionTimeAverage());
        addMetricValue("Host CPU Utilization", exp.getCpuUtilizationForAllHosts());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            objectMapper.writeValue(new File("datacenter.json"), exp.getDatacenterList().get(0).getStateHistory());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void printSimulationParameters() {
        System.out.printf("Executing %d experiments. Please wait ... It may take a while.%n", getSimulationRuns());
        System.out.println("Experiments configurations:");
        // System.out.printf("\tBase seed: %d | Number of VMs: %d | Number of Cloudlets:
        // %d%n", getBaseSeed(), VMS,
        // CLOUDLETS);
        System.out.printf("\tApply Antithetic Variates Technique: %b%n", isApplyAntitheticVariates());
        if (isApplyBatchMeansMethod()) {
            System.out.println("\tApply Batch Means Method to reduce simulation results correlation: true");
            System.out.printf("\tNumber of Batches for Batch Means Method: %d", getBatchesNumber());
            System.out.printf("\tBatch Size: %d%n", batchSizeCeil());
        }
        System.out.printf("%nSimulated Annealing Parameters%n");

    }

    protected void computeAndExportResultsToCSV(String fileName) {
        File csvOutputFile = new File(fileName);
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(csvOutputFile, true))) {
            // get metrics
            final Map<String, List<Double>> metrics = new TreeMap<>();
            for (String metricName : METRIC_NAMES) {
                final List<Double> metricValues = getMetricValues(metricName);
                metrics.put(metricName, metricValues);
            }
            // compute final values
            final List<ConfidenceInterval> confidenceIntervals = metrics.entrySet()
                    .stream()
                    .map(this::computeFinalResults)
                    .collect(toCollection(() -> new ArrayList<>(metrics.size())));
            if (csvOutputFile.length() == 0) {
                final String cols = confidenceIntervals.stream().map(ConfidenceInterval::getMetricName)
                        .collect(joining(", "));
                pw.println("N, " + cols);
            }
            final String format = "%.2f";
            final String values = confidenceIntervals
                    .stream()
                    .map(ci -> String.format(format, ci.getValue(), ci.getMetricName().length()))
                    .collect(joining(", "));
            pw.println(this.dynamicCloudlet + ", " + values);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
