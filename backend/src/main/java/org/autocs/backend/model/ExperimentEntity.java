/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.backend.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * A java class to represent an experiment record
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)
public class ExperimentEntity extends Entity {
    private int amount;
    Map<String, List<ExperimentEntity>> nestedEntities = new LinkedHashMap<>();

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Map<String, List<ExperimentEntity>> getNestedEntities() {
        return nestedEntities;
    }

    public void setNestedEntities(Map<String, List<ExperimentEntity>> nestedEntities) {
        this.nestedEntities = nestedEntities;
    }
}
