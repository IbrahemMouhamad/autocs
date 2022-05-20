/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.core.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.autocs.core.serializer.ProviderSerializer;
import org.autocs.core.service.EntityService;

import static org.autocs.core.utils.HumanizingUtils.BinaryMBHumanizing;
import static org.autocs.core.utils.HumanizingUtils.MBHumanizing;

/**
 * A java class to represent a cloud services provider
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)
@JsonSerialize(using = ProviderSerializer.class)
public class Provider extends Entity {
    private int datacentersNumber;
    private int hostsNumber;
    private long totalPES;
    private long totalMIPS;
    private long totalRAM;
    private long totalStorage;
    List<DatacenterModel> datacenters = new ArrayList<>();

    public int getDatacentersNumber() {
        return datacentersNumber;
    }

    public void setDatacentersNumber(int datacentersNumber) {
        this.datacentersNumber = datacentersNumber;
    }

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

    public List<DatacenterModel> getDatacenters() {
        return datacenters;
    }

    public void setDatacenters(List<DatacenterModel> datacenters) {
        this.datacenters = datacenters;
    }

    public void loadDatacenter(EntityService<DatacenterModel> entityService) {
        for (int i = 0; i < this.getDatacenters().size(); i++) {
            DatacenterModel datacenter = entityService.getById("datacenters", this.getDatacenters().get(i).getId());
            datacenter.setAmount(this.getDatacenters().get(i).getAmount());
            // collect statistics
            this.collectStatistics(datacenter);
            this.getDatacenters().set(i, datacenter);
        }
        this.loadStatistics();
    }

    private void collectStatistics(DatacenterModel datacenter) {
        this.setDatacentersNumber(this.getDatacentersNumber() + datacenter.getAmount());
        this.setHostsNumber(this.getHostsNumber()
                + (datacenter.getAmount() * datacenter.getHostsNumber()));
        this.setTotalPES(this.getTotalPES()
                + (datacenter.getAmount() * datacenter.getTotalPES()));
        this.setTotalMIPS(this.getTotalMIPS()
                + (datacenter.getAmount() * datacenter.getTotalMIPS()));
        this.setTotalRAM(this.getTotalRAM()
                + (datacenter.getAmount() * datacenter.getTotalRAM()));
        this.setTotalStorage(this.getTotalStorage()
                + (datacenter.getAmount() * datacenter.getTotalStorage()));
    }

    private void loadStatistics() {
        this.getStatistics().put("Datacenter Number", "" + this.getDatacentersNumber());
        this.getStatistics().put("Hosts Number", "" + this.getHostsNumber());
        this.getStatistics().put("Processors Number", "" + this.getTotalPES());
        this.getStatistics().put("Total mips", "" + this.getTotalMIPS());
        this.getStatistics().put("Total ram", "" + BinaryMBHumanizing(this.getTotalRAM()));
        this.getStatistics().put("Total Storage", "" + MBHumanizing(this.getTotalStorage()));
    }

}
