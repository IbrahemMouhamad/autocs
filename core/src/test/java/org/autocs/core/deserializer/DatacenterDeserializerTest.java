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

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;

/**
 * unit testing for {@link DatacenterDeserializer} class
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class DatacenterDeserializerTest extends AbstractedDeserializerTest<DatacenterDeserializer> {

    final String datacenterJsonFile = "datacenter.json";

    @Before
    public void setup() {
        super.setup();
        this.deserializer = new DatacenterDeserializer(new CloudSim());
    }

    @Test
    public void datacenter_json_to_Datacenter_type() throws IOException {
        DatacenterSimple deserialisedDatacenter = (DatacenterSimple) super.convert(datacenterJsonFile);
        assertEquals(jsonNode.path("properties").get("schedulingInterval").asLong(),
                deserialisedDatacenter.getSchedulingInterval(), 0.1);
    }
}
