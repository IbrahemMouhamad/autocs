/*
 * Title:        CloudSim Plus SDN
 * Description:  Cloud Simulation Toolkit for Modeling and Simulation of Cloud networks and SDN
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.data.networkelement.switches;

import java.util.function.Function;

import org.cloudbus.cloudsim.network.switches.Switch;
import org.autocs.sdn.data.networkelement.resources.NetworkElementStateEntry;
import org.autocs.sdn.data.networkelement.resources.NetworkElementStats;

public class SwitchResourceStats extends NetworkElementStats<Switch> {
    public static final SwitchResourceStats NULL = new SwitchResourceStats(Switch.NULL, _switch -> null) {
        @Override
        public boolean add(double time) {
            return false;
        }
    };

    /**
     * Creates a SwitchResourceStats to collect resource utilization statistics for
     * a Switch.
     *
     * @param networkElement              the Switch where the statistics will be
     *                                    collected
     * @param resourceUtilizationFunction a {@link Function} that receives a Switch
     *                                    and returns the current resource
     *                                    utilization for that Switch
     */
    public SwitchResourceStats(Switch networkElement,
            Function<Switch, NetworkElementStateEntry> resourceUtilizationFunction) {
        super(networkElement, resourceUtilizationFunction);
    }

    /**
     * {@inheritDoc}.
     * The method is automatically called when the Switch processing is updated.
     * 
     * @param time {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean add(final double time) {
        return super.add(time);
    }

}
