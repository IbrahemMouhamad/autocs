// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

package org.autocs.sdn.examples.autoscaling;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyBestFit;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.resources.Processor;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.autoscaling.VerticalVmScalingSimple;
import org.cloudsimplus.autoscaling.resources.ResourceScalingGradual;

import org.autocs.sdn.examples.experiment.BaseExperiment;

public class RuleBasedVerticalVmScalingExperiment extends BaseExperiment {

    protected RuleBasedVerticalVmScalingExperiment(int index,
            RuleBasedVerticalVmScalingExperimentRunner runner,
            final long seed,
            final VmAllocationPolicy vmAllocationPolicy) {
        super(index, runner, seed, vmAllocationPolicy);
        this.setBrokersNumber(1);
    }

    protected RuleBasedVerticalVmScalingExperiment(int index,
            RuleBasedVerticalVmScalingExperimentRunner runner,
            final long seed) {
        super(index, runner, seed, new VmAllocationPolicyBestFit());
    }

    protected Vm createVm(DatacenterBroker broker, int id) {
        final Vm vm = super.createVm(broker, id);
        createVerticalPeScaling(vm);
        return vm;
    }

    /**
     * Creates a {@link VerticalVmScaling} for scaling VM's CPU when it's under or
     * overloaded.
     *
     * <p>
     * Realize the lower and upper thresholds are defined inside this method by
     * using
     * references to the methods {@link #lowerCpuUtilizationThreshold(Vm)}
     * and {@link #upperCpuUtilizationThreshold(Vm)}.
     * These methods enable defining thresholds in a dynamic way
     * and even different thresholds for distinct VMs.
     * Therefore, it's a powerful mechanism.
     * </p>
     *
     *
     * @see #createVm(DatacenterBroker, int)
     */
    private void createVerticalPeScaling(final Vm vm) {
        // The percentage in which the number of PEs has to be scaled
        final double scalingFactor = 0.1;
        VerticalVmScalingSimple verticalCpuScaling = new VerticalVmScalingSimple(Processor.class, scalingFactor);
        verticalCpuScaling.setResourceScaling(new ResourceScalingGradual());

        /*
         * By uncommenting the line below, instead of gradually
         * increasing or decreasing the number of PEs, when the scaling object detects
         * the CPU usage is above or below the defined thresholds,
         * it will automatically calculate the number of PEs to add/remove to
         * move the VM from the over or under load condition.
         */
        // verticalCpuScaling.setResourceScaling(new ResourceScalingInstantaneous());

        /**
         * Different from the commented line above, the line below implements a
         * ResourceScaling using a Lambda Expression.
         * It is just an example which scales the resource twice the amount defined by
         * the scaling factor
         * defined in the constructor.
         *
         * Realize that if the setResourceScaling method is not called, a
         * ResourceScalingGradual will be used,
         * which scales the resource according to the scaling factor.
         * The lower and upper thresholds after this line can also be defined using a
         * Lambda Expression.
         *
         * So, here we are defining our own {@link ResourceScaling} instead of
         * using the available ones such as the {@link ResourceScalingGradual}
         * or {@link ResourceScalingInstantaneous}.
         */
        verticalCpuScaling.setResourceScaling(vs -> 2 * vs.getScalingFactor() * vs.getAllocatedResource());

        verticalCpuScaling.setLowerThresholdFunction(this::lowerCpuUtilizationThreshold);
        verticalCpuScaling.setUpperThresholdFunction(this::upperCpuUtilizationThreshold);

        vm.setPeVerticalScaling(verticalCpuScaling);
    }

    /**
     * Defines the minimum CPU utilization percentage that indicates a Vm is
     * under loaded.
     * This function is using a statically defined threshold, but it would be
     * defined
     * a dynamic threshold based on any condition.
     * A reference to this method is assigned to each Vertical VM Scaling created.
     *
     * @param vm the VM to check if its CPU is under loaded.
     *           <b>The parameter is not being used internally, which means the same
     *           threshold is used for any Vm.</b>
     * @return the lower CPU utilization threshold
     * @see #createVerticalPeScaling()
     */
    private double lowerCpuUtilizationThreshold(Vm vm) {
        return 0.4;
    }

    /**
     * Defines the maximum CPU utilization percentage that indicates a Vm is
     * overloaded.
     * This function is using a statically defined threshold, but it would be
     * defined
     * a dynamic threshold based on any condition.
     * A reference to this method is assigned to each Vertical VM Scaling created.
     *
     * @param vm the VM to check if its CPU is overloaded.
     *           The parameter is not being used internally, that means the same
     *           threshold is used for any Vm.
     * @return the upper CPU utilization threshold
     * @see #createVerticalPeScaling()
     */
    private double upperCpuUtilizationThreshold(Vm vm) {
        return 0.8;
    }
}
