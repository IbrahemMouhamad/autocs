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
import org.autocs.core.model.DatacenterModel;
import org.autocs.core.model.Entity;

/**
 * Serializer for Datacenter model
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

public class DatacenterSerializer extends StdSerializer<DatacenterModel> {

    @Autowired
    private EntityService<Entity> entityService;

    public DatacenterSerializer() {
        super(DatacenterModel.class);
    }

    @Override
    public void serialize(DatacenterModel datacenter, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", datacenter.getId());
        gen.writeStringField("name", datacenter.getName());
        gen.writeNumberField("amount", datacenter.getAmount());
        gen.writeStringField("description", datacenter.getDescription());
        gen.writeNumberField("lastModified", datacenter.getLastModified());
        if (datacenter.getProperties().size() > 0) {
            gen.writeObjectField("properties", datacenter.getProperties());
        }
        if (datacenter.getHosts().size() > 0) {
            if (datacenter.getStatistics().size() == 0) {
                datacenter.loadHosts(entityService);
            }
            gen.writeObjectField("hosts", datacenter.getHosts());
            gen.writeObjectField("statistics", datacenter.getStatistics());
            gen.writeNumberField("hostsNumber", datacenter.getHostsNumber());
            gen.writeNumberField("totalPES", datacenter.getTotalPES());
            gen.writeNumberField("totalMIPS", datacenter.getTotalMIPS());
            gen.writeNumberField("totalRAM", datacenter.getTotalRAM());
            gen.writeNumberField("totalStorage", datacenter.getTotalStorage());
        }
        gen.writeEndObject();
    }
}
