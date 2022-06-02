// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

package org.autocs.sdn.examples.autoscaling;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyBestFit;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyFirstFit;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.autoscaling.HorizontalVmScaling;
import org.cloudsimplus.autoscaling.HorizontalVmScalingSimple;

import org.autocs.sdn.examples.experiment.BasicPowerAwareExperiment;

public class PowerAwareRuleBasedHorizontalVmScalingExperiment extends BasicPowerAwareExperiment {

    protected PowerAwareRuleBasedHorizontalVmScalingExperiment(int index,
            PowerAwareRuleBasedHorizontalVmScalingExperimentRunner runner,
            final long seed,
            final VmAllocationPolicy vmAllocationPolicy) {
        super(index, runner, seed, vmAllocationPolicy);
        this.setBrokersNumber(1);
    }

    protected PowerAwareRuleBasedHorizontalVmScalingExperiment(int index,
            PowerAwareRuleBasedHorizontalVmScalingExperimentRunner runner,
            final long seed) {
        this(index, runner, seed, new VmAllocationPolicyFirstFit());
    }

    @Override
    protected final DatacenterBroker createBroker() {
        final DatacenterBroker datacenter = super.createBroker();
        /**
         * Defines the Vm Destruction Delay Function as a lambda expression
         * so that the broker will wait 10 seconds before destroying any idle VM.
         * By commenting this line, no down scaling will be performed
         * and idle VMs will be destroyed just after all running Cloudlets
         * are finished and there is no waiting Cloudlet.
         *
         * @see DatacenterBroker#setVmDestructionDelayFunction(Function)
         */
        return datacenter.setVmDestructionDelay(20.0);
    }

    protected Vm createVm(DatacenterBroker broker, int id) {
        final Vm vm = super.createVm(broker, id);
        createHorizontalVmScaling(vm);
        return vm;
    }

    /**
     * Creates a {@link HorizontalVmScaling} object for a given VM.
     *
     * @param vm the VM for which the Horizontal Scaling will be created
     * @see #createListOfScalableVms(int)
     */
    private void createHorizontalVmScaling(final Vm vm) {
        final HorizontalVmScaling horizontalScaling = new HorizontalVmScalingSimple();
        horizontalScaling
                .setVmSupplier(() -> super.createVm(vm.getBroker(), nextVmId()))
                .setOverloadPredicate(this::isVmOverloaded);
        vm.setHorizontalScaling(horizontalScaling);
    }

    /**
     * A {@link Predicate} that checks if a given VM is overloaded or not,
     * based on upper CPU utilization threshold.
     * A reference to this method is assigned to each {@link HorizontalVmScaling}
     * created.
     *
     * @param vm the VM to check if it is overloaded
     * @return true if the VM is overloaded, false otherwise
     * @see #createHorizontalVmScaling(Vm)
     */
    private boolean isVmOverloaded(final Vm vm) {
        return vm.getCpuPercentUtilization() > 0.7;
    }
}
