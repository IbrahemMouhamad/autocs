/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.resolver;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.autocs.core.model.Entity;
import org.autocs.core.loader.PolicyLoader;

/**
 * cloudlet resolver class.
 * It convert presentation of cloudlet using {@link Entity} to {@link Cloudlet}
 * object
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class CloudletResolver {

    public Cloudlet resolve(Entity cloudletEntity) {
        CloudletSimple cloudlet = new CloudletSimple(
                (int) cloudletEntity.getProperties().get("length"),
                (int) cloudletEntity.getProperties().get("pes"));
        cloudlet
                .setFileSize((int) cloudletEntity.getProperties().get("fileSize"))
                .setOutputSize((int) cloudletEntity.getProperties().get("outputSize"))
                .setUtilizationModelRam(
                        PolicyLoader
                                .utilizationModel((String) cloudletEntity.getProperties().get("utilizationModelRam")))
                .setUtilizationModelCpu(
                        PolicyLoader
                                .utilizationModel((String) cloudletEntity.getProperties().get("utilizationModelCpu")))
                .setUtilizationModelBw(
                        PolicyLoader
                                .utilizationModel((String) cloudletEntity.getProperties().get("utilizationModelBw")));
        return cloudlet;
    }
}
