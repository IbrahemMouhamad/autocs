/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.data.networkelement.resources;

import org.autocs.sdn.data.networkelement.NetworkElement;

import java.util.Objects;
import java.util.function.Function;

/**
 * A base class for computing statistics about {@link Resource} utilization
 * for a given network element (Switch). Such a resource can be, for instance,
 * Port or BW.
 *
 * @param <T> The kind of machine to collect resource utilization statistics
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public class NetworkElementStats<T extends NetworkElement> {
    private final Function<T, NetworkElementStateEntry> resourcesUtilizationFunction;
    private final T networkElement;
    private final NetworkElementStateSummary stats;
    private double previousTime;
    private NetworkElementStateEntry previousUtilization;

    /**
     * Creates a NetworkResourceStats to collect resource utilization statistics.
     *
     * @param networkElement               the network element where the statistics
     *                                     will be collected (which can be a Switch)
     * @param resourcesUtilizationFunction a {@link Function} that receives a
     *                                     network
     *                                     element
     *                                     and returns the current resources
     *                                     utilization for that machine, see
     *                                     {@link NetworkElementStateEntry}
     */
    protected NetworkElementStats(final T networkElement,
            final Function<T, NetworkElementStateEntry> resourcesUtilizationFunction) {
        this.resourcesUtilizationFunction = Objects.requireNonNull(resourcesUtilizationFunction);
        this.networkElement = Objects.requireNonNull(networkElement);
        this.stats = new NetworkElementStateSummary();
    }

    /**
     * Collects the current resource utilization
     * for the given time to the statistics.
     *
     * @param time current simulation time
     * @return true if data was collected, false otherwise (meaning it's not time to
     *         collect data).
     */
    public boolean add(final double time) {
        try {
            if (isNotTimeToAddHistory(time)) {
                return false;
            }

            final NetworkElementStateEntry utilization = resourcesUtilizationFunction.apply(networkElement);
            /*
             * Check if one of {@link NetworkElementStateEntry} meets the following
             * conditions:
             * If (i) the previous utilization is not zero and the current utilization is
             * zero
             * and (ii) those values don't change, it means the network element has finished
             * and this utilization must not be collected.
             * If that happens, it may reduce accuracy of the utilization mean.
             * For instance, if a network element uses 100% of a resource all the time,
             * when it finishes, the utilization will be zero.
             * If that utilization is collected, the mean won't be 100% anymore.
             */
            if ((!isZero(previousUtilization) && isZero(utilization))
                    || (networkElement.isNetworkElementIdle() && largerThanZero(previousUtilization))) {
                this.previousUtilization = utilization;
                return false;
            }

            this.stats.addValue(utilization.upChannels(), utilization.upload(),
                    utilization.downChannels(), utilization.download());
            this.previousUtilization = utilization;
            return true;
        } finally {
            this.previousTime = (int) time;
        }
    }

    private boolean isZero(NetworkElementStateEntry utilization) {
        if (utilization != null && utilization.upload() == 0 && utilization.download() == 0) {
            return true;
        }
        return false;
    }

    private boolean largerThanZero(NetworkElementStateEntry utilization) {
        if (utilization != null && utilization.upload() > 0 && utilization.download() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Gets the minimum upload
     *
     * @return
     */
    public double getMinUpload() {
        return stats.getUpload().getMin();
    }

    /**
     * Gets the maximum upload
     *
     * @return
     */
    public double getMaxUpload() {
        return stats.getUpload().getMax();
    }

    /**
     * Gets the average upload
     *
     * @return
     */
    public double getMeanUpload() {
        return stats.getUpload().getMean();
    }

    /**
     * Gets the Standard Deviation of resource utilization percentage (from 0 to 1).
     *
     * @return
     */
    public double getUploadStandardDeviation() {
        return stats.getUpload().getStandardDeviation();
    }

    /**
     * Gets the (sample) variance of resource utilization percentage (from 0 to 1).
     *
     * @return
     */
    public double getUploadVariance() {
        return stats.getUpload().getVariance();
    }

    /**
     * Gets the number of collected samples.
     *
     * @return
     */
    public double count() {
        return stats.getUpChannels().getN();
    }

    /**
     * Indicates if no resource utilization sample was collected.
     *
     * @return
     */
    public boolean isEmpty() {
        return count() == 0;
    }

    /**
     * Checks if it isn't time to add a value to the utilization history.
     * The utilization history is not updated in any one of the following conditions
     * is met:
     * <ul>
     * <li>the simulation clock was not changed yet;</li>
     * <li>the time passed is smaller than one second;</li>
     * <li>the floor time is equal to the previous time and Switch is not idle.</li>
     * </ul>
     *
     * <p>
     * If the time is smaller than one second and the Switch became idle,
     * the history will be added so that we know what is the resource
     * utilization when the Switch became idle.
     * This way, we can see clearly in the history when the VM was busy
     * and when it became idle.
     * </p>
     *
     * <p>
     * If the floor time is equal to the previous time,
     * that means not even a second has passed. This way,
     * that utilization will not be stored.
     * </p>
     *
     * @param time the current simulation time
     * @return true if it's time to add utilization history, false otherwise
     */
    protected final boolean isNotTimeToAddHistory(final double time) {
        return time <= 0 ||
                isElapsedTimeSmall(time);
    }

    protected final boolean isElapsedTimeSmall(final double time) {
        return time - previousTime < 1;
    }

    protected final boolean isNotEntireSecondElapsed(final double time) {
        return Math.floor(time) == previousTime;
    }

    protected T getNetworkElement() {
        return networkElement;
    }

    /**
     * Gets the previous time that resource statistics were computed.
     *
     * @return
     */
    protected double getPreviousTime() {
        return previousTime;
    }

}
