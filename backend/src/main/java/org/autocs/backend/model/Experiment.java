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
@JsonInclude(value = Include.NON_NULL)
public class Experiment {
    private String id;
    private String name;
    private String description;
    private long createdDate;
    private List<ExperimentRecord> datacenters;
    private List<ExperimentRecord> brokers;

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

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public List<ExperimentRecord> getDatacenters() {
        return datacenters;
    }

    public void setDatacenters(List<ExperimentRecord> datacenters) {
        this.datacenters = datacenters;
    }

    public List<ExperimentRecord> getBrokers() {
        return brokers;
    }

    public void setBrokers(List<ExperimentRecord> brokers) {
        this.brokers = brokers;
    }

}
