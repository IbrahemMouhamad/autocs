/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.deserializer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.autocs.core.testbeds.ScenarioHolder;
import org.cloudbus.cloudsim.core.CloudSim;

/**
 * unit testing for {@link ScenarioHolderDeserializer} class
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class ScenarioHolderDeserializerTest extends AbstractedDeserializerTest<ScenarioHolderDeserializer> {

    final String scenarioJsonFile = "scenario.json";

    @Before
    public void setup() {
        super.setup();
        this.deserializer = new ScenarioHolderDeserializer(new CloudSim());
    }

    @Test
    public void scenario_json_to_Scenario_type() throws IOException {
        ScenarioHolder deserialisedScenarioHolder = (ScenarioHolder) super.convert(scenarioJsonFile);
        assertEquals(1, deserialisedScenarioHolder.getDatacenterList().size());
    }
}
