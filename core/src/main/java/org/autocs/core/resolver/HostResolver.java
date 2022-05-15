/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.resolver;

import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;

import java.util.ArrayList;
import java.util.List;

import org.autocs.core.model.Entity;
import org.autocs.core.loader.PolicyLoader;

/**
 * host resolver class.
 * It convert presentation of host using {@link Entity} to {@link Host}
 * object
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class HostResolver {

    public Host resolve(Entity hostEntity) {
        // create processing elements list
        final List<Pe> peList = getPeList(hostEntity);

        HostSimple host = new HostSimple(
                (int) hostEntity.getProperties().get("ram"),
                (int) hostEntity.getProperties().get("bw"),
                (int) hostEntity.getProperties().get("storage"),
                peList);
        host
                .setRamProvisioner(
                        PolicyLoader.newResourceProvisioner((String) hostEntity.getProperties().get("ramProvisioner")))
                .setBwProvisioner(
                        PolicyLoader.newResourceProvisioner((String) hostEntity.getProperties().get("bwProvisioner")))
                .setVmScheduler(
                        PolicyLoader.vmScheduler((String) hostEntity.getProperties().get("vmScheduler")));
        return host;
    }

    private List<Pe> getPeList(Entity hostEntity) {
        final List<Pe> peList = new ArrayList<>();
        for (int i = 0; i < (int) hostEntity.getProperties().get("pes"); i++) {
            peList.add(new PeSimple((int) hostEntity.getProperties().get("mips"),
                    PolicyLoader.newPeProvisioner((String) hostEntity.getProperties().get("peProvisioner"))));
        }
        return peList;
    }
}
