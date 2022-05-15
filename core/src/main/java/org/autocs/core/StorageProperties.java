/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private String basePath;
    private String dataDirectory;
    private String configurationDirectory;
    private String workspaceDirectory;
    private String entityDirectory;
    private String scenarioDirectory;
    private String runDirectory;

    public String getBasePath() {
        return basePath;
    }

    public String getDataDirectory() {
        this.createFolder(this.dataDirectory);
        return this.dataDirectory;
    }

    public String getConfigurationDirectory() {
        this.createFolder(this.configurationDirectory);
        return this.configurationDirectory;
    }

    public String getWorkspaceDirectory() {
        this.createFolder(this.workspaceDirectory);
        return this.workspaceDirectory;
    }

    public String getEntityDirectory() {
        this.createFolder(this.entityDirectory);
        return this.entityDirectory;
    }

    public String getScenarioDirectory() {
        this.createFolder(this.scenarioDirectory);
        return this.scenarioDirectory;
    }

    public String getRunDirectory() {
        this.createFolder(this.runDirectory);
        return this.runDirectory;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public void setConfigurationDirectory(String configurationDirectory) {
        this.configurationDirectory = configurationDirectory;
    }

    public void setWorkspaceDirectory(String workspaceDirectory) {
        this.workspaceDirectory = workspaceDirectory;
    }

    public void setEntityDirectory(String entityDirectory) {
        this.entityDirectory = entityDirectory;
    }

    public void setScenarioDirectory(String scenarioDirectory) {
        this.scenarioDirectory = scenarioDirectory;
    }

    public void setRunDirectory(String runDirectory) {
        this.runDirectory = runDirectory;
    }

    public void createFolder(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

}
