package ru.dreamworkerln.spring.utils.common.mapwrapper;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Обертка над ConcurrentNavigableMap
 * @param <K>
 * @param <V>
 */

public class ConcurrentNavigableMapWrapper<K extends Comparable<K>, V> extends MapWrapper<K, V> {

    public ConcurrentNavigableMapWrapper() {
        map = new ConcurrentSkipListMap<>();
    }
}
