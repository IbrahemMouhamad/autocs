/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.backend.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.beans.factory.annotation.Autowired;

import org.autocs.backend.model.Datacenter;
import org.autocs.backend.model.Provider;
import org.autocs.backend.service.EntityService;

/**
 * Serializer for Datacenter model
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

public class ProviderSerializer extends StdSerializer<Provider> {

    @Autowired
    private EntityService<Datacenter> datacenterService;

    public ProviderSerializer() {
        super(Provider.class);
    }

    @Override
    public void serialize(Provider providerEntity, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", providerEntity.getId());
        gen.writeStringField("name", providerEntity.getName());
        gen.writeStringField("description", providerEntity.getDescription());
        gen.writeNumberField("lastModified", providerEntity.getLastModified());
        if (providerEntity.getProperties().size() > 0) {
            gen.writeObjectField("properties", providerEntity.getProperties());
        }
        if (providerEntity.getDatacenters().size() > 0) {
            if (providerEntity.getStatistics().size() == 0) {
                providerEntity.loadDatacenter(datacenterService);
            }
            gen.writeObjectField("datacenters", providerEntity.getDatacenters());
            gen.writeObjectField("statistics", providerEntity.getStatistics());
        }
        gen.writeEndObject();
    }
}
