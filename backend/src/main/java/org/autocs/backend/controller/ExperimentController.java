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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.autocs.backend.model.Experiment;
import org.autocs.backend.service.ExperimentService;

/**
 * Experiments endpoint
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@RestController
public class ExperimentController {

    @Autowired
    private ExperimentService experimentService;

    @GetMapping("/experiments")
    public List<Experiment> list() throws Exception {
        return experimentService.list();
    }

    @PostMapping("/experiments")
    @ResponseStatus(HttpStatus.CREATED)
    public Experiment create(@RequestBody Experiment newExperiment) throws Exception {
        return experimentService.create(newExperiment);
    }

}
