package org.autocs.sdn.examples.applications;

import org.autocs.sdn.control.controllers.ControllerSimple;
import org.autocs.sdn.control.network.virtual.VirtualTopology;
import org.autocs.sdn.data.networkelement.NetworkElement;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.network.CloudletExecutionTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletReceiveTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletSendTask;
import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.network.switches.AggregateSwitch;
import org.cloudbus.cloudsim.network.switches.EdgeSwitch;
import org.cloudbus.cloudsim.network.switches.RootSwitch;
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology;
import org.cloudbus.cloudsim.network.topologies.NetworkTopology;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.vms.network.NetworkVm;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * A base class for network simulation examples
 * using objects such as{@link NetworkDatacenter},
 * {@link NetworkHost}, {@link NetworkVm} and {@link NetworkCloudlet}.
 *
 * The class simulate applications that are compounded by a list of
 * {@link NetworkCloudlet}.
 *
 * @author Saurabh Kumar Garg
 * @author Rajkumar Buyya
 * @author Manoel Campos da Silva Filho
 */
abstract class NetworkVmExampleAbstract {
    public static final int MAX_VMS_PER_HOST = 2;

    public static final double COST = 3.0; // the cost of using processing in this resource
    public static final double COST_PER_MEM = 0.05; // the cost of using memory in this resource
    public static final double COST_PER_STORAGE = 0.001; // the cost of using storage in this resource
    public static final double COST_PER_BW = 0.0; // the cost of using bw in this resource

    public static final int HOST_MIPS = 1000;
    public static final int HOST_PES = 8;
    public static final int HOST_RAM = 2048; // MEGA
    public static final long HOST_STORAGE = 1000000; // MEGA
    public static final long HOST_BW = 10000;

    public static final int VM_MIPS = 1000;
    public static final long VM_SIZE = 10000; // image size (Megabyte)
    public static final int VM_RAM = 512; // MEGA
    public static final long VM_BW = 1000;
    public static final int VM_PES_NUMBER = HOST_PES / MAX_VMS_PER_HOST;

    /**
     * Number of fictitious applications to create.
     * Each application is just a list of {@link NetworkCloudlet}.
     *
     * @see #appMap
     */
    public static final int APPS_NUMBER = 1;

    public static final int CLOUDLET_PES = VM_PES_NUMBER;
    public static final int TASK_LENGTH = 4000;
    public static final int CLOUDLET_FILE_SIZE = 300;
    public static final int CLOUDLET_OUTPUT_SIZE = 300;
    public static final long CLOUDLET_RAM = 100; // in Megabytes
    private static final long PACKET_DATA_LENGTH_IN_BYTES = 1500;
    private static final long PACKETS_TO_SEND = 5592;
    private static final int SCHEDULING_INTERVAL = 5;

    private final CloudSim simulation;

    protected final List<NetworkVm> vmList;
    private final NetworkDatacenter datacenter1;
    // private final NetworkDatacenter datacenter2;
    private final List<DatacenterBroker> brokerList;
    private RootSwitch rootSwitch;
    AggregateSwitch aggregateSwitch2;
    EdgeSwitch edgeSwitches1;
    protected ObjectMapper objectMapper;

    /**
     * A Map representing a list of cloudlets from different applications.
     * Each key represents the ID of an app that is composed by a list
     * of {@link NetworkCloudlet}.
     */
    private final Map<Integer, List<NetworkCloudlet>> appMap;

