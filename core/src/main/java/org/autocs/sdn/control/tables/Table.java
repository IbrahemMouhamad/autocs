/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.control.tables;

/**
 * Represents a flow control table
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public interface Table<K, V> {

    /**
     * An attribute that implements the Null Object Design Pattern for
     * {@link Table}
     * objects.
     */
    Table NULL = new TableNull();

    /**
     * Clears the table by deleting all rows/rules
     */
    void clear();

    /**
     * Removes a rule from the table
     *
     * @param key
     * @param value
     */
    void removeRule(K key);

    /**
     * Prints the table entries in human readable formate
     */
    void print();
}
