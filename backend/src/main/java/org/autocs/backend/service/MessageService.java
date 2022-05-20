/*
 * Title:        Backend Server
 * Description:  Backend server of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.backend.service;

import java.io.IOException;

import org.autocs.core.model.ScenarioRun;
import org.autocs.core.service.ScenarioRunService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Running Experiment service
 *
 * @author Ibrahem Mouhamad
 * @since Backend Server 1.0.0
 */

@Service
public class MessageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ScenarioRunService scenarioRunService;

    @Value("${spring.rabbitmq.runs_exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.runs_routingkey}")
    private String routingKey;

    public void sendRunRequest(ScenarioRun scenarioRun) {
        rabbitTemplate.convertAndSend(exchange, routingKey, scenarioRun);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receiveResult(ScenarioRun scenarioRun) {
        try {
            scenarioRunService.update("runs", scenarioRun);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
