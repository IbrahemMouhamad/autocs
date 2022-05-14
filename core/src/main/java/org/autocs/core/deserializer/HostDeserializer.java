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
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer;

import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.autocs.core.loader.PolicyLoader;

/**
 * host deserialization class.
 * It convert JSON presentation of host to {@link Host} object
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class HostDeserializer extends StdNodeBasedDeserializer<Host> {

    protected HostDeserializer() {
        super(Host.class);
    }

    @Override
    public Host convert(JsonNode root, DeserializationContext context) throws IOException {
        // create processing elements list
        final List<Pe> peList = getPeList(
                root.path("properties").get("pes").asInt(),
                root.path("properties").get("mips").asLong(),
                root.path("properties").get("peProvisioner").asText());

        HostSimple host = new HostSimple(
                root.path("properties").get("ram").asLong(),
                root.path("properties").get("bw").asLong(),
                root.path("properties").get("storage").asLong(),
                peList);
        host
                .setRamProvisioner(
                        PolicyLoader.newResourceProvisioner(root.path("properties").get("ramProvisioner").asText()))
                .setBwProvisioner(
                        PolicyLoader.newResourceProvisioner(root.path("properties").get("bwProvisioner").asText()))
                .setVmScheduler(
                        PolicyLoader.vmScheduler(root.path("properties").get("vmScheduler").asText()));
        return host;
    }

    private List<Pe> getPeList(final int pes, final long mips, final String peProvisioner) {
        final List<Pe> peList = new ArrayList<>(pes);
        for (int i = 0; i < pes; i++) {
            peList.add(new PeSimple(mips, PolicyLoader.newPeProvisioner(peProvisioner)));
        }
        return peList;
    }
}
