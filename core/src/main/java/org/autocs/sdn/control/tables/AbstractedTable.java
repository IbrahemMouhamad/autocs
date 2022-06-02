/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.control.tables;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class for implementing a flow control table
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

public abstract class AbstractedTable<K, V> implements Table<K, V> {

    /**
     * Map that represent the actual table, which will contain the flow rules
     */
    private final Map<K, V> table;

    public AbstractedTable() {
        this.table = new HashMap<K, V>();
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        this.table.clear();
    }

    /**
     * {@inheritDoc}
     */
    public void removeRule(K key) {
        this.table.remove(key);
    }

    public Map<K, V> getTable() {
        return table;
    }

}
