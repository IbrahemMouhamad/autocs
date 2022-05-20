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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.autocs.core.experiment.AutoCSExperimentRunner;
import org.autocs.core.model.ScenarioRun;

/**
 * Experiment run service to receive run request from RabbitQM runs queue
 * and start running the scenario
 *
 * @author Ibrahem Mouhamad
 * @since Engine Package 1.0.0
 */

@Service
public class ExperimentRunnerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.results_exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.results_routingkey}")
    private String routingKey;

    public void sendResult(ScenarioRun scenarioRun) {
        rabbitTemplate.convertAndSend(exchange, routingKey, scenarioRun);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void run(ScenarioRun scenarioRun) {
        AutoCSExperimentRunner runner = new AutoCSExperimentRunner(9075098589732L, scenarioRun);
        // run scenario
        runner.setVerbose(true).run();
        // return results
        sendResult(scenarioRun);
    }
}
