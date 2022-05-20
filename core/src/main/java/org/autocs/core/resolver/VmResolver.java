/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.resolver;

import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;

import org.autocs.core.model.Entity;
import org.autocs.core.loader.PolicyLoader;

/**
 * vm resolver class.
 * It convert presentation of vm using {@link Entity} to {@link Vm}
 * object
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class VmResolver {

    public Vm resolve(Entity vmEntity) {
        VmSimple vm = new VmSimple(
                (int) vmEntity.getProperties().get("mips"),
                (int) vmEntity.getProperties().get("pes"));
        vm
                .setRam((int) vmEntity.getProperties().get("ram"))
                .setBw((int) vmEntity.getProperties().get("bw"))
                .setCloudletScheduler(
                        PolicyLoader
                                .cloudletScheduler((String) vmEntity.getProperties().get("cloudletScheduler")));
        vm.enableUtilizationStats();
        return vm;
    }
}