    /**
     * Creates, starts, stops the simulation and shows results.
     *
     * @throws IOException
     * @throws DatabindException
     * @throws StreamWriteException
     */
    NetworkVmExampleAbstract() throws StreamWriteException, DatabindException, IOException {
        /*
         * Enables just some level of log messages.
         * Make sure to import org.cloudsimplus.util.Log;
         */
        Log.setLevel(ch.qos.logback.classic.Level.ALL);

        System.out.println("Starting " + getClass().getSimpleName());
        simulation = new CloudSim();
        rootSwitch = null;
        aggregateSwitch2 = null;
        edgeSwitches1 = null;
        this.datacenter1 = createDatacenter();
        this.brokerList = createBrokerForEachApp();
        this.vmList = new ArrayList<>();
        this.appMap = new HashMap<>();

        int appId = -1;
        for (DatacenterBroker broker : this.brokerList) {
            this.vmList.addAll(createAndSubmitVMs(broker));
            this.appMap.put(++appId, createAppAndSubmitToBroker(broker));
        }

        // install the controller
        ControllerSimple controller = new ControllerSimple(this.datacenter1);
        VirtualTopology vTopology = new VirtualTopology();
        vTopology.addVirtualLink(this.vmList.get(0), this.vmList.get(1), 1.024);
        vTopology.addVirtualLink(this.vmList.get(1), this.vmList.get(3), 1.024);
        vTopology.addVirtualLink(this.vmList.get(3), this.vmList.get(0), 1.024);
        this.vmList.get(0).setHost(this.datacenter1.getHost(0));
        this.vmList.get(1).setHost(this.datacenter1.getHost(1));
        this.vmList.get(2).setHost(this.datacenter1.getHost(0));
        this.vmList.get(3).setHost(this.datacenter1.getHost(1));

        controller.deployUserApplication(this.brokerList.get(0), vTopology);

        printRoutingTables(this.datacenter1);
        printForwardingTables(this.datacenter1);

        simulation.start();
        showSimulationResults();
    }

    /**
     * Adds an execution task to the list of tasks of the given
     * {@link NetworkCloudlet}.
     *
     * @param cloudlet the {@link NetworkCloudlet} the task will belong to
     */
    protected static void addExecutionTask(NetworkCloudlet cloudlet) {
        final var task = new CloudletExecutionTask(cloudlet.getTasks().size(), TASK_LENGTH);
        task.setMemory(CLOUDLET_RAM);
        cloudlet.addTask(task);
    }

    private void showSimulationResults() throws StreamWriteException, DatabindException, IOException {
        for (int i = 0; i < APPS_NUMBER; i++) {
            final DatacenterBroker broker = brokerList.get(i);
            List<Cloudlet> newList = broker.getCloudletFinishedList();
            String caption = broker.getName() + " - Application " + broker.getId();
            new CloudletsTableBuilder(newList)
                    .setTitle(caption)
                    .build();
            // System.out.printf(
            // "Number of NetworkCloudlets for Application: %d%n", newList.size());
            for (int j = 0; j < newList.size(); j++) {
                NetworkCloudlet c = (NetworkCloudlet) newList.get(j);
                System.out.printf(
                        "%nInstance number %d: ", c.getId());
                for (int k = 0; k < c.getTasks().size(); k++) {
                    if (c.getTasks().get(k) instanceof CloudletSendTask) {
                        System.out.printf(
                                "sent %d packets (%d bytes) at %,.2f second%n",
                                ((CloudletSendTask) c.getTasks().get(k)).getPacketsToSend().size(),
                                ((CloudletSendTask) c.getTasks().get(k)).getPacketsToSend().size() * 1500,
                                ((CloudletSendTask) c.getTasks().get(k)).getStartTime());
                    }
                    if (c.getTasks().get(k) instanceof CloudletReceiveTask) {
                        System.out.printf(
                                "received %d packets (%d bytes) at %,.2f second%n",
                                ((CloudletReceiveTask) c.getTasks().get(k)).getPacketsReceived().size(),
                                ((CloudletReceiveTask) c.getTasks().get(k)).getPacketsReceived().size() * 1500,
                                ((CloudletReceiveTask) c.getTasks().get(k)).getFinishTime());
                    }
                }
            }
        }

        for (NetworkHost host : datacenter1.getHostList()) {
            System.out.printf("%nHost %d data transferred: %d bytes",
                    host.getId(), host.getTotalDataTransferBytes());
        }
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(new File("switch.json"), edgeSwitches1.getStateHistory());

        System.out.println(getClass().getSimpleName() + " finished!");
    }

