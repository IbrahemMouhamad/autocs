// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

package org.autocs.core.testbeds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.cloudsimplus.testbeds.ConfidenceInterval;
import org.cloudsimplus.testbeds.ExperimentRunner;

import static java.util.stream.Collectors.toCollection;

import org.autocs.core.model.RunningExperiment;
import org.autocs.core.model.Scenario;

public class AutoCSExperimentRunner extends ExperimentRunner<AutoCSExperiment> {

    private final Scenario scenarioEntity;
    private final String outputDirectory;
    private List<String> enabledMetricsName;

    /**
     * List of metric names.
     */
    private static final String[] METRIC_NAMES = { "Submitted Cloudlets",
            "Created Cloudlets", "Waiting Cloudlets",
            "Finished Cloudlets", "Created Vms", "Failed Vms",
            "Waiting Vms", "Wait Time", "Completion Time", "Host Cpu Utilization", "Vm Cpu Utilization",
            "Active Hosts Number", "Power Consumption", "Processed Load" };

    public AutoCSExperimentRunner(final long baseSeed, final RunningExperiment runningExperiment) {
        super(baseSeed, (int) runningExperiment.getProperties().get("numberOfRuns"));
        this.scenarioEntity = runningExperiment.getScenario();
        this.outputDirectory = runningExperiment.getOutputPath();
    }

    @Override
    protected AutoCSExperiment createExperimentInternal(int index) {
        AutoCSExperiment exp = new AutoCSExperiment(index, this, scenarioEntity);
        exp.setAfterExperimentFinish(this::afterExperimentFinish);
        return exp;
    }

    @Override
    public void run() {
        // redirect standard out to file.
        // PrintStream OutputFile;
        // OutputFile = new PrintStream(this.outputDirectory + File.separator +
        // "output.log");
        // System.setOut(OutputFile);
        // redirect standard err to file.
        // PrintStream errorFile = new PrintStream(this.outputDirectory + File.separator
        // + "error.log");
        // System.setErr(errorFile);
        super.run();
        try {
            this.computeAndExportMetrics();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method automatically called after every experiment finishes running. It
     * performs some post-processing such as collection of data for statistic
     * analysis.
     *
     * @param exp the finished experiment
     */
    protected void afterExperimentFinish(AutoCSExperiment exp) {
        Metrics metrics = new Metrics(exp.getScenario(), Arrays.asList(METRIC_NAMES));
        Map<String, Double> metricsValue = metrics.getMetricsValue();
        this.enabledMetricsName = new ArrayList<>(metricsValue.keySet());
        metricsValue.entrySet().stream().forEach(entry -> addMetricValue(entry.getKey(), entry.getValue()));
    }

    @Override
    protected void printSimulationParameters() {
        System.out.printf("Executing %d experiments. Please wait ... It may take a while.%n", getSimulationRuns());
    }

    private void computeAndExportMetrics() throws StreamWriteException, DatabindException, IOException {
        // get metrics
        final Map<String, List<Double>> metricsMap = new TreeMap<>();
        this.enabledMetricsName.forEach(metricName -> {
            final List<Double> metricValues = getMetricValues(metricName);
            metricsMap.put(metricName, metricValues);
        });
        // compute final values
        final List<ConfidenceInterval> confidenceIntervals = metricsMap.entrySet()
                .stream()
                .map(this::computeFinalResults)
                .collect(toCollection(() -> new ArrayList<>(metricsMap.size())));

        Metrics metrics = new Metrics(confidenceIntervals);
        // export
        final String metricsFilePath = this.outputDirectory + File.separator + "metrics.json";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(new File(metricsFilePath), metrics);
    }
}
