/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.data.networkelement.resources;

/**
 * An interface that enables network elements ({@link Switch}s)
 * to enable the computation of statistics for its resource utilization.
 * Since that computation may be computationally complex and increase memory
 * consumption,
 * you have to explicitly enable that by calling
 * {@link #enableUtilizationStats()}.
 *
 * @param <T> the class in which resource utilization will be computed and
 *            stored
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public interface NetworkElementStatsComputer<T extends NetworkElementStats> {
    /**
     * <p>
     * <b>WARNING:</b> You need to enable the data collection and computation of
     * statistics
     * by calling {@link #enableUtilizationStats()}.
     * </p>
     *
     * <p>
     * The time interval in which utilization is collected is defined
     * by the {@link Datacenter#getSchedulingInterval()}.
     * </p>
     *
     * @return
     */
    T getNetworkElementStats();

    /**
     * Enables the data collection and computation of utilization statistics.
     * 
     * @see #getActivePortsStats()
     */
    void enableUtilizationStats();
}
