/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.backend.controller;

import org.autocs.backend.service.ExperimentRunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Experiment run endpoint
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@RestController
public class ExperimentRunController {

    @Autowired
    ExperimentRunService experimentRunService;
    private static final Logger logger = LoggerFactory.getLogger(ExperimentRunController.class);

    @PostMapping("/runs/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public String run(@PathVariable String id) {
        experimentRunService.sendRunRequest(id);
        logger.info("id sent: " + id);
        return id;
    }

}
