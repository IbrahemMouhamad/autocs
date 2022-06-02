/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.power.models;

import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.network.switches.Switch;

import java.util.Objects;

/**
 * Abstract implementation of a switch power model.
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */
public abstract class PowerModelSwitch implements PowerModel {
    /**
     * An attribute that implements the Null Object Design Pattern for
     * {@link PowerModelSwitch}
     * objects.
     */
    public static final PowerModelSwitchNull NULL = new PowerModelSwitchNull();

    private Switch switchEntity;

    /**
     * Checks if a power value (in Watts) is valid.
     * 
     * @param power     the value to validate
     * @param fieldName the name of the field/variable storing the value
     * @return the given power if it's valid
     * @throws IllegalArgumentException when the value is smaller than 1
     */
    protected static double validatePower(final double power, final String fieldName) {
        if (power < 0)
            throw new IllegalArgumentException(fieldName + " cannot be negative.");

        if (power < 1) {
            throw new IllegalArgumentException(
                    fieldName +
                            " must be in watts. A value smaller than 1 may indicate you're trying to give a percentage value instead.");
        }

        return power;
    }

    /**
     * Gets the Switch this PowerModel is collecting power consumption measurements
     * from.
     * 
     * @return
     */
    public Switch getSwitch() {
        return switchEntity;
    }

    /**
     * Sets the Switch this PowerModel will collect power consumption measurements
     * from.
     * 
     * @param switch the Switch to set
     */
    public final void setSwitch(final Switch switchEntity) {
        this.switchEntity = Objects.requireNonNull(switchEntity);
    }

    /**
     * Computes the switch power usage in Watts (W) at a certain degree of
     * utilization.
     * Mainly for backwards compatibility.
     *
     * @param utilizationFraction the utilization percentage of the switch.
     * @return the power supply in Watts (W)
     * @throws IllegalArgumentException if utilizationFraction is not between 0 and
     *                                  switch's ports number
     */
    public abstract double getPower(double utilizationFraction) throws IllegalArgumentException;
}