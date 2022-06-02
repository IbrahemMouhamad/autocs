/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.control.network.virtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.vms.network.NetworkVm;;

/**
 * Represents a virtual topology for one user {@link DatacenterBroker}
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public class VirtualTopology {

    /**
     * map of {@link VirtualLink}s identified by its id
     */
    private Map<Long, VirtualLink> flowToVirtualLinks;

    /**
     * map of {@link NetworkVm}s identified by its id
     */
    private Map<Long, NetworkVm> idToNetworkVms;

    private static long linkId = 0;

    public VirtualTopology() {
        flowToVirtualLinks = new HashMap<>();
        this.idToNetworkVms = new HashMap<>();
    }

    /**
     * Adds a virtual link to the topology
     */
    public void addVirtualLink(final NetworkVm src, final NetworkVm dest, final double bw) {
        final long id = ++linkId;
        VirtualLink link = new VirtualLink(src.getId(), dest.getId(), bw, id);
        this.flowToVirtualLinks.put(id, link);
        link.setVirtualTopology(this);
        // add the src an dest {@link NetworkVm}s to idToNetworkVms map
        this.idToNetworkVms.put(src.getId(), src);
        this.idToNetworkVms.put(dest.getId(), dest);
    }

    /**
     * Removes a virtual link from the topology using flow id
     */
    public void removeVirtualLinkByFlowId(final long flowId) {
        this.flowToVirtualLinks.remove(flowId);
    }

    /**
     * Removes a virtual link from the topology using {@link NetworkVm}
     * which may be source or destination
     */
    public void removeAllVirtualLinkByVm(final NetworkVm vm) {
        for (VirtualLink link : getAllLinks()) {
            if (link.getSrcId() == vm.getId() || link.getDstId() == vm.getId()) {
                removeVirtualLinkByFlowId(link.getId());
            }
        }
    }

    /**
     * Gets {@link VirtualLink} using its flow id
     * 
     * @param flowId
     * @return
     */
    public VirtualLink getVirtualLinkByFlowId(final long flowId) {
        return this.flowToVirtualLinks.get(flowId);
    }

    /**
     * Gets {@link NetworkVm} using its id
     * 
     * @param flowId
     * @return
     */
    public NetworkVm getNetworkVmById(final long vmId) {
        return this.idToNetworkVms.get(vmId);
    }

    /**
     * Gets a list of all {@link VirtualLink}s
     *
     * @return
     */
    public List<VirtualLink> getAllLinks() {
        return new ArrayList<VirtualLink>(this.flowToVirtualLinks.values());
    }

    /**
     * Gets a list of all {@link NetworkVm}s
     *
     * @return
     */
    public List<NetworkVm> getAllNetworkVms() {
        return new ArrayList<NetworkVm>(this.idToNetworkVms.values());
    }

}
