// Copyright 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

package org.autocs.sdn.examples.experiment;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyBestFit;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.hosts.Host;

import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.testbeds.ExperimentRunner;

public class BasicPowerAwareExperiment extends BaseExperiment {
    /**
     * Indicates the time (in seconds) the Host takes to start up.
     * Setting a value larger than 0 makes the VM placement to wait for the Host
     * initialization.
     */
    private static final double HOST_START_UP_DELAY = 20;

    /** Indicates the time (in seconds) the Host takes to shut down. */
    private static final double HOST_SHUT_DOWN_DELAY = 10;

    /** Indicates the power (in watts) the Host consumes for starting up. */
    private static final double HOST_START_UP_POWER = 40;

    /** Indicates the power (in watts) the Host consumes for shutting up. */
    private static final double HOST_SHUT_DOWN_POWER = 15;

    /**
     * The deadline (in seconds) after the Host becoming idle that it will be
     * shutdown
     * automatically.
     *
     * @see Host#setIdleShutdownDeadline(double)
     */
    private static final int HOST_IDLE_SECONDS_TO_SHUTDOWN = 20;

    protected BasicPowerAwareExperiment(final int index, final ExperimentRunner runner, final long seed,
            final VmAllocationPolicy vmAllocationPolicy) {
        super(index, runner, seed, vmAllocationPolicy);
    }

    protected BasicPowerAwareExperiment(int index, ExperimentRunner runner, final long seed) {
        this(index, runner, seed, new VmAllocationPolicyBestFit());
    }

    @Override
    protected Host createHost(int id) {
        final Host host = super.createHost(id);

        host.getPowerModel().setStartupDelay(HOST_START_UP_DELAY)
                .setShutDownDelay(HOST_SHUT_DOWN_DELAY)
                .setStartupPower(HOST_START_UP_POWER)
                .setShutDownPower(HOST_SHUT_DOWN_POWER);

        host.setIdleShutdownDeadline(HOST_IDLE_SECONDS_TO_SHUTDOWN);
        host.setActive(false);
        return host;
    }

    @Override
    protected Vm createVm(DatacenterBroker broker, int id) {
        final Vm vm = super.createVm(broker, id);
        vm.enableUtilizationStats();
        return vm;
    }
}
