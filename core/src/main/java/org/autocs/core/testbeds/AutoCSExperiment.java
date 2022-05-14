/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.testbeds;

import java.util.Comparator;
import java.util.List;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.testbeds.Experiment;
import org.cloudsimplus.testbeds.ExperimentRunner;

import static java.util.Comparator.comparingDouble;

import java.io.File;
import java.io.IOException;

public class AutoCSExperiment extends Experiment {

    private final ScenarioHolder scenario;

    public AutoCSExperiment(int index, ExperimentRunner<AutoCSExperiment> runner, final File inputFile)
            throws IOException {
        super(index, runner);
        this.scenario = ScenarioHolder.read(inputFile, this.getSimulation());
        this.setDatacentersNumber(scenario.getDatacenterList().size());
        this.setBrokersNumber(scenario.getBrokerList().size());
        setVmsByBrokerFunction(broker -> scenario.getVmsNumberPerBroker(broker));
    }

    @Override
    protected Datacenter createDatacenter(final int index) {
        return scenario.getDatacenterList().get(index);
    }

    @Override
    protected void createBrokers() {
        if (this.getBrokersNumber() <= 0) {
            throw new IllegalStateException("The number of brokers to create was not set");
        }
        for (int i = 0; i < this.getBrokersNumber(); i++) {
            this.getBrokerList().add(scenario.getBrokerList().get(i));
        }
    }

    @Override
    protected List<Vm> createVms(final DatacenterBroker broker) {
        return scenario.getVmsListPerBroker(broker);
    }

    @Override
    protected List<Cloudlet> createCloudlets(DatacenterBroker broker) {
        return scenario.getCloudletsListPerBroker(broker);
    }

    @Override
    public void printResults() {
        for (int id = 0; id < getBrokerList().size(); id++) {
            this.printBrokerFinishedCloudlets(getBrokerList().get(id));
        }
    }

    protected void printBrokerFinishedCloudlets(final DatacenterBroker broker) {
        final List<Cloudlet> finishedCloudlets = broker.getCloudletFinishedList();
        final Comparator<Cloudlet> sortByVmId = comparingDouble(c -> c.getVm().getId());
        final Comparator<Cloudlet> sortByStartTime = comparingDouble(Cloudlet::getExecStartTime);
        finishedCloudlets.sort(sortByVmId.thenComparing(sortByStartTime));

        new CloudletsTableBuilder(finishedCloudlets).build();
    }

    @Override
    protected Cloudlet createCloudlet(DatacenterBroker broker) {
        return null;
    }

    @Override
    protected Vm createVm(DatacenterBroker broker, int id) {
        return null;
    }

    @Override
    protected Host createHost(int id) {
        return null;
    }

    @Override
    protected DatacenterBroker createBroker() {
        return null;
    }

    public ScenarioHolder getScenario() {
        return scenario;
    }
}
