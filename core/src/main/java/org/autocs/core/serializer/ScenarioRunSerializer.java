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
import org.autocs.core.model.ScenarioRun;
import org.autocs.core.model.Scenario;

/**
 * Serializer for ExperimentRun model
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

public class ScenarioRunSerializer extends StdSerializer<ScenarioRun> {

    @Autowired
    private EntityService<Scenario> scenarioService;

    public ScenarioRunSerializer() {
        super(ScenarioRun.class);
    }

    @Override
    public void serialize(ScenarioRun experimentRun, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", experimentRun.getId());
        gen.writeStringField("name", experimentRun.getName());
        gen.writeStringField("description", experimentRun.getDescription());
        gen.writeNumberField("lastModified", experimentRun.getLastModified());
        gen.writeBooleanField("isRunning", experimentRun.getIsRunning());
        gen.writeStringField("outputPath", experimentRun.getOutputPath());
        if (experimentRun.getProperties().size() > 0) {
            gen.writeObjectField("properties", experimentRun.getProperties());
        }
        if (experimentRun.getScenario().getBrokers().size() == 0) {
            experimentRun.loadScenario(scenarioService);
        }
        gen.writeObjectField("scenario", experimentRun.getScenario());
        gen.writeObjectField("metrics", experimentRun.getMetrics());
        gen.writeEndObject();
    }
}
