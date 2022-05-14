/*
 * Title:        Engine Package
 * Description:  Engine package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.engine;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private String basePath;
    private String workspaceDirectory;
    private String scenarioDirectory;

    public String getBasePath() {
        return basePath;
    }

    public String getWorkspaceDirectory() {
        return this.workspaceDirectory;
    }

    public String getScenarioDirectory() {
        return this.scenarioDirectory;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setWorkspaceDirectory(String workspaceDirectory) {
        this.workspaceDirectory = workspaceDirectory;
    }

    public void setScenarioDirectory(String scenarioDirectory) {
        this.scenarioDirectory = scenarioDirectory;
    }

    public void createFolder(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

}
