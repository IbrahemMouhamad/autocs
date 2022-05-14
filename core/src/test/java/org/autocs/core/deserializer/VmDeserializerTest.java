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

import org.cloudbus.cloudsim.vms.VmSimple;

/**
 * unit testing for {@link VmDeserializer} class
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class VmDeserializerTest extends AbstractedDeserializerTest<VmDeserializer> {

    final String vmJsonFile = "vm.json";

    @Before
    public void setup() {
        super.setup();
        this.deserializer = new VmDeserializer();
    }

    @Test
    public void vm_json_to_Vm_type() throws IOException {
        VmSimple deserialisedVm = (VmSimple) super.convert(vmJsonFile);
        assertEquals(jsonNode.path("properties").get("pes").asLong(), deserialisedVm.getNumberOfPes());
        assertEquals(jsonNode.path("properties").get("mips").asLong() * deserialisedVm.getNumberOfPes(),
                deserialisedVm.getTotalMipsCapacity(),
                0.1);
        assertEquals(jsonNode.path("properties").get("ram").asLong(), deserialisedVm.getRam().getCapacity());
        assertEquals(jsonNode.path("properties").get("bw").asLong(), deserialisedVm.getBw().getCapacity());
    }
}
