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
import org.autocs.core.model.BrokerModel;
import org.autocs.core.model.Entity;

/**
 * Serializer for Broker model
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

public class BrokerSerializer extends StdSerializer<BrokerModel> {

    @Autowired
    private EntityService<Entity> entityService;

    public BrokerSerializer() {
        super(BrokerModel.class);
    }

    @Override
    public void serialize(BrokerModel broker, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", broker.getId());
        gen.writeStringField("name", broker.getName());
        gen.writeStringField("description", broker.getDescription());
        gen.writeNumberField("lastModified", broker.getLastModified());
        gen.writeNumberField("amount", broker.getAmount());
        if (broker.getProperties().size() > 0) {
            gen.writeObjectField("properties", broker.getProperties());
        }
        if (broker.getVms().size() > 0) {
            if (broker.getStatistics().size() == 0) {
                broker.loadVms(entityService);
                broker.loadCloudlets(entityService);
            }
            gen.writeObjectField("vms", broker.getVms());
            gen.writeObjectField("cloudlets", broker.getCloudlets());
            gen.writeObjectField("statistics", broker.getStatistics());
            gen.writeNumberField("VmsNumber", broker.getVmsNumber());
            gen.writeNumberField("totalVmPES", broker.getTotalVmPES());
            gen.writeNumberField("totalVmMIPS", broker.getTotalVmMIPS());
            gen.writeNumberField("totalVmRAM", broker.getTotalVmRAM());
            gen.writeNumberField("totalVmBW", broker.getTotalVmBW());
            gen.writeNumberField("CloudletsNumber", broker.getCloudletsNumber());
            gen.writeNumberField("totalCloudletsPES", broker.getTotalCloudletsPES());
            gen.writeNumberField("totalCloudletsLength", broker.getTotalCloudletsLength());
        }
        gen.writeEndObject();
    }
}
