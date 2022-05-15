/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.beans.factory.annotation.Autowired;

import org.autocs.core.service.EntityService;
import org.autocs.core.model.Broker;
import org.autocs.core.model.Provider;
import org.autocs.core.model.Scenario;

/**
 * Serializer for Scenario model
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

public class ScenarioSerializer extends StdSerializer<Scenario> {

    @Autowired
    private EntityService<Provider> providerService;

    @Autowired
    private EntityService<Broker> brokerService;

    public ScenarioSerializer() {
        super(Scenario.class);
    }

    @Override
    public void serialize(Scenario scenario, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", scenario.getId());
        gen.writeStringField("name", scenario.getName());
        gen.writeStringField("description", scenario.getDescription());
        gen.writeNumberField("lastModified", scenario.getLastModified());
        if (scenario.getProperties().size() > 0) {
            gen.writeObjectField("properties", scenario.getProperties());
        }
        if (scenario.getProvider() != null) {
            if (scenario.getStatistics().size() == 0) {
                scenario.loadProvider(providerService);
                scenario.loadBrokers(brokerService);
            }
            gen.writeObjectField("provider", scenario.getProvider());
            gen.writeObjectField("brokers", scenario.getBrokers());
            gen.writeObjectField("statistics", scenario.getStatistics());
        }
        gen.writeEndObject();
    }
}
