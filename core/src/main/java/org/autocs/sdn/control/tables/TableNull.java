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
 * A class that implements the Null Object Design Pattern for {@link Table}
 * class.
 *
 * @author Ibrahem Mouhamad
 * @since AutoCS SDN Package 1.0.0
 */

final class TableNull<K, V> implements Table<K, V> {

    @Override
    public void clear() {
    }

    @Override
    public void removeRule(Object key) {
    }

    @Override
    public void print() {
    }
}
