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

import org.autocs.core.serializer.BrokerSerializer;
import org.autocs.core.service.EntityService;

import static org.autocs.core.utils.HumanizingUtils.BinaryMBHumanizing;
import static org.autocs.core.utils.HumanizingUtils.MBHumanizing;

/**
 * A java class to represent a customer
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_DEFAULT)
@JsonSerialize(using = BrokerSerializer.class)
public class Broker extends Entity {
    // vm statistics
    private int VmsNumber;
    private long totalVmPES;
    private long totalVmMIPS;
    private long totalVmRAM;
    private long totalVmBW;
    // cloudlet statistics
    private int CloudletsNumber;
    private long totalCloudletsPES;
    private long totalCloudletsLength;
    List<Entity> vms = new ArrayList<>();
    List<Entity> cloudlets = new ArrayList<>();

    public int getVmsNumber() {
        return VmsNumber;
    }

    public void setVmsNumber(int vmsNumber) {
        VmsNumber = vmsNumber;
    }

    public long getTotalVmPES() {
        return totalVmPES;
    }

    public void setTotalVmPES(long totalVmPES) {
        this.totalVmPES = totalVmPES;
    }

    public long getTotalVmMIPS() {
        return totalVmMIPS;
    }

    public void setTotalVmMIPS(long totalVmMIPS) {
        this.totalVmMIPS = totalVmMIPS;
    }

    public long getTotalVmRAM() {
        return totalVmRAM;
    }

    public void setTotalVmRAM(long totalVmRAM) {
        this.totalVmRAM = totalVmRAM;
    }

    public long getTotalVmBW() {
        return totalVmBW;
    }

    public void setTotalVmBW(long totalVmBW) {
        this.totalVmBW = totalVmBW;
    }

    public List<Entity> getVms() {
        return vms;
    }

    public void setVms(List<Entity> vms) {
        this.vms = vms;
    }

    public int getCloudletsNumber() {
        return CloudletsNumber;
    }

    public void setCloudletsNumber(int cloudletsNumber) {
        CloudletsNumber = cloudletsNumber;
    }

    public long getTotalCloudletsPES() {
        return totalCloudletsPES;
    }

    public void setTotalCloudletsPES(long totalCloudletsPES) {
        this.totalCloudletsPES = totalCloudletsPES;
    }

    public long getTotalCloudletsLength() {
        return totalCloudletsLength;
    }

    public void setTotalCloudletsLength(long totalCloudletsLength) {
        this.totalCloudletsLength = totalCloudletsLength;
    }

    public List<Entity> getCloudlets() {
        return cloudlets;
    }

    public void setCloudlets(List<Entity> cloudlets) {
        this.cloudlets = cloudlets;
    }

    public void loadVms(EntityService<Entity> entityService) {
        for (int i = 0; i < this.getVms().size(); i++) {
            Entity vm = entityService.getById("vms", this.getVms().get(i).getId());
            vm.setAmount(this.getVms().get(i).getAmount());
            // collect statistics
            this.collectVmStatistics(vm);
            this.getVms().set(i, vm);
        }
        this.loadVmStatistics();
    }

    public void loadCloudlets(EntityService<Entity> entityService) {
        for (int i = 0; i < this.getCloudlets().size(); i++) {
            Entity cloudlet = entityService.getById("cloudlets", this.getCloudlets().get(i).getId());
            cloudlet.setAmount(this.getCloudlets().get(i).getAmount());
            // collect statistics
            this.collectCloudletStatistics(cloudlet);
            this.getCloudlets().set(i, cloudlet);
        }
        this.loadCloudletStatistics();
    }

    private void collectVmStatistics(Entity vm) {
        this.setVmsNumber(this.getVmsNumber() + vm.getAmount());
        this.setTotalVmPES(this.getTotalVmPES()
                + (vm.getAmount() * (Integer) vm.getProperties().get("pes")));
        this.setTotalVmMIPS(this.getTotalVmMIPS()
                + (vm.getAmount() * (Integer) vm.getProperties().get("mips")));
        this.setTotalVmRAM(this.getTotalVmRAM()
                + (vm.getAmount() * (Integer) vm.getProperties().get("ram")));
        this.setTotalVmBW(this.getTotalVmBW()
                + (vm.getAmount() * (Integer) vm.getProperties().get("bw")));
    }

    private void collectCloudletStatistics(Entity cloudlet) {
        this.setCloudletsNumber(this.getCloudletsNumber() + cloudlet.getAmount());
        this.setTotalCloudletsPES(this.getTotalCloudletsPES()
                + (cloudlet.getAmount() * (Integer) cloudlet.getProperties().get("pes")));
        this.setTotalCloudletsLength(this.getTotalCloudletsLength()
                + (cloudlet.getAmount() * (Integer) cloudlet.getProperties().get("length")));
    }

    private void loadVmStatistics() {
        this.getStatistics().put("Vms Number", "" + this.getVmsNumber());
        this.getStatistics().put("Total Vm Processors Number", "" + this.getTotalVmPES());
        this.getStatistics().put("Total Vm mips", "" + this.getTotalVmMIPS());
        this.getStatistics().put("Total Vm ram", "" + BinaryMBHumanizing(this.getTotalVmRAM()));
        this.getStatistics().put("Total Vm Bandwidth", "" + MBHumanizing(this.getTotalVmBW()));
    }

    private void loadCloudletStatistics() {
        this.getStatistics().put("Cloudlets Number", "" + this.getCloudletsNumber());
        this.getStatistics().put("Total Cloudlets Processors Number", "" + this.getTotalCloudletsPES());
        this.getStatistics().put("Total Cloudlets length", "" + this.getTotalCloudletsLength());
    }
}
