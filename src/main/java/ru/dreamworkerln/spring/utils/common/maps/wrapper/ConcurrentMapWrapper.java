package ru.dreamworkerln.spring.utils.common.maps.wrapper;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class ConcurrentMapWrapper<K,V> implements ConcurrentMap<K,V> {

    protected ConcurrentMap<K,V> map;

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    public void removeAll(Set<K> collection) {
        map.keySet().removeAll(collection);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> otherMap) {
        map.putAll(otherMap);
    }


    public boolean removeIf(Predicate<Map.Entry<K,V>> filter) {
        return map.entrySet().removeIf(filter);
    }

    @Override
    public int size() {
        return map.size();
    }


    public ConcurrentMap<K, V> getMap() {
        return map;
    }


    public Stream<Map.Entry<K, V>> getStream() {
        return map.entrySet().stream();
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return map.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return map.remove(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return map.replace(key, oldValue, newValue);
    }

    @Override
    public V replace(K key, V value) {
        return map.replace(key, value);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    public void clear() {
        map.clear();
    }

    //    @Override
//    @Nonnull
//    public Iterator<ConcurrentMap.Entry<K, V>> iterator() {
//        return map.entrySet().iterator();
//    }
}
