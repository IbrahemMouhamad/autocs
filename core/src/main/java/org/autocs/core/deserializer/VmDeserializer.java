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

import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.autocs.core.loader.PolicyLoader;

/**
 * Vm deserialization class.
 * It convert JSON presentation of vm to {@link Vm} object
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class VmDeserializer extends StdNodeBasedDeserializer<Vm> {

    protected VmDeserializer() {
        super(Vm.class);
    }

    @Override
    public Vm convert(JsonNode root, DeserializationContext context) throws IOException {
        VmSimple vm = new VmSimple(
                root.path("properties").get("mips").asLong(),
                root.path("properties").get("pes").asInt());
        vm
                .setRam(root.path("properties").get("ram").asLong())
                .setBw(root.path("properties").get("bw").asLong())
                .setCloudletScheduler(
                        PolicyLoader.cloudletScheduler(root.path("properties").get("cloudletScheduler").asText()));
        return vm;
    }
}
