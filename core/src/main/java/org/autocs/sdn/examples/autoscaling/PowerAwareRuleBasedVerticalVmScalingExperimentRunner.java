// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

package org.autocs.sdn.examples.autoscaling;

import java.sql.Timestamp;

import org.autocs.sdn.examples.experiment.BaseExperimentRunner;

public class PowerAwareRuleBasedVerticalVmScalingExperimentRunner
        extends BaseExperimentRunner<PowerAwareRuleBasedVerticalVmScalingExperiment> {

    /**
     * Starts the execution of the experiments the number of times defines in
     * {@link #getSimulationRuns()}.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        String outputFileName = "PowerAwareRuleBasedVerticalVmScaling-VmAllocationPolicyBestFit-"
                + new Timestamp(System.currentTimeMillis()).getTime()
                + ".csv";
        for (Integer number : DYNAMIC_CLOUDLETS) {
            PowerAwareRuleBasedVerticalVmScalingExperimentRunner runner = new PowerAwareRuleBasedVerticalVmScalingExperimentRunner(
                    9075098589732L);
            runner.dynamicCloudlet = number;
            runner.setVerbose(false).run();
            runner.computeAndExportResultsToCSV(outputFileName);
        }
    }

    private PowerAwareRuleBasedVerticalVmScalingExperimentRunner(final long baseSeed) {
        super(baseSeed);
    }

    @Override
    protected PowerAwareRuleBasedVerticalVmScalingExperiment createExperimentInternal(int index) {
        PowerAwareRuleBasedVerticalVmScalingExperiment exp = new PowerAwareRuleBasedVerticalVmScalingExperiment(
                index,
                this,
                9075098589732L);
        exp.setDynamicCloudlets(this.dynamicCloudlet);
        exp.setAfterExperimentFinish(super::afterExperimentFinish).setVerbose(experimentVerbose);
        return exp;
    }
}
