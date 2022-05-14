/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.backend.service;

import org.autocs.backend.model.Datacenter;
import org.springframework.stereotype.Service;

/**
 * Datacenter service
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@Service
public class DatacenterService extends EntityService<Datacenter> {
    public DatacenterService() {
        super.setType(Datacenter.class);
    }
}
