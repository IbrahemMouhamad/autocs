// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

package org.autocs.backend.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.autocs.backend.StorageProperties;
import org.autocs.backend.model.Experiment;
import org.autocs.backend.model.ExperimentEntity;
import org.autocs.backend.model.Entity;

@Service
public class ExperimentService {

    private final String EXT = ".json";

    private final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    @Autowired
    private StorageProperties storageProps;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityService entityService;

    public List<Experiment> list() throws IOException, StreamReadException, DatabindException {
        final String experimentsPath = storageProps.getExperimentDirectory();
        List<Experiment> experiments = new ArrayList<Experiment>();

        try (Stream<Path> paths = Files.walk(Paths.get(experimentsPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Experiment experiment = objectMapper.readValue(path.toFile(), Experiment.class);
                            experiment.setId(path.toFile().getName().replace(EXT, ""));
                            experiments.add(experiment);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return experiments;
    }

    public Experiment create(Experiment newExperiment) throws StreamWriteException, DatabindException, IOException {
        final long id = timestamp.getTime();
        final String experimentPath = this.getExperimentPath("" + id);
        newExperiment.setId("" + id);

        // resolve datacenters entities
        newExperiment.getDatacenters().forEach((datacenter) -> resolveExperimentEntityByID(datacenter, "datacenters"));

        // resolve brokers entities
        newExperiment.getBrokers().forEach((broker) -> resolveExperimentEntityByID(broker, "brokers"));

        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(new File(experimentPath), newExperiment);

        return newExperiment;
    }

    private String getExperimentPath(String id) {
        return storageProps.getExperimentDirectory() + File.separator + id + EXT;
    }

    private void resolveExperimentEntityByID(ExperimentEntity entity, String type) {
        Entity parent = entityService.getById(type, entity.getId());
        entity.setName(parent.getName());
        entity.setDescription(parent.getDescription());
        entity.setProperties(parent.getProperties());
        entity.getNestedEntities().forEach((nestedType, nestedEntitiesList) -> {
            nestedEntitiesList.forEach((nestedEntity) -> resolveExperimentEntityByID(nestedEntity, nestedType));
        });
    }
}
