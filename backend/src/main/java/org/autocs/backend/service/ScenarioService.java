/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.backend.service;

import org.autocs.backend.model.Scenario;
import org.springframework.stereotype.Service;

/**
 * Scenario service
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@Service
public class ScenarioService extends EntityService<Scenario> {

    public ScenarioService() {
        super.setType(Scenario.class);
    }

    @Override
    protected String getEntitiesDirectoryPath(String type) {
        return storageProps.getScenarioDirectory();
    }
}
