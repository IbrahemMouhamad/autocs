/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.power.models;

import org.cloudbus.cloudsim.power.PowerMeasurement;
import org.cloudbus.cloudsim.network.switches.Switch;

/**
 * A class that implements the Null Object Design Pattern for
 * {@link PowerModelSwitch} objects.
 * 
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */
final class PowerModelSwitchNull extends PowerModelSwitch {

    @Override
    public PowerMeasurement getPowerMeasurement() {
        return new PowerMeasurement();
    }

    @Override
    public Switch getSwitch() {
        return (Switch) Switch.NULL;
    }

    @Override
    public double getPower(double utilizationFraction) throws IllegalArgumentException {
        return 0;
    }
}