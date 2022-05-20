/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.autocs.core.model.ScenarioRun;

/**
 * Running Experiment service
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@Service
public class ScenarioRunService extends EntityService<ScenarioRun> {

    public ScenarioRunService() {
        super.setType(ScenarioRun.class);
    }

    @Override
    public List<ScenarioRun> list(String type) throws IOException, StreamReadException, DatabindException {
        final String entitiesPath = this.getEntitiesDirectoryPath(type);
        List<ScenarioRun> entities = new ArrayList<ScenarioRun>();

        File directory = new File(entitiesPath);
        if (!directory.exists()) {
            directory.mkdir();
            return entities;
        }

        Arrays.asList(new File(entitiesPath).listFiles(File::isFile))
                .forEach(entityFile -> {
                    try {
                        ScenarioRun entity = objectMapper.readValue(entityFile, ScenarioRun.class);
                        entity.setId(entityFile.getName().replace(EXT, ""));
                        entity.setLastModified(entityFile.lastModified());
                        entities.add(entity);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        return entities;
    }

    @Override
    public ScenarioRun create(String type, ScenarioRun newEntity)
            throws StreamWriteException, DatabindException, IOException {
        final long id = timestamp.getTime();
        final String entityPath = this.getEntityFilePath("runs", "" + id);
        newEntity.setId("" + id);
        newEntity.setLastModified(id);
        newEntity.setOutputPath(entityPath);

        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(new File(entityPath), newEntity);

        return newEntity;
    }

    @Override
    public ScenarioRun update(String type, ScenarioRun entity)
            throws StreamWriteException, DatabindException, IOException {
        final long currentTimestamp = timestamp.getTime();
        entity.setIsRunning(false);
        entity.setLastModified(currentTimestamp);

        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(
                new File(getEntityFilePath("runs", entity.getId())),
                entity);

        return entity;
    }

    @Override
    protected String getEntitiesDirectoryPath(String type) {
        return storageProps.getRunDirectory();
    }
}
