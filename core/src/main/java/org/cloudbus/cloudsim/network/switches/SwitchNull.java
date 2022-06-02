/*
 * CloudSim Plus: A modern, highly-extensible and easier-to-use Framework for
 * Modeling and Simulation of Cloud Computing Infrastructures and Services.
 * http://cloudsimplus.org
 *
 *     Copyright (C) 2015-2021 Universidade da Beira Interior (UBI, Portugal) and
 *     the Instituto Federal de Educação Ciência e Tecnologia do Tocantins (IFTO, Brazil).
 *
 *     This file is part of CloudSim Plus.
 *
 *     CloudSim Plus is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     CloudSim Plus is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with CloudSim Plus. If not, see <http://www.gnu.org/licenses/>.
 */
package org.cloudbus.cloudsim.network.switches;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSimTag;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEntityNullBase;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.network.HostPacket;
import org.autocs.sdn.control.tables.ForwardingTable;
import org.autocs.sdn.control.tables.RoutingTable;
import org.autocs.sdn.data.networkelement.resources.NetworkElementStateEntry;
import org.autocs.sdn.data.networkelement.switches.SwitchResourceStats;
import org.autocs.sdn.power.models.PowerModelSwitch;

import java.util.Collections;
import java.util.List;

/**
 * A class that implements the Null Object Design Pattern for {@link Switch}
 * class.
 *
 * @author Manoel Campos da Silva Filho
 * @author Ibrahem Mouhamad
 * @since AutoCS Core Package 1.0.0
 * @see Switch#NULL
 */
final class SwitchNull implements Switch, SimEntityNullBase {
    private static final NetworkDatacenter DATACENTER = new NetworkDatacenter(Simulation.NULL, Collections.emptyList(),
            VmAllocationPolicy.NULL);

    @Override
    public double downlinkTransferDelay(HostPacket packet, int simultaneousPackets) {
        return 0;
    }

    @Override
    public double uplinkTransferDelay(HostPacket packet, int simultaneousPackets) {
        return 0;
    }

    @Override
    public double getUplinkBandwidth() {
        return 0;
    }

    @Override
    public void setUplinkBandwidth(double uplinkBandwidth) {
        /**/}

    @Override
    public double getDownlinkBandwidth() {
        return 0;
    }

    @Override
    public void setDownlinkBandwidth(double downlinkBandwidth) {
        /**/}

    @Override
    public int getPorts() {
        return 0;
    }

    @Override
    public void setPorts(int ports) {
        /**/}

    @Override
    public double getSwitchingDelay() {
        return 0;
    }

    @Override
    public void setSwitchingDelay(double switchingDelay) {
        /**/}

    @Override
    public List<Switch> getUplinkSwitches() {
        return Collections.emptyList();
    }

    @Override
    public List<Switch> getDownlinkSwitches() {
        return Collections.emptyList();
    }

    @Override
    public NetworkDatacenter getDatacenter() {
        return DATACENTER;
    }

    @Override
    public void setDatacenter(NetworkDatacenter datacenter) {
        /**/}

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int compareTo(SimEntity entity) {
        return 0;
    }

    @Override
    public PowerModelSwitch getPowerModel() {
        return PowerModelSwitch.NULL;
    }

    @Override
    public void setPowerModel(PowerModelSwitch powerModel) {
        /**/}

    @Override
    public void enableStateHistory() {
        /**/}

    @Override
    public void disableStateHistory() {
        /**/}

    @Override
    public boolean isStateHistoryEnabled() {
        return false;
    }

    @Override
    public List<NetworkElementStateEntry> getStateHistory() {
        return Collections.emptyList();
    }

    @Override
    public void enableUtilizationStats() {
        /**/}

    @Override
    public double getStartTime() {
        return 0;
    }

    @Override
    public double getShutdownTime() {
        return 0;
    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public SimEntity setState(State state) {
        return null;
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Simulation getSimulation() {
        return null;
    }

    @Override
    public SimEntity setSimulation(Simulation simulation) {
        return null;
    }

    @Override
    public void processEvent(SimEvent evt) {
    }

    @Override
    public boolean schedule(SimEvent evt) {
        return false;
    }

    @Override
    public boolean schedule(double delay, CloudSimTag tag, Object data) {
        return false;
    }

    @Override
    public boolean schedule(double delay, CloudSimTag tag) {
        return false;
    }

    @Override
    public boolean schedule(SimEntity dest, double delay, CloudSimTag tag, Object data) {
        return false;
    }

    @Override
    public boolean schedule(SimEntity dest, double delay, CloudSimTag tag) {
        return false;
    }

    @Override
    public boolean schedule(CloudSimTag tag, Object data) {
        return false;
    }

    @Override
    public void run() {

    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public void shutdown() {
    }

    @Override
    public SimEntity setName(String newName) throws IllegalArgumentException {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public RoutingTable getRoutingTable() {
        return null;
    }

    @Override
    public ForwardingTable getForwardingTable() {
        return null;
    }

    @Override
    public double getNetworkElementIdleInterval() {
        return 0;
    }

    @Override
    public double getLastBusyTime() {
        return 0;
    }

    @Override
    public boolean isNetworkElementIdle() {
        return false;
    }

    @Override
    public SwitchResourceStats getNetworkElementStats() {
        return null;
    }

    @Override
    public NetworkElementStateEntry getSateEntrySoFar() {
        return null;
    }
}
