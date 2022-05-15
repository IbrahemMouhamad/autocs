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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.autocs.core.model.RunningExperiment;
import org.autocs.core.service.RunningExperimentService;
import org.autocs.backend.service.MessageService;

/**
 * Experiment run endpoint
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@RestController
public class RunningExperimentController {

    @Autowired
    RunningExperimentService runningExperimentService;

    @Autowired
    MessageService messageService;

    @GetMapping("/runs")
    public List<RunningExperiment> list() throws Exception {
        return runningExperimentService.list("runs");
    }

    @GetMapping("/runs/{id}")
    public RunningExperiment get(@PathVariable String id) throws Exception {
        return runningExperimentService.getById("runs", id);
    }

    @PostMapping("/runs")
    @ResponseStatus(HttpStatus.CREATED)
    public RunningExperiment create(@RequestBody RunningExperiment newExperimentRun) throws Exception {
        newExperimentRun = runningExperimentService.create("runs", newExperimentRun);
        messageService.sendRunRequest(newExperimentRun);
        return newExperimentRun;
    }

    @DeleteMapping("/runs/{id}")
    public void delete(@PathVariable String id) throws Exception {
        runningExperimentService.deleteById("runs", id);
    }

}
