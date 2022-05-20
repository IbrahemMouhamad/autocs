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

import org.autocs.core.service.DatacenterService;
import org.autocs.core.model.DatacenterModel;

/**
 * Datacenters endpoint
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@RestController
public class DatacenterController {

    @Autowired
    private DatacenterService datacenterService;

    @GetMapping("/datacenters")
    public List<DatacenterModel> list() throws Exception {
        return datacenterService.list("datacenters");
    }

    @GetMapping("/datacenters/{id}")
    public DatacenterModel get(@PathVariable String id) throws Exception {
        return datacenterService.getById("datacenters", id);
    }

    @PostMapping("/datacenters")
    @ResponseStatus(HttpStatus.CREATED)
    public DatacenterModel create(@RequestBody DatacenterModel newEntity) throws Exception {
        return datacenterService.create("datacenters", newEntity);
    }

    @PutMapping("/datacenters/{id}")
    public DatacenterModel update(@RequestBody DatacenterModel newEntity) throws Exception {
        return datacenterService.update("datacenters", newEntity);
    }

    @DeleteMapping("/datacenters/{id}")
    public void delete(@PathVariable String id) throws Exception {
        datacenterService.deleteById("datacenters", id);
    }
}
