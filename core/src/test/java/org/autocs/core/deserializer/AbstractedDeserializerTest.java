/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.deserializer;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer;

import org.junit.Before;

/**
 * abstracted class for deserializers unit testing
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public abstract class AbstractedDeserializerTest<T extends StdNodeBasedDeserializer> {
    private ObjectMapper mapper;
    protected T deserializer;
    protected JsonNode jsonNode;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    private JsonNode loadJsonRepresentation(String resourceFile) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceFile).getFile());
        return mapper.readTree(file);
    }

    protected Object convert(String jsonFile) throws IOException {
        this.jsonNode = this.loadJsonRepresentation(jsonFile);
        DeserializationContext context = mapper.getDeserializationContext();
        return deserializer.convert(jsonNode, context);
    }
}
