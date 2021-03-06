/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.experiment;

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

import org.autocs.core.model.Scenario;
import org.autocs.core.resolver.ScenarioResolver;

public class AutoCSExperiment extends Experiment {

    private final ScenarioResolver scenarioResolver;

    public AutoCSExperiment(int index, ExperimentRunner<AutoCSExperiment> runner, final Scenario scenario) {
        super(index, runner);
        scenarioResolver = new ScenarioResolver(this.getSimulation(), scenario);
        this.setDatacentersNumber(scenarioResolver.getDatacenters().size());
        this.setBrokersNumber(scenarioResolver.getBrokers().size());
        setVmsByBrokerFunction(broker -> scenarioResolver.getVmsNumberPerBroker(broker));
    }

    @Override
    protected Datacenter createDatacenter(final int index) {
        return scenarioResolver.getDatacenters().get(index);
    }

    @Override
    protected void createBrokers() {
        if (this.getBrokersNumber() <= 0) {
            throw new IllegalStateException("The number of brokers to create was not set");
        }
        for (int i = 0; i < this.getBrokersNumber(); i++) {
            this.getBrokerList().add(scenarioResolver.getBrokers().get(i));
        }
    }

    @Override
    protected List<Vm> createVms(final DatacenterBroker broker) {
        return scenarioResolver.getVmsListPerBroker(broker);
    }

    @Override
    protected List<Cloudlet> createCloudlets(DatacenterBroker broker) {
        return scenarioResolver.getCloudletsListPerBroker(broker);
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
}
