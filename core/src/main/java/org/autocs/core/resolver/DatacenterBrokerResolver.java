/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.resolver;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import org.autocs.core.model.BrokerModel;

/**
 * broker resolver class.
 * It convert presentation of broker using {@link BrokerModel}
 * to {@link DatacenterBroker}
 * object
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class DatacenterBrokerResolver {

    private CloudSim simulation;
    private VmResolver vmResolver;
    private CloudletResolver cloudletResolver;

    protected DatacenterBrokerResolver(CloudSim simulation) {
        this.simulation = simulation;
        this.vmResolver = new VmResolver();
        this.cloudletResolver = new CloudletResolver();
    }

    public DatacenterBroker resolve(BrokerModel brokerEntity) {
        // datacenter broker creation
        DatacenterBroker broker = new DatacenterBrokerSimple(this.simulation, brokerEntity.getName());
        return broker;
    }

    public List<Vm> getVmsList(BrokerModel brokerEntity) {
        final List<Vm> vmList = new ArrayList<>();
        brokerEntity.getVms()
                .forEach(vmEntity -> {
                    for (int i = 0; i < vmEntity.getAmount(); i++) {
                        Vm vm = vmResolver.resolve(vmEntity);
                        vmList.add(vm);
                    }
                });
        return vmList;
    }

    public List<Cloudlet> getCloudletsList(BrokerModel brokerEntity) {
        final List<Cloudlet> cloudletList = new ArrayList<>();
        brokerEntity.getCloudlets()
                .forEach(cloudletEntity -> {
                    for (int i = 0; i < cloudletEntity.getAmount(); i++) {
                        Cloudlet vm = cloudletResolver.resolve(cloudletEntity);
                        cloudletList.add(vm);
                    }
                });
        return cloudletList;
    }

}