    /**
     * Create a {@link DatacenterBroker} for each each list of
     * {@link NetworkCloudlet},
     * representing cloudlets that compose the same application.
     *
     * @return the list of created NetworkDatacenterBroker
     */
    private List<DatacenterBroker> createBrokerForEachApp() {
        final List<DatacenterBroker> list = new ArrayList<>();
        for (int i = 0; i < APPS_NUMBER; i++) {
            list.add(new DatacenterBrokerSimple(simulation));
        }

        return list;
    }

    /**
     * Creates the Datacenter.
     *
     * @return the Datacenter
     */
    protected final NetworkDatacenter createDatacenter() {
        final int hostsNumber = 2;
        final List<NetworkHost> hostList = new ArrayList<>(hostsNumber);
        for (int i = 0; i < hostsNumber; i++) {
            final List<Pe> peList = createPEs(HOST_PES, HOST_MIPS);
            final var host = new NetworkHost(HOST_RAM, HOST_BW, HOST_STORAGE, peList);
            host
                    .setRamProvisioner(new ResourceProvisionerSimple())
                    .setBwProvisioner(new ResourceProvisionerSimple())
                    .setVmScheduler(new VmSchedulerTimeShared());
            hostList.add(host);
        }

        final var dc = new NetworkDatacenter(simulation, hostList);
        dc.setSchedulingInterval(SCHEDULING_INTERVAL);
        dc.getCharacteristics()
                .setCostPerSecond(COST)
                .setCostPerMem(COST_PER_MEM)
                .setCostPerStorage(COST_PER_STORAGE)
                .setCostPerBw(COST_PER_BW);

        createNetwork(dc);
        return dc;
    }

    protected List<Pe> createPEs(final int pesNumber, final long mips) {
        final List<Pe> peList = new ArrayList<>();
        for (int i = 0; i < pesNumber; i++) {
            peList.add(new PeSimple(mips, new PeProvisionerSimple()));
        }

        return peList;
    }

    /**
     * Creates internal Datacenter network.
     *
     * @param datacenter Datacenter where the network will be created
     */
    protected void createNetwork(final NetworkDatacenter datacenter) {
        /*
         * final EdgeSwitch[] edgeSwitches = new EdgeSwitch[1];
         * for (int i = 0; i < edgeSwitches.length; i++) {
         * edgeSwitches[i] = new EdgeSwitch(simulation, datacenter);
         * datacenter.addSwitch(edgeSwitches[i]);
         * }
         *
         * for (NetworkHost host : datacenter.getHostList()) {
         * final int switchNum = getSwitchIndex(host, edgeSwitches[0].getPorts());
         * edgeSwitches[switchNum].connectHost(host);
         * }
         */
        createMultipleLevelNetwork(datacenter);
    }

    void createMultipleLevelNetwork(final NetworkDatacenter datacenter) {
        // Configure network by mapping CloudSim entities to BRITE entities
        final NetworkTopology networkTopology = new BriteNetworkTopology();
        simulation.setNetworkTopology(networkTopology);

        edgeSwitches1 = new EdgeSwitch(simulation, datacenter);
        edgeSwitches1.setDownlinkBandwidth(1000);
        edgeSwitches1.setUplinkBandwidth(10000);
        edgeSwitches1.enableStateHistory();
        edgeSwitches1.enableUtilizationStats();
        final EdgeSwitch edgeSwitches2 = new EdgeSwitch(simulation, datacenter);
        edgeSwitches2.setDownlinkBandwidth(1000);
        edgeSwitches2.setUplinkBandwidth(10000);
        final AggregateSwitch aggregateSwitch1 = new AggregateSwitch(simulation,
                datacenter);
        aggregateSwitch1.setDownlinkBandwidth(10000);
        aggregateSwitch1.setUplinkBandwidth(40000);
        aggregateSwitch2 = new AggregateSwitch(simulation,
                datacenter);
        aggregateSwitch2.setDownlinkBandwidth(10000);
        aggregateSwitch2.setUplinkBandwidth(40000);
        this.rootSwitch = new RootSwitch(simulation, datacenter);
        rootSwitch.setDownlinkBandwidth(40000);
        rootSwitch.setUplinkBandwidth(40000);
        aggregateSwitch2.enableStateHistory();
        aggregateSwitch2.enableUtilizationStats();

        datacenter.addSwitch(edgeSwitches1);
        datacenter.addSwitch(edgeSwitches2);
        datacenter.addSwitch(aggregateSwitch1);
        datacenter.addSwitch(aggregateSwitch2);
        datacenter.addSwitch(rootSwitch);

        edgeSwitches1.connectHost(datacenter.getHostList().get(0));
        edgeSwitches2.connectHost(datacenter.getHostList().get(1));

        networkTopology.addLink(rootSwitch, aggregateSwitch1, 40000.0, 5);
        networkTopology.addLink(aggregateSwitch1, rootSwitch, 40000.0, 0.1);

        networkTopology.addLink(rootSwitch, aggregateSwitch2, 40000.0, 0.1);
        networkTopology.addLink(aggregateSwitch2, rootSwitch, 40000.0, 5);

        networkTopology.addLink(edgeSwitches1, aggregateSwitch1, 10000.0, 0.1);
        networkTopology.addLink(aggregateSwitch1, edgeSwitches1, 10000.0, 0.1);

        networkTopology.addLink(edgeSwitches2, aggregateSwitch2, 10000.0, 0.1);
        networkTopology.addLink(aggregateSwitch2, edgeSwitches2, 10000.0, 0.1);

        // networkTopology.addLink(edgeSwitches2, aggregateSwitch1, 10000.0, 0.1);
        // networkTopology.addLink(aggregateSwitch1, edgeSwitches2, 10000.0, 0.1);

    }

