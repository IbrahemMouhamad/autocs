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

import java.io.IOException;

import org.cloudbus.cloudsim.core.CloudSim;

/**
 * unit testing for {@link DatacenterBrokerDeserializer} class
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class DatacenterBrokerDeserializerTest extends AbstractedDeserializerTest<DatacenterBrokerDeserializer> {

    final String brokerJsonFile = "broker.json";

    @Before
    public void setup() {
        super.setup();
        this.deserializer = new DatacenterBrokerDeserializer(new CloudSim());
    }

    @Test
    public void broker_json_to_Broker_type() throws IOException {
        // DatacenterBrokerSimple deserialisedBroker = (DatacenterBrokerSimple)
        // super.convert(brokerJsonFile);
    }
}
