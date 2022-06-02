// Copyright 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

package org.autocs.sdn.examples.experiment;

import java.sql.Timestamp;

public class BasicPowerAwareExperimentRunner extends BaseExperimentRunner<BasicPowerAwareExperiment> {

    /**
     * Starts the execution of the experiments the number of times defines in
     * {@link #getSimulationRuns()}.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        String outputFileName = "BasicPowerAwareExperiment-VmAllocationPolicyBestFit-"
                + new Timestamp(System.currentTimeMillis()).getTime()
                + ".csv";
        for (Integer number : DYNAMIC_CLOUDLETS) {
            BasicPowerAwareExperimentRunner runner = new BasicPowerAwareExperimentRunner(9075098589732L);
            runner.dynamicCloudlet = number;
            runner.setVerbose(false).run();
            runner.computeAndExportResultsToCSV(outputFileName);
        }
    }

    private BasicPowerAwareExperimentRunner(final long baseSeed) {
        super(baseSeed);
    }

    @Override
    protected BasicPowerAwareExperiment createExperimentInternal(int index) {
        BasicPowerAwareExperiment exp = new BasicPowerAwareExperiment(index, this, 9075098589732L);
        exp.setDynamicCloudlets(this.dynamicCloudlet);
        exp.setAfterExperimentFinish(super::afterExperimentFinish).setVerbose(experimentVerbose);
        return exp;
    }
}
