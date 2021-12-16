package ru.dreamworkerln.spring.utils.common.maps;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *  3-step mirror merge:
 *  add new, modify existing, delete deprecated
 */
public class MapMirrorMerge<K,V> {

    /**
     * Three step mirror merge source Map with update Map
     * <br>
     * 1. Add new streams from source to target
     * <br>
     * 2. Update altered values in target (if changed)
     * <br>
     * 3. Delete deprecated values in target
     *
     * @param source source map
     * @param target target map to synchronize with update (make mirror)
     */
    public static <K,V> void merge(Map<K,V> source, Map<K,V> target) {
        mergeWithActions(source, target, (k,v) -> {}, (k,v) -> {}, (k,v) -> {});
    }
    /*
    public static <K,V> void merge(Map<K,V> target, Map<K,V> update) {


        // step 1-2
        for (Map.Entry<K, V> updEntry : update.entrySet()) {

            // 1. Add new streams from update to target
            if(!target.containsKey(updEntry.getKey())) {
                target.put(updEntry.getKey(), updEntry.getValue());
            }
            // 2. Update existed and altered values in target
            else {

                K key = updEntry.getKey();
                V tarValue = target.get(key);
                V updValue = updEntry.getValue();

                if(!tarValue.equals(updValue)) {
                    target.put(key, updValue);
                }
            }

            // 3. Delete deprecated values in target
            target.keySet().removeIf(key -> !update.containsKey(key));
        }
    }
    */


    public static <K,V> void mergeWithActions(Map<K,V> source, Map<K,V> target,
                                              BiConsumer<K,V> onAdd,
                                              BiConsumer<K,V> onUpdate,
                                              BiConsumer<K,V> onDelete) {

        // step 1-2
        for (Map.Entry<K, V> srcEntry : source.entrySet()) {

            K key = srcEntry.getKey();
            V srcValue = srcEntry.getValue();
            V tarValue = target.get(key);

            // 1. Add new streams from source to target
            if(!target.containsKey(key)) {
                target.put(key, srcValue);
                onAdd.accept(key, srcValue);
            }
            // 2. Update existed and altered values in target
            else {
                if(!tarValue.equals(srcValue)) {
                    target.put(key, srcValue);
                    onUpdate.accept(key, srcValue);
                }
            }
        }


        // 3. Delete deprecated values in target
        for (Map.Entry<K, V> entry : target.entrySet()) {
            K key = entry.getKey();

            if(!source.containsKey(key)) {
                V tarValue = entry.getValue();
                target.remove(key);
                onDelete.accept(key, tarValue);
            }
        }
    }
}
