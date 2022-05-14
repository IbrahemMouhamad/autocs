/*
 * Title:        Engine Package
 * Description:  Engine package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.engine.service;

import java.io.File;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.autocs.core.testbeds.AutoCSExperimentRunner;
import org.autocs.engine.StorageProperties;

/**
 * Experiment run service to receive from RabbitQM runs queue
 * and start running the scenario
 *
 * @author Ibrahem Mouhamad
 * @since Engine Package 1.0.0
 */

@Service
public class ExperimentRunnerService {

    private String scenarioFile;
    private String outputPath;

    @Autowired
    private StorageProperties storageProps;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void run(String scenarioID) {
        this.scenarioFile = storageProps.getScenarioDirectory() + File.separator + scenarioID + ".json";
        this.outputPath = storageProps.getScenarioDirectory() + File.separator + scenarioID;
        // create output folder
        storageProps.createFolder(outputPath);
        File inputFile = new File(this.scenarioFile);
        AutoCSExperimentRunner runner = new AutoCSExperimentRunner(9075098589732L, 3, inputFile,
                this.outputPath);
        runner.setVerbose(true).run();
    }
}
