/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.deserializer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.cloudbus.cloudsim.hosts.HostSimple;
import org.junit.Before;
import org.junit.Test;

/**
 * unit testing for {@link HostDeserializer} class
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class HostDeserializerTest extends AbstractedDeserializerTest<HostDeserializer> {

    final String hostJsonFile = "host.json";

    @Before
    public void setup() {
        super.setup();
        this.deserializer = new HostDeserializer();
    }

    @Test
    public void host_json_to_Host_type() throws IOException {
        HostSimple deserialisedHost = (HostSimple) super.convert(hostJsonFile);
        assertEquals(jsonNode.path("properties").get("ram").asLong(), deserialisedHost.getRam().getCapacity());
        assertEquals(jsonNode.path("properties").get("bw").asLong(), deserialisedHost.getBw().getCapacity());
        assertEquals(jsonNode.path("properties").get("storage").asLong(), deserialisedHost.getStorage().getCapacity());
        assertEquals(jsonNode.path("properties").get("pes").asInt(), deserialisedHost.getNumberOfPes());
        assertEquals(jsonNode.path("properties").get("ram").asLong(), deserialisedHost.getRam().getCapacity());
    }
}
