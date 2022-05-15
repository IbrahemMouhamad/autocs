/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.autocs.core.serializer.RunningExperimentSerializer;
import org.autocs.core.service.EntityService;

/**
 * A java class to represent an experiment run
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)
@JsonSerialize(using = RunningExperimentSerializer.class)
public class RunningExperiment extends Entity {
    private Scenario scenario;
    private Boolean isRunning = false;
    private String outputPath;

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public Boolean getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(Boolean isRunning) {
        this.isRunning = isRunning;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void loadScenario(EntityService<Scenario> entityService) {
        Scenario scenario = entityService.getById("scenarios", this.getScenario().getId());
        this.setScenario(scenario);
    }
}
