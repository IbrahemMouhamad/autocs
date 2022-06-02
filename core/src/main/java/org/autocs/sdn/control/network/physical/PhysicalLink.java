/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.control.network.physical;

import java.util.HashMap;
import java.util.Map;

import org.cloudbus.cloudsim.resources.Bandwidth;
import org.cloudbus.cloudsim.network.topologies.TopologicalLink;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.network.switches.Switch;

import org.autocs.sdn.data.networkelement.NetworkElement;
import org.autocs.sdn.control.network.virtual.Channel;

/**
 * Represents a physical link between two {@link NetworkElement}s. It will be
 * used by routing tables in {@link NetworkElement}s to resolve the routing
 * issues.
 * It has an instance of {@link TopologicalLink} to access the other information
 * like latency and bandwidth, to get benefits from the features introduced by
 * NetworkCLoudSim.
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public class PhysicalLink {

    /**
     * source {@link NetworkElement}
     */
    private NetworkElement src;

    /**
     * destination {@link TopologicalLink}
     */
    private NetworkElement dest;

    /**
     * {@link Bandwidth} to be managed for this link.
     * its capacity should be equal to {@link TopologicalLink} bw.
     */
    private Bandwidth bw;

    /**
     * destination {@link NetworkElement}
     */
    private TopologicalLink topologicalLink;

    /**
     * mapping between the flow id and its {@link Channel}
     */
    private Map<Long, Channel> flowToChannelsMapping;

    /**
     * Define new {@link PhysicalLink} using {@link TopologicalLink} information.
     *
     * @param src
     * @param dest
     * @param topologicalLink
     */
    public PhysicalLink(final NetworkElement src, final NetworkElement dest, final TopologicalLink topologicalLink) {
        this(src, dest, (long) topologicalLink.getLinkBw());
        this.topologicalLink = topologicalLink;
    }

    /**
     * Define new {@link PhysicalLink} in absence of a {@link TopologicalLink}, this
     * happens between {@link Switch}es and {@link NetworkHost}es where there is no
     * {@link TopologicalLink}
     *
     * @param src
     * @param dest
     * @param bw
     */
    public PhysicalLink(final NetworkElement src, final NetworkElement dest, final long bw) {
        this.src = src;
        this.dest = dest;
        this.bw = new Bandwidth(bw);
        this.flowToChannelsMapping = new HashMap<>();
    }

    public NetworkElement getSrc() {
        return src;
    }

    public NetworkElement getDest() {
        return dest;
    }

    public TopologicalLink getTopologicalLink() {
        return topologicalLink;
    }

    /**
     * Gets the delay of the link using the {@link TopologicalLink} instance if
     * exists, otherwise return 0;
     *
     * @return the link delay (in seconds)
     */
    public double getTopologicalLinkDelay() {
        return topologicalLink != null ? topologicalLink.getLinkDelay() : 0.0;
    }

    /**
     * Gets the bandwidth of the link using the {@link TopologicalLink} instance
     *
     * @return the bandwidth in Megabits/s.
     */
    public double getTopologicalLinkBw() {
        return topologicalLink != null ? topologicalLink.getLinkBw() : 0.0;
    }

    /**
     * Gets the link {@link Bandwidth}
     *
     * @return
     */
    public Bandwidth getBw() {
        return bw;
    }

    /**
     * Gets {@link Channel} using a flow Id
     */
    public Channel getChannelByFlowId(final long flowId) {
        return flowToChannelsMapping.get(flowId);
    }

    /**
     * Checks if the requested bandwidth is available, then add the new channel
     *
     * @param channel
     * @param flowId
     * @return
     */
    public boolean addChanel(final Channel channel, final long flowId) {
        if (bw.isAmountAvailable((double) channel.getVirtualLink().getRequiredBandwidth())) {
            this.flowToChannelsMapping.put(flowId, channel);
            return true;
        }
        return false;
    }

    public Map<Long, Channel> getFlowToChannelsMapping() {
        return flowToChannelsMapping;
    }
}
