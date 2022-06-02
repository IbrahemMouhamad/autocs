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

/**
 * Simple power model defining the power consumption of a switch.
 * 
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */
public class PowerModelSwitchSimple extends PowerModelSwitch {
    private final double staticPower;
    private final double powerPerActivePort;

    /**
     * Instantiates a {@link PowerModelSwitchSimple} by specifying its static and
     * power per port usage.
     *
     * @param staticPower        power (in watts) the switch consumes when idle.
     * @param powerPerActivePort power (in watts) the switch consumes for each
     *                           active port.
     */
    public PowerModelSwitchSimple(final double staticPower, final double powerPerActivePort) {
        super();

        this.staticPower = validatePower(staticPower, "staticPower");
        this.powerPerActivePort = validatePower(powerPerActivePort, "powerPerActivePort");
    }

    @Override
    public PowerMeasurement getPowerMeasurement() {
        /*
         * TODO: Add isActive to switch
         * if(!getSwitch().isActive()){
         * return new PowerMeasurement();
         * }
         */

        final double numOfActivePorts = 4;// this.getSwitch().getActivePortsNumber();
        return new PowerMeasurement(staticPower, dynamicPower(numOfActivePorts));
    }

    /**
     * Computes the switch current power usage in Watts (W) at a certain degree of
     * utilization
     *
     * @param numOfActivePorts the number of active ports of the switch.
     * @return the power supply in Watts (W)
     * @throws IllegalArgumentException if numOfActivePorts is not between [0 and
     *                                  {@link Switch.getPorts}]
     */
    public double getPower(final double numOfActivePorts) throws IllegalArgumentException {
        if (numOfActivePorts < 0 || numOfActivePorts > this.getSwitch().getPorts()) {
            throw new IllegalArgumentException(
                    "numOfActivePorts has to be between 0 and the number of ports the switch has");
        }

        return staticPower + dynamicPower(numOfActivePorts);
    }

    /**
     * Computes the dynamic power consumed according to the number of used ports.
     * 
     * @param numOfActivePorts the number of active ports of the switch.
     * @return the dynamic power supply in Watts (W)
     */
    private double dynamicPower(final double numOfActivePorts) {
        return powerPerActivePort * numOfActivePorts;
    }

    /**
     * Gets the static power (in watts) the switch consumes when idle.
     *
     * @return
     */
    public double getStaticPower() {
        return staticPower;
    }

    /**
     * Gets the power (in watts) the switch consumes for each active port.
     * 
     * @return
     */
    public double gePowerPerActivePort() {
        return powerPerActivePort;
    }
}