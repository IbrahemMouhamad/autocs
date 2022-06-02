package org.autocs.sdn.examples.applications;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.network.NetworkVm;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

/**
 * An example of a Workflow Distributed Application that is compounded by
 * 3 {@link NetworkCloudlet}, each one having different stages
 * such as sending, receiving or processing data.
 *
 * @author Saurabh Kumar Garg
 * @author Rajkumar Buyya
 * @author Manoel Campos da Silva Filho
 */
public class NetworkVmsExampleWorkflowApp extends NetworkVmExampleAbstract {
    /**
     * Starts the execution of the example.
     *
     * @param args
     * @throws IOException
     * @throws DatabindException
     * @throws StreamWriteException
     */
    public static void main(String[] args) throws StreamWriteException, DatabindException, IOException {
        new NetworkVmsExampleWorkflowApp();
    }

    private NetworkVmsExampleWorkflowApp() throws StreamWriteException, DatabindException, IOException {
        super();
    }

    @Override
    public List<NetworkCloudlet> createNetworkCloudlets(DatacenterBroker broker) {
        final NetworkCloudlet networkCloudletList[] = new NetworkCloudlet[3];
        final List<NetworkVm> selectedVms = randomlySelectVmsForApp(networkCloudletList.length);

        for (int i = 0; i < 3; i++) {
            networkCloudletList[i] = createNetworkCloudlet(selectedVms.get(i), broker);
            System.out.printf(
                    "Created NetworkCloudlet %d for Application %d%n",
                    networkCloudletList[i].getId(), broker.getId());
        }
        networkCloudletList[0].setVm(this.vmList.get(0));
        networkCloudletList[1].setVm(this.vmList.get(1));
        networkCloudletList[2].setVm(this.vmList.get(3));

        // NetworkCloudlet 0 Tasks
        addSendTask(networkCloudletList[0], networkCloudletList[1], 1);
        addExecutionTask(networkCloudletList[0]);
        addReceiveTask(networkCloudletList[0], networkCloudletList[2]);

        // NetworkCloudlet 1 Tasks
        // addExecutionTask(networkCloudletList[1]);
        addReceiveTask(networkCloudletList[1], networkCloudletList[0]);
        addExecutionTask(networkCloudletList[1]);
        addSendTask(networkCloudletList[1], networkCloudletList[2], 2);

        // NetworkCloudlet 2 Tasks
        addExecutionTask(networkCloudletList[2]);
        addReceiveTask(networkCloudletList[2], networkCloudletList[1]);
        addSendTask(networkCloudletList[2], networkCloudletList[0], 3);

        return Arrays.asList(networkCloudletList);
    }

    /**
     * Creates a {@link NetworkCloudlet}.
     *
     * @param vm     the VM that will run the created {@link NetworkCloudlet)
     * @param broker the broker that will own the create NetworkCloudlet
     * @return
     */
    private NetworkCloudlet createNetworkCloudlet(NetworkVm vm, DatacenterBroker broker) {
        final UtilizationModel utilizationModel = new UtilizationModelFull();
        final var cloudlet = new NetworkCloudlet(1, CLOUDLET_PES);
        cloudlet
                .setMemory(CLOUDLET_RAM)
                .setFileSize(CLOUDLET_FILE_SIZE)
                .setOutputSize(CLOUDLET_OUTPUT_SIZE)
                .setUtilizationModel(utilizationModel)
                .setVm(vm)
                .setBroker(vm.getBroker());

        return cloudlet;
    }
}
