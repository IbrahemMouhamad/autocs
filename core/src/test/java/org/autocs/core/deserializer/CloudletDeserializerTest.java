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

import org.cloudbus.cloudsim.cloudlets.CloudletSimple;

/**
 * unit testing for {@link CloudletDeserializer} class
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class CloudletDeserializerTest extends AbstractedDeserializerTest<CloudletDeserializer> {

    final String cloudletJsonFile = "cloudlet.json";

    @Before
    public void setup() {
        super.setup();
        this.deserializer = new CloudletDeserializer();
    }

    @Test
    public void cloudlet_json_to_Cloudlet_type() throws IOException {
        CloudletSimple deserialisedCloudlet = (CloudletSimple) super.convert(cloudletJsonFile);
        assertEquals(jsonNode.path("properties").get("length").asLong(), deserialisedCloudlet.getLength());
        assertEquals(jsonNode.path("properties").get("pes").asLong(), deserialisedCloudlet.getNumberOfPes());
        assertEquals(jsonNode.path("properties").get("fileSize").asLong(), deserialisedCloudlet.getFileSize());
        assertEquals(jsonNode.path("properties").get("outputSize").asLong(), deserialisedCloudlet.getOutputSize());
    }
}
