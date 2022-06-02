// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

package org.autocs.sdn.examples.autoscaling;

import java.io.File;
import java.sql.Timestamp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.autocs.sdn.examples.experiment.BaseExperimentRunner;

public class PowerAwareRuleBasedHorizontalVmScalingExperimentRunner
        extends BaseExperimentRunner<PowerAwareRuleBasedHorizontalVmScalingExperiment> {

    /**
     * Starts the execution of the experiments the number of times defines in
     * {@link #getSimulationRuns()}.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        String outputFileName = "PowerAwareRuleBasedHorizontalVmScaling-VmAllocationPolicyBestFit-"
                + new Timestamp(System.currentTimeMillis()).getTime()
                + ".csv";
        for (Integer number : DYNAMIC_CLOUDLETS) {
            PowerAwareRuleBasedHorizontalVmScalingExperimentRunner runner = new PowerAwareRuleBasedHorizontalVmScalingExperimentRunner(
                    9075098589732L);
            runner.dynamicCloudlet = number;
            runner.setVerbose(false).run();
            runner.computeAndExportResultsToCSV(outputFileName);
        }
    }

    private PowerAwareRuleBasedHorizontalVmScalingExperimentRunner(final long baseSeed) {
        super(baseSeed);
    }

    @Override
    protected PowerAwareRuleBasedHorizontalVmScalingExperiment createExperimentInternal(int index) {
        PowerAwareRuleBasedHorizontalVmScalingExperiment exp = new PowerAwareRuleBasedHorizontalVmScalingExperiment(
                index,
                this,
                9075098589732L);
        exp.setDynamicCloudlets(this.dynamicCloudlet);
        exp.setAfterExperimentFinish(super::afterExperimentFinish).setVerbose(experimentVerbose);
        return exp;
    }
}
