package ru.dreamworkerln.spring.utils.common.mapwrapper;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class MapWrapper<K,V> {

    protected ConcurrentMap<K,V> map;

    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public V get(K key) {
        return map.get(key);
    }

    public Collection<V> values() {
        return map.values();
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    public void remove(K key) {
        map.remove(key);
    }

    public void removeAll(Set<K> collection) {
        map.keySet().removeAll(collection);
    }

    public void putAll(Map<K, V> otherMap) {
        map.putAll(otherMap);
    }

    public boolean removeIf(Predicate<Map.Entry<K,V>> filter) {
        return map.entrySet().removeIf(filter);
    }


    public int size() {
        return map.size();
    }

    public ConcurrentMap<K, V> getMap() {
        return map;
    }

    public Stream<Map.Entry<K, V>> getStream() {
        return map.entrySet().stream();
    }



//    @Override
//    @Nonnull
//    public Iterator<ConcurrentMap.Entry<K, V>> iterator() {
//        return map.entrySet().iterator();
//    }

    public void clear() {
        map.clear();
    }
}
