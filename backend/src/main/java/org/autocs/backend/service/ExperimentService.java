// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

package org.autocs.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.autocs.backend.StorageProperties;
import org.autocs.backend.model.Experiment;

@Service
public class ExperimentService {

    private final String EXT = ".json";

    private final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    @Autowired
    private StorageProperties storageProps;

    @Autowired
    private ObjectMapper objectMapper;

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

    public Experiment create(Experiment newExperiment) {
        final long id = timestamp.getTime();
        newExperiment.setId("" + id);
        return newExperiment;
    }

}
