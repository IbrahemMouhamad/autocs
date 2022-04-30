/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.backend.service;

import java.util.List;
import java.util.stream.Stream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.autocs.backend.StorageProperties;
import org.autocs.backend.model.Entity;

/**
 * Entity service
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@Service
public class EntityService {

    private final String EXT = ".json";

    private final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    @Autowired
    private StorageProperties storageProps;

    @Autowired
    private ObjectMapper objectMapper;

    public List<Entity> list(String type) throws IOException, StreamReadException, DatabindException {
        final String entitiesPath = storageProps.getEntityDirectory() + File.separator + type;
        List<Entity> entities = new ArrayList<Entity>();

        try (Stream<Path> paths = Files.walk(Paths.get(entitiesPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Entity entity = objectMapper.readValue(path.toFile(), Entity.class);
                            entity.setId(path.toFile().getName().replace(EXT, ""));
                            entity.setLastModified(path.toFile().lastModified());
                            entities.add(entity);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return entities;
    }

    public Entity getById(String type, String id) {
        final String entityPath = this.getEntityPath(type, id);
        try {
            File entityFile = new File(entityPath);
            Entity entity = objectMapper.readValue(entityFile, Entity.class);
            entity.setLastModified(entityFile.lastModified());
            return entity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Entity create(String type, Entity newEntity) throws StreamWriteException, DatabindException, IOException {
        final long id = timestamp.getTime();
        final String entityPath = this.getEntityPath(type, "" + id);
        newEntity.setId("" + id);

        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(new File(entityPath), newEntity);

        return newEntity;
    }

    public Entity update(String type, Entity newEntity) throws StreamWriteException, DatabindException, IOException {
        final String entityPath = this.getEntityPath(type, newEntity.getId());
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(new File(entityPath), newEntity);

        return newEntity;
    }

    public void deleteById(String type, String id) throws IOException {
        final String entityPath = this.getEntityPath(type, id);
        Files.deleteIfExists(Paths.get(entityPath));
    }

    private String getEntityPath(String type, String id) {
        return storageProps.getEntityDirectory() + File.separator + type
                + File.separator + id + EXT;
    }
}
