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

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.autocs.core.testbeds.ScenarioHolder;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;

/**
 * scenario deserialization class.
 * It convert JSON presentation of scenario to {@link ScenarioHolder} object
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class ScenarioHolderDeserializer extends StdNodeBasedDeserializer<ScenarioHolder> {

    private CloudSim simulation;
    private DatacenterDeserializer datacenterDeserializer;
    private DatacenterBrokerDeserializer brokerDeserializer;

    public ScenarioHolderDeserializer(CloudSim simulation) {
        super(ScenarioHolder.class);
        this.simulation = simulation;
        this.datacenterDeserializer = new DatacenterDeserializer(this.simulation);
        this.brokerDeserializer = new DatacenterBrokerDeserializer(this.simulation);
    }

    @Override
    public ScenarioHolder convert(JsonNode root, DeserializationContext context) throws IOException {
        ScenarioHolder scenarioHolder = new ScenarioHolder();
        // get datacenters
        this.getDatacenters(scenarioHolder, root, context);
        // get brokers
        this.getBrokers(scenarioHolder, root, context);
        return scenarioHolder;
    }

    private void getDatacenters(ScenarioHolder scenarioHolder, JsonNode root, DeserializationContext context)
            throws IOException {
        JsonNode datacentersArrayNode = root.path("provider").get("datacenters");
        if (datacentersArrayNode.isArray()) {
            for (JsonNode datacenterNode : datacentersArrayNode) {
                final int amount = datacenterNode.get("amount").asInt();
                final String name = datacenterNode.get("name").asText();
                for (int i = 0; i < amount; i++) {
                    Datacenter datacenter = this.datacenterDeserializer.convert(datacenterNode, context);
                    scenarioHolder.addDatacenter(name, datacenter);
                }
            }
        }
    }

    private void getBrokers(ScenarioHolder scenarioHolder, JsonNode root, DeserializationContext context)
            throws IOException {
        JsonNode brokersArrayNode = root.get("brokers");
        if (brokersArrayNode.isArray()) {
            for (JsonNode brokerNode : brokersArrayNode) {
                final int amount = brokerNode.get("amount").asInt();
                final String name = brokerNode.get("name").asText();
                for (int i = 0; i < amount; i++) {
                    DatacenterBroker broker = this.brokerDeserializer.convert(brokerNode, context);
                    scenarioHolder.addBroker(name, broker);
                    // get vms
                    scenarioHolder.addVmsListToBroker(broker, this.brokerDeserializer.getVmsList(brokerNode, context));
                    // get cloudlets
                    scenarioHolder.addCloudletsListToBroker(broker,
                            this.brokerDeserializer.getCloudletsList(brokerNode, context));
                }
            }
        }
    }
}
