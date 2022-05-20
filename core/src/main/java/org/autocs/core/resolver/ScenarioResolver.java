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
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.autocs.core.model.Scenario;

/**
 * scenario resolver class.
 * It convert presentation of experiment scenario using {@link Scenario}
 * to {@link ScenarioHolder}
 * object
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class ScenarioResolver {

    private CloudSim simulation;
    private DatacenterResolver datacenterResolver;
    private List<Datacenter> datacenters;
    private DatacenterBrokerResolver datacenterBrokerResolver;
    private List<DatacenterBroker> brokers;
    private Map<DatacenterBroker, List<Vm>> vmsToBrokerMap;
    private Map<DatacenterBroker, List<Cloudlet>> cloudletsToBrokerMap;

    public ScenarioResolver(CloudSim simulation, Scenario scenario) {
        this.simulation = simulation;
        this.datacenterResolver = new DatacenterResolver(this.simulation);
        this.datacenterBrokerResolver = new DatacenterBrokerResolver(this.simulation);
        this.resolve(scenario);
    }

    private void resolve(Scenario scenarioEntity) {
        // get datacenters
        this.getDatacenters(scenarioEntity);
        // get brokers
        this.getBrokers(scenarioEntity);
    }

    private void getDatacenters(Scenario scenarioEntity) {
        datacenters = new ArrayList<>();
        scenarioEntity.getProvider().getDatacenters()
                .forEach(datacenterEntity -> {
                    final String name = datacenterEntity.getName();
                    for (int i = 0; i < datacenterEntity.getAmount(); i++) {
                        datacenterEntity.setName(name + "_" + i);
                        Datacenter datacenter = this.datacenterResolver.resolve(datacenterEntity);
                        datacenters.add(datacenter);
                    }
                });
    }

    private void getBrokers(Scenario scenarioEntity) {
        brokers = new ArrayList<>();
        vmsToBrokerMap = new HashMap<>();
        cloudletsToBrokerMap = new HashMap<>();
        scenarioEntity.getBrokers()
                .forEach(brokerEntity -> {
                    final String name = brokerEntity.getName();
                    for (int i = 0; i < brokerEntity.getAmount(); i++) {
                        brokerEntity.setName(name + "_" + i);
                        DatacenterBroker broker = this.datacenterBrokerResolver.resolve(brokerEntity);
                        brokers.add(broker);
                        // get vms
                        vmsToBrokerMap.put(broker,
                                this.datacenterBrokerResolver.getVmsList(brokerEntity));
                        // get cloudlets
                        cloudletsToBrokerMap.put(broker,
                                this.datacenterBrokerResolver.getCloudletsList(brokerEntity));
                    }
                });
    }

    public List<Datacenter> getDatacenters() {
        return datacenters;
    }

    public List<DatacenterBroker> getBrokers() {
        return brokers;
    }

    public Map<DatacenterBroker, List<Vm>> getVmsToBrokerMap() {
        return vmsToBrokerMap;
    }

    public Map<DatacenterBroker, List<Cloudlet>> getCloudletsToBrokerMap() {
        return cloudletsToBrokerMap;
    }

    public int getVmsNumberPerBroker(DatacenterBroker broker) {
        return vmsToBrokerMap.get(broker).size();
    }

    public List<Vm> getVmsListPerBroker(DatacenterBroker broker) {
        return this.vmsToBrokerMap.get(broker);
    }

    public List<Cloudlet> getCloudletsListPerBroker(DatacenterBroker broker) {
        return this.cloudletsToBrokerMap.get(broker);
    }

}
