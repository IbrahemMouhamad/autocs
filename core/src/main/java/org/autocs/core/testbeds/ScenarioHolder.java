/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.testbeds;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.autocs.core.deserializer.ScenarioHolderDeserializer;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.vms.Vm;

/**
 * A java class to hold an experiment scenario
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class ScenarioHolder {
    private Map<String, Datacenter> datacenters;
    private Map<String, DatacenterBroker> brokers;
    private Map<DatacenterBroker, List<Vm>> vmsToBrokerMap;
    private Map<DatacenterBroker, List<Cloudlet>> cloudletsToBrokerMap;

    public ScenarioHolder() {
        this.datacenters = new HashMap<>();
        this.brokers = new HashMap<>();
        this.vmsToBrokerMap = new HashMap<>();
        this.cloudletsToBrokerMap = new HashMap<>();
    }

    public static ScenarioHolder read(File inputFile, CloudSim simulation) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        ScenarioHolderDeserializer scenarioDeserializer = new ScenarioHolderDeserializer(simulation);
        return scenarioDeserializer.convert(mapper.readTree(inputFile), context);
    }

    public void addDatacenter(final String name, final Datacenter datacenter) {
        this.datacenters.put(name, datacenter);
    }

    public void addBroker(final String name, final DatacenterBroker broker) {
        this.brokers.put(name, broker);
    }

    public void addVmsListToBroker(final DatacenterBroker broker, final List<Vm> vmsList) {
        this.vmsToBrokerMap.put(broker, vmsList);
    }

    public void addCloudletsListToBroker(final DatacenterBroker broker, final List<Cloudlet> cloudletsList) {
        this.cloudletsToBrokerMap.put(broker, cloudletsList);
    }

    public List<Datacenter> getDatacenterList() {
        return this.datacenters.values().stream().toList();
    }

    public List<DatacenterBroker> getBrokerList() {
        return this.brokers.values().stream().toList();
    }

    public List<Vm> getVmsListPerBroker(DatacenterBroker broker) {
        return this.vmsToBrokerMap.get(broker);
    }

    public int getVmsNumberPerBroker(DatacenterBroker broker) {
        return this.vmsToBrokerMap.get(broker).size();
    }

    public List<Cloudlet> getCloudletsListPerBroker(DatacenterBroker broker) {
        return this.cloudletsToBrokerMap.get(broker);
    }

    public Map<String, DatacenterBroker> getBrokers() {
        return brokers;
    }

    public Map<String, Datacenter> getDatacenters() {
        return datacenters;
    }
}
