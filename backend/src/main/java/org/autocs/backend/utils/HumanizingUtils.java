/*
 * Title:        Core Package
 * Description:  Core package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.backend.utils;

import java.text.DecimalFormat;

/**
 * A java class to provide humanizing utils
 *
 * @author Ibrahem Mouhamad
 * @since Core Package 1.0.0
 */

public class HumanizingUtils {

    /*
     * Convert input which representing binary size in MB to human readable string
     */
    public static String BinaryMBHumanizing(long size) {
        String hrSize = null;
        DecimalFormat dec = new DecimalFormat("0.00");

        double g = size / 1024.0;
        double t = size / 1048576.0;

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else {
            hrSize = dec.format(size).concat(" MB");
        }

        return hrSize;
    }

    /*
     * Convert input which representing storage size in MB to human readable string
     */
    public static String MBHumanizing(long size) {
        String hrSize = null;
        DecimalFormat dec = new DecimalFormat("0.00");

        double g = size / 1000.0;
        double t = size / 1000000.0;

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else {
            hrSize = dec.format(size).concat(" MB");
        }

        return hrSize;
    }

}
