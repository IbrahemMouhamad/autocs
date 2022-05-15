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

import org.autocs.core.service.BrokerService;
import org.autocs.core.model.Broker;

/**
 * Brokers (customers) endpoint
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@RestController
public class BrokerController {

    @Autowired
    private BrokerService brokerService;

    @GetMapping("/brokers")
    public List<Broker> list() throws Exception {
        return brokerService.list("brokers");
    }

    @GetMapping("/brokers/{id}")
    public Broker get(@PathVariable String id) throws Exception {
        return brokerService.getById("brokers", id);
    }

    @PostMapping("/brokers")
    @ResponseStatus(HttpStatus.CREATED)
    public Broker create(@RequestBody Broker newEntity) throws Exception {
        return brokerService.create("brokers", newEntity);
    }

    @PutMapping("/brokers/{id}")
    public Broker update(@RequestBody Broker newEntity) throws Exception {
        return brokerService.update("brokers", newEntity);
    }

    @DeleteMapping("/brokers/{id}")
    public void delete(@PathVariable String id) throws Exception {
        brokerService.deleteById("brokers", id);
    }
}