    /**
     * Gets the index of a switch where a Host will be connected,
     * considering the number of ports the switches have.
     * Ensures that each set of N Hosts is connected to the same switch
     * (where N is defined as the number of switch's ports).
     * Since the host ID is long but the switch array index is int,
     * the module operation is used safely convert a long to int
     * For instance, if the id is 2147483648 (higher than the max int value
     * 2147483647),
     * it will be returned 0. For 2147483649 it will be 1 and so on.
     *
     * @param host        the Host to get the index of the switch to connect to
     * @param switchPorts the number of ports (N) the switches where the Host will
     *                    be connected have
     * @return the index of the switch to connect the host
     */
    public static int getSwitchIndex(final NetworkHost host, final int switchPorts) {
        return Math.round(host.getId() % Integer.MAX_VALUE) / switchPorts;
    }

    /**
     * Creates a list of virtual machines in a Datacenter for a given broker
     * and submit the list to the broker.
     *
     * @param broker The broker that will own the created VMs
     * @return the list of created VMs
     */
    protected final List<NetworkVm> createAndSubmitVMs(DatacenterBroker broker) {
        final int vmsNumber = getDatacenterHostList().size() * MAX_VMS_PER_HOST;
        final List<NetworkVm> list = new ArrayList<>();
        for (int i = 0; i < vmsNumber; i++) {
            final var vm = new NetworkVm(i, VM_MIPS, VM_PES_NUMBER);
            vm.setRam(VM_RAM)
                    .setBw(VM_BW)
                    .setSize(VM_SIZE)
                    .setCloudletScheduler(new CloudletSchedulerTimeShared());
            list.add(vm);
        }

        broker.submitVmList(list);
        return list;
    }

    private List<NetworkHost> getDatacenterHostList() {
        return datacenter1.getHostList();
    }

    /**
     * Randomly select a given number of VMs from the list of created VMs,
     * to be used by the NetworkCloudlets of the given application.
     *
     * @param vmsToSelect number of VMs to selected from the existing list of VMs.
     * @return The list of randomly selected VMs
     */
    protected List<NetworkVm> randomlySelectVmsForApp(int vmsToSelect) {
        final List<NetworkVm> list = new ArrayList<>();
        final int existingVms = this.vmList.size();
        final var rand = new UniformDistr(0, existingVms, 5);
        for (int i = 0; i < vmsToSelect; i++) {
            final int vmIndex = (int) rand.sample() % vmList.size();
            final NetworkVm vm = vmList.get(vmIndex);
            list.add(vm);
        }

        return list;
    }

