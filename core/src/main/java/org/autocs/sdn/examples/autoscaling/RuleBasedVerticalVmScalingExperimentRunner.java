// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

package org.autocs.sdn.examples.autoscaling;

import java.sql.Timestamp;

import org.autocs.sdn.examples.experiment.BaseExperimentRunner;

public class RuleBasedVerticalVmScalingExperimentRunner
        extends BaseExperimentRunner<RuleBasedVerticalVmScalingExperiment> {

    /**
     * Starts the execution of the experiments the number of times defines in
     * {@link #getSimulationRuns()}.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        String outputFileName = "RuleBasedVerticalVmScaling-VmAllocationPolicyBestFit-"
                + new Timestamp(System.currentTimeMillis()).getTime()
                + ".csv";
        for (Integer number : DYNAMIC_CLOUDLETS) {
            RuleBasedVerticalVmScalingExperimentRunner runner = new RuleBasedVerticalVmScalingExperimentRunner(
                    9075098589732L);
            runner.dynamicCloudlet = number;
            runner.setVerbose(false).run();
            runner.computeAndExportResultsToCSV(outputFileName);
        }
    }

    private RuleBasedVerticalVmScalingExperimentRunner(final long baseSeed) {
        super(baseSeed);
    }

    @Override
    protected RuleBasedVerticalVmScalingExperiment createExperimentInternal(int index) {
        RuleBasedVerticalVmScalingExperiment exp = new RuleBasedVerticalVmScalingExperiment(index,
                this,
                9075098589732L);
        exp.setDynamicCloudlets(this.dynamicCloudlet);
        exp.setAfterExperimentFinish(super::afterExperimentFinish).setVerbose(experimentVerbose);
        return exp;
    }
}
