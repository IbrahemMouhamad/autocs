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

import org.autocs.core.service.ProviderService;
import org.autocs.core.model.Provider;

/**
 * Cloud service providers endpoint
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@RestController
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @GetMapping("/providers")
    public List<Provider> list() throws Exception {
        return providerService.list("providers");
    }

    @GetMapping("/providers/{id}")
    public Provider get(@PathVariable String id) throws Exception {
        return providerService.getById("providers", id);
    }

    @PostMapping("/providers")
    @ResponseStatus(HttpStatus.CREATED)
    public Provider create(@RequestBody Provider newEntity) throws Exception {
        return providerService.create("providers", newEntity);
    }

    @PutMapping("/providers/{id}")
    public Provider update(@RequestBody Provider newEntity) throws Exception {
        return providerService.update("providers", newEntity);
    }

    @DeleteMapping("/providers/{id}")
    public void delete(@PathVariable String id) throws Exception {
        providerService.deleteById("providers", id);
    }
}
