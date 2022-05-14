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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
public class EntityService<T extends Entity> {

    protected final String EXT = ".json";

    private final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    @Autowired
    protected StorageProperties storageProps;

    @Autowired
    private ObjectMapper objectMapper;

    private Class<T> type;

    @SuppressWarnings("unchecked")
    public EntityService() {
        type = (Class<T>) Entity.class;
    }

    public List<T> list(String type) throws IOException, StreamReadException, DatabindException {
        final String entitiesPath = this.getEntitiesDirectoryPath(type);
        List<T> entities = new ArrayList<T>();

        File directory = new File(entitiesPath);
        if (!directory.exists()) {
            directory.mkdir();
            return entities;
        }

        try (Stream<Path> paths = Files.walk(Paths.get(entitiesPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            T entity = objectMapper.readValue(path.toFile(), this.type);
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

    public T getById(String type, String id) {
        final String entityPath = this.getEntityFilePath(type, id);
        try {
            File entityFile = new File(entityPath);
            T entity = objectMapper.readValue(entityFile, this.type);
            return entity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public T create(String type, T newEntity)
            throws StreamWriteException, DatabindException, IOException {
        final long id = timestamp.getTime();
        final String entityPath = this.getEntityFilePath(type, "" + id);
        newEntity.setId("" + id);
        newEntity.setLastModified(id);

        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(new File(entityPath), newEntity);

        return newEntity;
    }

    public T update(String type, T entity)
            throws StreamWriteException, DatabindException, IOException {
        final long currentTimestamp = timestamp.getTime();
        final String entityPath = this.getEntityFilePath(type, entity.getId());
        entity.setLastModified(currentTimestamp);
        entity.setStatistics(new LinkedHashMap<>());

        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(new File(entityPath), entity);

        return entity;
    }

    public void deleteById(String type, String id) throws IOException {
        final String entityPath = this.getEntityFilePath(type, id);
        Files.deleteIfExists(Paths.get(entityPath));
    }

    protected String getEntitiesDirectoryPath(String type) {
        return storageProps.getEntityDirectory() + File.separator + type;
    }

    protected String getEntityFilePath(String type, String id) {
        return getEntitiesDirectoryPath(type) + File.separator + id + EXT;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }
}
