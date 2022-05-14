/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.autocs.core.loader.PolicyLoader;

/**
 * cloudlet deserialization class.
 * It convert JSON presentation of cloudlet to {@link Cloudlet} object
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class CloudletDeserializer extends StdNodeBasedDeserializer<Cloudlet> {

    protected CloudletDeserializer() {
        super(Cloudlet.class);
    }

    @Override
    public Cloudlet convert(JsonNode root, DeserializationContext context) throws IOException {
        CloudletSimple cloudlet = new CloudletSimple(
                root.path("properties").get("length").asLong(),
                root.path("properties").get("pes").asInt());
        cloudlet
                .setFileSize(root.path("properties").get("fileSize").asLong())
                .setOutputSize(root.path("properties").get("outputSize").asLong())
                .setUtilizationModelRam(
                        PolicyLoader.utilizationModel(root.path("properties").get("utilizationModelRam").asText()))
                .setUtilizationModelCpu(
                        PolicyLoader.utilizationModel(root.path("properties").get("utilizationModelCpu").asText()))
                .setUtilizationModelBw(
                        PolicyLoader.utilizationModel(root.path("properties").get("utilizationModelBw").asText()));
        return cloudlet;
    }
}
