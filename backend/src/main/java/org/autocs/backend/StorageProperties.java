/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.backend;

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
    private String experimentDirectory;

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

    public String getExperimentDirectory() {
        this.createFolder(this.experimentDirectory);
        return this.experimentDirectory;
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

    public void setExperimentDirectory(String experimentDirectory) {
        this.experimentDirectory = experimentDirectory;
    }

    private void createFolder(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

}
