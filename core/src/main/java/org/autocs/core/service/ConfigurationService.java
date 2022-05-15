/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.service;

import java.util.List;
import java.util.stream.Stream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.autocs.core.StorageProperties;
import org.autocs.core.model.Configuration;

/**
 * Entity configuration service
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@Service
public class ConfigurationService {

    private final String EXT = ".json";

    @Autowired
    private StorageProperties storageProps;

    public List<Configuration> list() throws IOException, StreamReadException, DatabindException {
        final ObjectMapper objectMapper = new ObjectMapper();
        List<Configuration> configuration = new ArrayList<Configuration>();

        try (Stream<Path> paths = Files.walk(Paths.get(storageProps.getConfigurationDirectory()))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            configuration.add(objectMapper.readValue(path.toFile(), Configuration.class));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return configuration;
    }

    public Configuration getById(String id) {
        final String configurationPath = storageProps.getConfigurationDirectory() + File.separator + id + EXT;
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(configurationPath), Configuration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
