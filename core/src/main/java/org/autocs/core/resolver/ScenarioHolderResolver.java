/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.resolver;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;

import org.autocs.core.model.Scenario;
import org.autocs.core.testbeds.ScenarioHolder;

/**
 * scenario resolver class.
 * It convert presentation of experiment scenario using {@link Scenario}
 * to {@link ScenarioHolder}
 * object
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class ScenarioHolderResolver {

    private CloudSim simulation;
    private DatacenterResolver datacenterResolver;
    private DatacenterBrokerResolver datacenterBrokerResolver;

    public ScenarioHolderResolver(CloudSim simulation) {
        this.simulation = simulation;
        this.datacenterResolver = new DatacenterResolver(this.simulation);
        this.datacenterBrokerResolver = new DatacenterBrokerResolver(this.simulation);
    }

    public ScenarioHolder resolve(Scenario scenarioEntity) {
        ScenarioHolder scenarioHolder = new ScenarioHolder();
        // get datacenters
        this.getDatacenters(scenarioEntity, scenarioHolder);
        // get brokers
        this.getBrokers(scenarioEntity, scenarioHolder);
        return scenarioHolder;
    }

    private void getDatacenters(Scenario scenarioEntity, ScenarioHolder scenarioHolder) {
        scenarioEntity.getProvider().getDatacenters()
                .forEach(datacenterEntity -> {
                    for (int i = 0; i < datacenterEntity.getAmount(); i++) {
                        Datacenter datacenter = this.datacenterResolver.resolve(datacenterEntity);
                        scenarioHolder.addDatacenter(datacenterEntity.getName(), datacenter);
                    }
                });
    }

    private void getBrokers(Scenario scenarioEntity, ScenarioHolder scenarioHolder) {
        scenarioEntity.getBrokers()
                .forEach(brokerEntity -> {
                    for (int i = 0; i < brokerEntity.getAmount(); i++) {
                        DatacenterBroker broker = this.datacenterBrokerResolver.resolve(brokerEntity);
                        scenarioHolder.addBroker(brokerEntity.getName(), broker);
                        // get vms
                        scenarioHolder.addVmsListToBroker(broker,
                                this.datacenterBrokerResolver.getVmsList(brokerEntity));
                        // get cloudlets
                        scenarioHolder.addCloudletsListToBroker(broker,
                                this.datacenterBrokerResolver.getCloudletsList(brokerEntity));
                    }
                });
    }
}