    /**
     * @return List of VMs of all Brokers.
     */
    public List<NetworkVm> getVmList() {
        return vmList;
    }

    public NetworkDatacenter getDatacenter() {
        return datacenter1;
    }

    /**
     * Creates a list of {@link NetworkCloudlet}'s that represents
     * a single application and then submits the created cloudlets to a given
     * Broker.
     *
     * @param broker the broker to submit the list of NetworkCloudlets of the
     *               application
     * @return the list of created {@link NetworkCloudlet}'s
     */
    protected final List<NetworkCloudlet> createAppAndSubmitToBroker(DatacenterBroker broker) {
        final List<NetworkCloudlet> list = createNetworkCloudlets(broker);
        broker.submitCloudletList(list);
        return list;
    }

    /**
     * Creates a list of {@link NetworkCloudlet} that together represents the
     * distributed
     * processes of a given application.
     *
     * @param broker broker to associate the NetworkCloudlets
     * @return the list of create NetworkCloudlets
     */
    protected abstract List<NetworkCloudlet> createNetworkCloudlets(DatacenterBroker broker);

    /**
     * Adds a send task to the list of tasks of the given {@link NetworkCloudlet}.
     *
     * @param sourceCloudlet      the {@link NetworkCloudlet} from which packets
     *                            will be sent
     * @param destinationCloudlet the destination {@link NetworkCloudlet} to send
     *                            packets to
     */
    protected void addSendTask(
            NetworkCloudlet sourceCloudlet,
            NetworkCloudlet destinationCloudlet,
            long flowId) {
        final var task = new CloudletSendTask(sourceCloudlet.getTasks().size());
        task.setMemory(CLOUDLET_RAM);
        sourceCloudlet.addTask(task);
        for (int i = 0; i < PACKETS_TO_SEND; i++) {
            task.addPacket(destinationCloudlet, flowId, PACKET_DATA_LENGTH_IN_BYTES);
        }
    }

    /**
     * Adds a receive task to the list of tasks of the given
     * {@link NetworkCloudlet}.
     *
     * @param cloudlet       the {@link NetworkCloudlet} the task will belong to
     * @param sourceCloudlet the {@link NetworkCloudlet} expected to receive packets
     *                       from
     */
    protected void addReceiveTask(NetworkCloudlet cloudlet, NetworkCloudlet sourceCloudlet) {
        final var task = new CloudletReceiveTask(cloudlet.getTasks().size(), sourceCloudlet.getVm());
        task.setMemory(CLOUDLET_RAM);
        task.setExpectedPacketsToReceive(PACKETS_TO_SEND);
        cloudlet.addTask(task);
    }

    void printRoutingTables(NetworkDatacenter datacenter) {
        for (SimEntity node : datacenter.getSwitchMap()) {
            System.out.println(
                    "Routing Table for node " + node.getName() + " (sim Id: " + node.getId() + ", BriteId: "
                            + ((BriteNetworkTopology) datacenter.getSimulation().getNetworkTopology())
                                    .getSimEntityBriteId(node)
                            + "): ---");
            ((NetworkElement) node).getRoutingTable().print();
        }
        for (NetworkHost node : datacenter.getHostList()) {
            System.out.println(
                    "Routing Table for node " + node.toString() + " (sim Id: " + node.getId()
                            + "): ---");
            ((NetworkElement) node).getRoutingTable().print();
        }
    }

    void printForwardingTables(NetworkDatacenter datacenter) {
        for (SimEntity node : datacenter.getSwitchMap()) {
            System.out.println(
                    "Forwarding Table for node " + node.getName() + " (sim Id: " + node.getId() + ", BriteId: "
                            + ((BriteNetworkTopology) datacenter.getSimulation().getNetworkTopology())
                                    .getSimEntityBriteId(node)
                            + "): ---");
            ((NetworkElement) node).getForwardingTable().print();
        }
        for (NetworkHost node : datacenter.getHostList()) {
            System.out.println(
                    "Forwarding Table for node " + node.toString() + " (sim Id: " + node.getId()
                            + "): ---");
            ((NetworkElement) node).getForwardingTable().print();
        }
    }
}
