// Copyright 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

package org.autocs.sdn.examples.experiment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyBestFit;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.distributions.ContinuousDistribution;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.power.models.PowerModelHostSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerCompletelyFair;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.listeners.EventInfo;
import org.cloudsimplus.testbeds.Experiment;
import org.cloudsimplus.testbeds.ExperimentRunner;
import org.cloudsimplus.util.Log;
import ch.qos.logback.classic.Level;

import static java.util.Comparator.comparingDouble;

public class BaseExperiment extends Experiment {

    /**
     * The interval in which the Datacenter will schedule events.
     */
    private static final int SCHEDULING_INTERVAL = 5;

    private static final int HOSTS = 50;
    private static final int HOST_PES = 32;
    private static final int HOST_MIPS = 1000;
    private static final int HOST_RAM = 2048; // in Megabytes
    private static final long HOST_BW = 10_000; // in Megabits/s
    private static final long HOST_STORAGE = 1_000_000; // in Megabytes
    /**
     * Defines the power a Host uses, even if it's idle (in Watts).
     */
    static final double STATIC_POWER = 35;

    /**
     * The max power a Host uses (in Watts).
     */
    static final int MAX_POWER = 50;

    private static final int VMS = 50;
    private static final int VM_PES = 2;
    private static final int VM_MIPS = 1000;
    private static final int VM_RAM = 512; // in Megabytes
    private static final long VM_BW = 1000; // in Megabits/s
    private static final long VM_STORAGE = 10000; // in Megabytes

    private static final int CLOUDLETS = 1;
    private static final int CLOUDLET_PES = 2;
    private static final int CLOUDLET_File_Size = 1024; // in bytes
    private static final int CLOUDLET_OUTPUT_Size = 1024; // in bytes
    /**
     * Different lengths that will be randomly assigned to created Cloudlets.
     */
    private static final long[] CLOUDLET_LENGTHS = { 1000, 2000, 4000, 10000, 16000, 20000, 30000 };
    /**
     * The interval to request the creation of new Cloudlets.
     */
    private static final int CLOUDLETS_CREATION_INTERVAL = SCHEDULING_INTERVAL * 2;

    private final ContinuousDistribution randCloudlet;
    /**
     * The number of new Cloudlets which will be created every
     * {@link CLOUDLETS_CREATION_INTERVAL}
     */
    private int dynamicCloudlets = 500;

    protected BaseExperiment(final int index, final ExperimentRunner runner, final long seed,
            final VmAllocationPolicy vmAllocationPolicy) {
        super(index, runner, seed);
        setHostsNumber(HOSTS);
        setVmsByBrokerFunction(broker -> VMS);
        setVmAllocationPolicySupplier(() -> vmAllocationPolicy);
        this.setBeforeExperimentBuild(this::beforeExperimentBuild);
        this.randCloudlet = new UniformDistr(getSeed());
    }

    protected BaseExperiment(int index, ExperimentRunner runner, final long seed) {
        this(index, runner, seed, new VmAllocationPolicyBestFit());
    }

    /**
     * Method automatically called before the simulation scenario is built. It
     *
     * @param exp the experiment
     */
    private void beforeExperimentBuild(BaseExperiment exp) {
        Log.setLevel(Level.OFF);
        // exp.getSimulation().terminateAt(CLOUDLETS_CREATION_INTERVAL);
        exp.getSimulation().addOnClockTickListener(this::createNewCloudlets);
    }

    @Override
    protected Datacenter createDatacenter(final int index) {
        final DatacenterSimple dc = new DatacenterSimple(this.getSimulation(), createHosts(), newVmAllocationPolicy());
        dc.enableStateHistory();
        dc.setSchedulingInterval(SCHEDULING_INTERVAL);
        return dc;
    }

    @Override
    protected Host createHost(int id) {
        final List<Pe> peList = new ArrayList<>(HOST_PES);
        for (int i = 0; i < HOST_PES; i++) {
            peList.add(new PeSimple(HOST_MIPS, new PeProvisionerSimple()));
        }
        final Host host = new HostSimple(HOST_RAM, HOST_BW, HOST_STORAGE, peList);
        host
                .setRamProvisioner(new ResourceProvisionerSimple())
                .setBwProvisioner(new ResourceProvisionerSimple())
                .setVmScheduler(new VmSchedulerTimeShared())
                .setPowerModel(new PowerModelHostSimple(MAX_POWER, STATIC_POWER));
        host.setId(id);
        host.enableUtilizationStats();
        host.enableStateHistory();
        return host;
    }

