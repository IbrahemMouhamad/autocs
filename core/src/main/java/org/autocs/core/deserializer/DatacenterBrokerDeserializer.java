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

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;

/**
 * broker deserialization class.
 * It convert JSON presentation of broker to {@link Datacenter} object
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class DatacenterBrokerDeserializer extends StdNodeBasedDeserializer<DatacenterBroker> {

    private CloudSim simulation;
    private VmDeserializer vmDeserializer;
    private CloudletDeserializer cloudletDeserializer;

    protected DatacenterBrokerDeserializer(CloudSim simulation) {
        super(DatacenterBroker.class);
        this.simulation = simulation;
        this.vmDeserializer = new VmDeserializer();
        this.cloudletDeserializer = new CloudletDeserializer();
    }

    @Override
    public DatacenterBroker convert(JsonNode root, DeserializationContext context) throws IOException {
        // datacenter broker creation
        DatacenterBroker broker = new DatacenterBrokerSimple(this.simulation);
        return broker;
    }

    public List<Vm> getVmsList(JsonNode root, DeserializationContext context) throws IOException {
        final List<Vm> vmList = new ArrayList<>();
        JsonNode vmsArrayNode = root.get("vms");
        if (vmsArrayNode.isArray()) {
            for (JsonNode vmNode : vmsArrayNode) {
                final int amount = vmNode.get("amount").asInt();
                for (int i = 0; i < amount; i++) {
                    Vm vm = vmDeserializer.convert(vmNode, context);
                    vmList.add(vm);
                }
            }
        }
        return vmList;
    }

    public List<Cloudlet> getCloudletsList(JsonNode root, DeserializationContext context) throws IOException {
        final List<Cloudlet> cloudletList = new ArrayList<>();
        JsonNode cloudletsArrayNode = root.get("cloudlets");
        if (cloudletsArrayNode.isArray()) {
            for (JsonNode cloudletNode : cloudletsArrayNode) {
                final int amount = cloudletNode.get("amount").asInt();
                for (int i = 0; i < amount; i++) {
                    Cloudlet cloudlet = cloudletDeserializer.convert(cloudletNode, context);
                    cloudletList.add(cloudlet);
                }
            }
        }
        return cloudletList;
    }
}
