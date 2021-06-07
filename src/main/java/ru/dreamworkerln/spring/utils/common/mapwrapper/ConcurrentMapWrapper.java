package ru.dreamworkerln.spring.utils.common.mapwrapper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Обертка над ConcurrentNavigableMap
 * @param <K>
 * @param <V>
 */
public class ConcurrentMapWrapper<K,V> extends MapWrapper<K, V> {

    public ConcurrentMapWrapper() {
        map = new ConcurrentHashMap<>();
    }
}
