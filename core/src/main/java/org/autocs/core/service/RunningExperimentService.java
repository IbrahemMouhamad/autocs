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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.autocs.core.model.RunningExperiment;

/**
 * Running Experiment service
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@Service
public class RunningExperimentService extends EntityService<RunningExperiment> {

    public RunningExperimentService() {
        super.setType(RunningExperiment.class);
    }

    @Override
    public RunningExperiment create(String type, RunningExperiment newEntity)
            throws StreamWriteException, DatabindException, IOException {
        final long id = timestamp.getTime();
        final String entityPath = this.getEntityFilePath("runs", "" + id);
        newEntity.setId("" + id);
        newEntity.setLastModified(id);
        newEntity.setOutputPath(getOutputDirectory(newEntity.getId()));

        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(new File(entityPath), newEntity);

        return newEntity;
    }

    @Override
    protected String getEntitiesDirectoryPath(String type) {
        return storageProps.getRunDirectory();
    }

    @Override
    protected String getEntityFilePath(String type, String id) {
        final String runDirectory = getOutputDirectory(id);
        storageProps.createFolder(runDirectory);
        return runDirectory + File.separator + id + EXT;
    }

    private String getOutputDirectory(String id) {
        return getEntitiesDirectoryPath("runs") + File.separator + id;
    }
}
