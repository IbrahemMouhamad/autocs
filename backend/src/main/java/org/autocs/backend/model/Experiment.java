/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * A java class to represent an experiment
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)
public class Experiment {
    private String id;
    private String name;
    private String description;
    private long runs;
    private long createdDate;
    private List<ExperimentEntity> datacenters;
    private List<ExperimentEntity> brokers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getRuns() {
        return runs;
    }

    public void setRuns(long runs) {
        this.runs = runs;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public List<ExperimentEntity> getDatacenters() {
        return datacenters;
    }

    public void setDatacenters(List<ExperimentEntity> datacenters) {
        this.datacenters = datacenters;
    }

    public List<ExperimentEntity> getBrokers() {
        return brokers;
    }

    public void setBrokers(List<ExperimentEntity> brokers) {
        this.brokers = brokers;
    }

}
