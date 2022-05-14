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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.autocs.backend.model.Scenario;
import org.autocs.backend.service.ScenarioService;

/**
 * Scenarios endpoint
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@RestController
public class ScenarioController {

    @Autowired
    private ScenarioService scenarioService;

    @GetMapping("/scenarios")
    public List<Scenario> list() throws Exception {
        return scenarioService.list("scenarios");
    }

    @GetMapping("/scenarios/{id}")
    public Scenario get(@PathVariable String id) throws Exception {
        return scenarioService.getById("scenarios", id);
    }

    @PostMapping("/scenarios")
    @ResponseStatus(HttpStatus.CREATED)
    public Scenario create(@RequestBody Scenario newScenario) throws Exception {
        return scenarioService.create("scenarios", newScenario);
    }

    @PutMapping("/scenarios/{id}")
    public Scenario update(@RequestBody Scenario newEntity) throws Exception {
        return scenarioService.update("scenarios", newEntity);
    }

    @DeleteMapping("/scenarios/{id}")
    public void delete(@PathVariable String id) throws Exception {
        scenarioService.deleteById("scenarios", id);
    }
}
