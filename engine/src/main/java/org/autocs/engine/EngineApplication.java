/*
 * Title:        Engine Package
 * Description:  Engine package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Backend Application
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@SpringBootApplication(scanBasePackages = { "org.autocs.core", "org.autocs.engine" })
public class EngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(EngineApplication.class, args);
    }

}
