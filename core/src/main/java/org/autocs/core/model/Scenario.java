/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.autocs.core.serializer.ScenarioSerializer;
import org.autocs.core.service.EntityService;

/**
 * A java class to represent an experiment scenario
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)
@JsonSerialize(using = ScenarioSerializer.class)
public class Scenario extends Entity {
    private Provider provider;
    List<Broker> brokers = new ArrayList<>();

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public List<Broker> getBrokers() {
        return brokers;
    }

    public void setBrokers(List<Broker> brokers) {
        this.brokers = brokers;
    }

    public void loadProvider(EntityService<Provider> entityService) {
        Provider provider = entityService.getById("providers", this.provider.getId());
        this.setProvider(provider);
        this.setStatistics(provider.getStatistics());
    }

    public void loadBrokers(EntityService<Broker> entityService) {
        for (int i = 0; i < this.getBrokers().size(); i++) {
            Broker broker = entityService.getById("brokers", this.getBrokers().get(i).getId());
            broker.setAmount(this.getBrokers().get(i).getAmount());
            this.getBrokers().set(i, broker);
        }
    }
}
