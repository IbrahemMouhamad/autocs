/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.autocs.backend.serializer.DatacenterSerializer;
import org.autocs.backend.service.EntityService;

import static org.autocs.backend.utils.HumanizingUtils.BinaryMBHumanizing;
import static org.autocs.backend.utils.HumanizingUtils.MBHumanizing;

/**
 * A java class to represent a datacenter
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)
@JsonSerialize(using = DatacenterSerializer.class)
public class Datacenter extends Entity {
    private int hostsNumber;
    private long totalPES;
    private long totalMIPS;
    private long totalRAM;
    private long totalStorage;
    List<Entity> hosts = new ArrayList<>();

    public int getHostsNumber() {
        return hostsNumber;
    }

    public void setHostsNumber(int hostsNumber) {
        this.hostsNumber = hostsNumber;
    }

    public long getTotalPES() {
        return totalPES;
    }

    public void setTotalPES(long totalPES) {
        this.totalPES = totalPES;
    }

    public long getTotalMIPS() {
        return totalMIPS;
    }

    public void setTotalMIPS(long totalMIPS) {
        this.totalMIPS = totalMIPS;
    }

    public long getTotalRAM() {
        return totalRAM;
    }

    public void setTotalRAM(long totalRAM) {
        this.totalRAM = totalRAM;
    }

    public long getTotalStorage() {
        return totalStorage;
    }

    public void setTotalStorage(long totalStorage) {
        this.totalStorage = totalStorage;
    }

    public List<Entity> getHosts() {
        return hosts;
    }

    public void setHosts(List<Entity> hosts) {
        this.hosts = hosts;
    }

    public void loadHosts(EntityService<Entity> entityService) {
        for (int i = 0; i < this.getHosts().size(); i++) {
            Entity host = entityService.getById("hosts", this.getHosts().get(i).getId());
            host.setAmount(this.getHosts().get(i).getAmount());
            // collect statistics
            this.collectStatistics(host);
            this.getHosts().set(i, host);
        }
        this.loadStatistics();
    }

    private void collectStatistics(Entity host) {
        this.setHostsNumber(this.getHostsNumber() + host.getAmount());
        this.setTotalPES(this.getTotalPES()
                + (host.getAmount() * (Integer) host.getProperties().get("pes")));
        this.setTotalMIPS(this.getTotalMIPS()
                + (host.getAmount() * (Integer) host.getProperties().get("mips")));
        this.setTotalRAM(this.getTotalRAM()
                + (host.getAmount() * (Integer) host.getProperties().get("ram")));
        this.setTotalStorage(this.getTotalStorage()
                + (host.getAmount() * (Integer) host.getProperties().get("storage")));
    }

    private void loadStatistics() {
        this.getStatistics().put("Hosts Number", "" + this.getHostsNumber());
        this.getStatistics().put("Processors Number", "" + this.getTotalPES());
        this.getStatistics().put("Total mips", "" + this.getTotalMIPS());
        this.getStatistics().put("Total ram", "" + BinaryMBHumanizing(this.getTotalRAM()));
        this.getStatistics().put("Total Storage", "" + MBHumanizing(this.getTotalStorage()));
    }
}