    @Override
    protected DatacenterBroker createBroker() {
        return new DatacenterBrokerSimple(getSimulation());
    }

    @Override
    protected Vm createVm(DatacenterBroker broker, int id) {
        final Vm vm = new VmSimple(id, VM_MIPS, VM_PES)
                .setRam(VM_RAM).setBw(VM_BW).setSize(VM_STORAGE)
                .setCloudletScheduler(new CloudletSchedulerCompletelyFair());
        vm.enableUtilizationStats();
        return vm;
    }

    @Override
    protected List<Cloudlet> createCloudlets(DatacenterBroker broker) {
        final List<Cloudlet> cloudletList = new ArrayList<>(CLOUDLETS);
        for (int id = getCloudletList().size(); id < getCloudletList().size() + CLOUDLETS; id++) {
            cloudletList.add(createCloudlet(broker));
        }

        return cloudletList;
    }

    @Override
    protected Cloudlet createCloudlet(DatacenterBroker broker) {
        final int id = nextCloudletId();
        // randomly selects a length for the cloudlet
        final int i = (int) (randCloudlet.sample() * CLOUDLET_LENGTHS.length);
        final long length = CLOUDLET_LENGTHS[i];
        UtilizationModel utilization = new UtilizationModelFull();
        return new CloudletSimple(id, length, CLOUDLET_PES)
                .setFileSize(CLOUDLET_File_Size)
                .setOutputSize(CLOUDLET_OUTPUT_Size)
                .setUtilizationModel(utilization);
    }

    /**
     * Creates new Cloudlets at every {@link #CLOUDLETS_CREATION_INTERVAL} seconds,
     * up to the 50th simulation second.
     * A reference to this method is set as the {@link EventListener}
     * to the {@link Simulation#addOnClockTickListener(EventListener)}.
     * The method is called every time the simulation clock advances.
     *
     * @param info the information about the OnClockTick event that has happened
     */
    private void createNewCloudlets(final EventInfo info) {
        final long time = (long) info.getTime();
        if (time % CLOUDLETS_CREATION_INTERVAL == 0 && time <= 50) {
            final int cloudletsNumber = this.dynamicCloudlets;
            System.out.printf("\t#Creating %d Cloudlets at time %d.%n", cloudletsNumber, time);
            // we have only one broker
            final List<Cloudlet> newCloudlets = new ArrayList<>(cloudletsNumber);
            for (int i = 0; i < cloudletsNumber; i++) {
                final Cloudlet cloudlet = createCloudlet(this.getFirstBroker());
                // cloudletList.add(cloudlet);
                newCloudlets.add(cloudlet);
            }
            this.getFirstBroker().submitCloudletList(newCloudlets);
        }
    }

    /**
     * Get the number of submitted Cloudlets of the first broker
     *
     * @return the wait time average
     */
    public double getSubmittedCloudletsCount() {
        return getFirstBroker().getCloudletSubmittedList().size();
    }

    /**
     * Get the number of created Cloudlets of the first broker
     *
     * @return the wait time average
     */
    public double getCreatedCloudletsCount() {
        return getFirstBroker().getCloudletCreatedList().size();
    }

    /**
     * Get the number of waiting Cloudlets of the first broker
     *
     * @return the wait time average
     */
    public double getWaitingCloudletsCount() {
        return getFirstBroker().getCloudletWaitingList().size();
    }

    /**
     * Gets the number of finished Cloudlets of the first broker
     *
     * @return the wait time average
     */
    public double getFinishedCloudletsCount() {
        return getFirstBroker().getCloudletFinishedList().size();
    }

    /**
     * Gets the total amount of finished load of the first broker (in MIPS)
     * which is equal the sum of finished cloudlets length
     */
    public double getProcessedLoadLength() {
        final SummaryStatistics processedLoadLength = new SummaryStatistics();
        getFirstBroker().getCloudletFinishedList().stream()
                .map(cloudlet -> cloudlet.getLength())
                .forEach(processedLoadLength::addValue);

        return processedLoadLength.getSum();
    }

    /**
     * Get the number of created VMs of the first broker
     *
     * @return the wait time average
     */
    public double getCreatedVMCount() {
        return getFirstBroker().getVmCreatedList().size();
    }

    /**
     * Get the number of failed VMs of the first broker
     *
     * @return the wait time average
     */
    public double getFailedVMCount() {
        return getFirstBroker().getVmFailedList().size();
    }

