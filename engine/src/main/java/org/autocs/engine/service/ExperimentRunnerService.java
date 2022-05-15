/*
 * Title:        Engine Package
 * Description:  Engine package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.engine.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.autocs.core.model.RunningExperiment;
import org.autocs.core.testbeds.AutoCSExperimentRunner;

/**
 * Experiment run service to receive run request from RabbitQM runs queue
 * and start running the scenario
 *
 * @author Ibrahem Mouhamad
 * @since Engine Package 1.0.0
 */

@Service
public class ExperimentRunnerService {

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void run(RunningExperiment experimentRun) {
        AutoCSExperimentRunner runner = new AutoCSExperimentRunner(9075098589732L, experimentRun);
        runner.setVerbose(true).run();
    }
}
