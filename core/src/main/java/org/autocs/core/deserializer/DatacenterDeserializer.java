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

import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.autocs.core.loader.PolicyLoader;

/**
 * datacenter deserialization class.
 * It convert JSON presentation of datacenter to {@link Datacenter} object
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class DatacenterDeserializer extends StdNodeBasedDeserializer<Datacenter> {

    private Simulation simulation;
    private HostDeserializer hostDeserializer;

    protected DatacenterDeserializer(Simulation simulation) {
        super(Datacenter.class);
        this.simulation = simulation;
        this.hostDeserializer = new HostDeserializer();
    }

    @Override
    public Datacenter convert(JsonNode root, DeserializationContext context) throws IOException {
        // deserialize hosts
        final List<Host> hostList = new ArrayList<>();
        JsonNode hostsArrayNode = root.get("hosts");
        if (hostsArrayNode.isArray()) {
            for (JsonNode hostNode : hostsArrayNode) {
                final int amount = hostNode.get("amount").asInt();
                for (int i = 0; i < amount; i++) {
                    Host host = hostDeserializer.convert(hostNode, context);
                    hostList.add(host);
                }
            }
        }
        // datacenter creation
        Datacenter datacenter = new DatacenterSimple(
                this.simulation,
                hostList,
                PolicyLoader.vmAllocationPolicy(root.path("properties").get("vmAllocationPolicy").asText()));
        datacenter
                .setSchedulingInterval(root.path("properties").get("schedulingInterval").asInt());
        return datacenter;
    }
}
