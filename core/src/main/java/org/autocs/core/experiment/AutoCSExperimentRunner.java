/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.experiment;

import java.util.List;

import org.cloudsimplus.testbeds.ExperimentRunner;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.autocs.core.metrics.Metrics;
import org.autocs.core.model.ScenarioRun;

public class AutoCSExperimentRunner extends ExperimentRunner<AutoCSExperiment> {

    private final ScenarioRun scenarioRun;

    public AutoCSExperimentRunner(final long baseSeed, final ScenarioRun scenarioRun) {
        super(baseSeed, (int) scenarioRun.getProperties().get("numberOfRuns"));
        this.scenarioRun = scenarioRun;
    }

    @Override
    protected AutoCSExperiment createExperimentInternal(int index) {
        AutoCSExperiment exp = new AutoCSExperiment(index, this, scenarioRun.getScenario());
        exp.setAfterExperimentFinish(this::afterExperimentFinish);
        return exp;
    }

    @Override
    public void run() {
        super.run();
        this.scenarioRun.setIsRunning(false);
    }

    /**
     * Method automatically called after every experiment finishes running. It
     * performs some post-processing such as collection of data for statistic
     * analysis.
     *
     * @param exp the finished experiment
     */
    @SuppressWarnings("unchecked")
    protected void afterExperimentFinish(AutoCSExperiment exp) {
        this.scenarioRun.setMetrics(new Metrics((List<DatacenterSimple>) (List<?>) exp.getDatacenterList()));
    }

    @Override
    protected void printSimulationParameters() {
        System.out.printf("Executing %d experiments. Please wait ... It may take a while.%n", getSimulationRuns());
    }
}
