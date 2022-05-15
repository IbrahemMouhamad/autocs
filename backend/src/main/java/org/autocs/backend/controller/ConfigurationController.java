/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.autocs.core.service.ConfigurationService;
import org.autocs.core.model.Configuration;

/**
 * Entity configuration endpoint
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@RestController
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @GetMapping("/configurations")
    public List<Configuration> list() throws Exception {
        return configurationService.list();
    }

    @GetMapping("/configurations/{id}")
    public Configuration get(@PathVariable String id) throws Exception {
        return configurationService.getById(id);
    }
}
