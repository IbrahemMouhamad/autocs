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

import org.autocs.core.service.EntityService;
import org.autocs.core.model.Entity;

/**
 * Entities endpoint
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@RestController
public class EntityController {

    @Autowired
    private EntityService<Entity> entityService;

    @GetMapping("/entities/{type}")
    public List<Entity> list(@PathVariable String type) throws Exception {
        return entityService.list(type);
    }

    @GetMapping("/entities/{type}/{id}")
    public Entity get(@PathVariable String type, @PathVariable String id) throws Exception {
        return entityService.getById(type, id);
    }

    @PostMapping("/entities/{type}")
    @ResponseStatus(HttpStatus.CREATED)
    public Entity create(
            @PathVariable String type, @RequestBody Entity newEntity) throws Exception {
        return entityService.create(type, newEntity);
    }

    @PutMapping("/entities/{type}/{id}")
    public Entity update(
            @PathVariable String type, @RequestBody Entity newEntity) throws Exception {
        return entityService.update(type, newEntity);
    }

    @DeleteMapping("/entities/{type}/{id}")
    public void delete(@PathVariable String type, @PathVariable String id) throws Exception {
        entityService.deleteById(type, id);
    }
}
