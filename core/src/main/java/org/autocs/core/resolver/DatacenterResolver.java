/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.resolver;

import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;

import java.util.ArrayList;
import java.util.List;

import org.autocs.core.loader.PolicyLoader;

/**
 * datacenter resolver class.
 * It convert presentation of datacenter using
 * {@link org.autocs.backend.model.Datacenter}
 * to {@link Datacenter}
 * object
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class DatacenterResolver {

    private Simulation simulation;
    private HostResolver hostResolver;

    protected DatacenterResolver(Simulation simulation) {
        this.simulation = simulation;
        this.hostResolver = new HostResolver();
    }

    public Datacenter resolve(org.autocs.core.model.Datacenter datacenterEntity) {
        // resolve hosts
        final List<Host> hostList = new ArrayList<>();
        datacenterEntity.getHosts()
                .forEach(hostEntity -> {
                    for (int i = 0; i < hostEntity.getAmount(); i++) {
                        Host host = hostResolver.resolve(hostEntity);
                        hostList.add(host);
                    }
                });
        // datacenter creation
        Datacenter datacenter = new DatacenterSimple(
                this.simulation,
                hostList,
                PolicyLoader.vmAllocationPolicy((String) datacenterEntity.getProperties().get("vmAllocationPolicy")));
        datacenter
                .setSchedulingInterval((int) datacenterEntity.getProperties().get("schedulingInterval"));
        return datacenter;
    }
}