    /**
     * Get the number of waiting VMs of the first broker
     *
     * @return the wait time average
     */
    public double getWaitingVMCount() {
        return getFirstBroker().getVmWaitingList().size();
    }

    /**
     * Gets the average wait time of cloudlets of the first broker
     *
     * @return the wait time average
     */
    public double getWaitTimeAverage() {
        return getFirstBroker().getCloudletFinishedList().stream().mapToDouble(Cloudlet::getWaitingTime).average()
                .orElse(0);
    }

    /**
     * Computes the Task Completion Time average for all finished Cloudlets on this
     * experiment.
     *
     * @return the Task Completion Time average
     */
    public double getTaskCompletionTimeAverage() {
        final SummaryStatistics cloudletTaskCompletionTime = new SummaryStatistics();
        final DatacenterBroker broker = getBrokerList().stream()
                .findFirst()
                .orElse(DatacenterBroker.NULL);

        broker.getCloudletFinishedList().stream()
                .map(c -> c.getFinishTime() - c.getLastDatacenterArrivalTime())
                .forEach(cloudletTaskCompletionTime::addValue);

        return cloudletTaskCompletionTime.getMean();
    }

    /**
     * Gets CPU utilization mean of all hosts into all Datacenter.
     */
    public double getCpuUtilizationForAllHosts() {
        final SummaryStatistics hostsCpuUtilizationAverage = new SummaryStatistics();
        this.getDatacenterList().forEach(datacenter -> datacenter.getHostList().stream()
                .filter(host -> host.isActive())
                .map(host -> host.getCpuUtilizationStats().getMean()
                        * 100)
                .forEach(hostsCpuUtilizationAverage::addValue));

        return hostsCpuUtilizationAverage.getMean();
    }

    /**
     * Gets CPU utilization mean of all VMs of the first broker
     */
    public double getCpuUtilizationForAllVms() {
        final SummaryStatistics VMsCpuUtilizationAverage = new SummaryStatistics();
        this.getFirstBroker().getVmCreatedList().stream().map(vm -> vm.getCpuUtilizationStats().getMean()
                * 100)
                .forEach(VMsCpuUtilizationAverage::addValue);

        return VMsCpuUtilizationAverage.getMean();
    }

    /**
     * Computes the total power consumption of all hosts
     *
     * @return the power consumption of all hosts
     */
    public double getTotalPowerConsumption() {
        final SummaryStatistics hostsPowerConsumptionTotal = new SummaryStatistics();
        this.getDatacenterList().forEach(datacenter -> datacenter.getHostList().stream()
                .filter(host -> host.isActive())
                .map(host -> host.getPowerModel().getPower(host.getCpuUtilizationStats().getMean()))
                .forEach(hostsPowerConsumptionTotal::addValue));

        return hostsPowerConsumptionTotal.getSum();
    }

    public double getActiveHostsNumber() {
        final SummaryStatistics hostsNumber = new SummaryStatistics();
        this.getDatacenterList().forEach(datacenter -> hostsNumber.addValue(
                datacenter.getHostList().stream()
                        .filter(host -> host.isActive())
                        .collect(Collectors.toList()).size()));

        return hostsNumber.getSum();
    }

    @Override
    public void printResults() {
        // for (int id = 0; id < getBrokerList().size(); id++) {
        // this.printBrokerFinishedCloudlets(getBrokerList().get(id));
        // }
        // System.out.printf("%n# The Task Completion Time Average is: %f%n",
        // getTaskCompletionTimeAverage());
        // System.out.printf("%n# The wait time is: %f%n", getWaitTimeAverage());
    }

    protected void printBrokerFinishedCloudlets(final DatacenterBroker broker) {
        final List<Cloudlet> finishedCloudlets = broker.getCloudletFinishedList();
        final Comparator<Cloudlet> sortByVmId = comparingDouble(c -> c.getVm().getId());
        final Comparator<Cloudlet> sortByStartTime = comparingDouble(Cloudlet::getExecStartTime);
        finishedCloudlets.sort(sortByVmId.thenComparing(sortByStartTime));

        new CloudletsTableBuilder(finishedCloudlets).build();
    }

    protected DatacenterBroker getFirstBroker() {
        return getBrokerList().stream().findFirst().orElse(DatacenterBroker.NULL);
    }

    public void setDynamicCloudlets(final int value) {
        this.dynamicCloudlets = value;
    }
}
