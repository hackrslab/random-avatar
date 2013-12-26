package org.hackrslab.avatar;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Simple LRU Cache Implementation
 *
 * @author Daegeun Kim (dgkim84@gmail.com)
 */
class AvatarCache {
    private int maximumNumberOfItems;
    private int maximumNumberOfBytes;
    private int numberOfBytes;
    private LinkedList<String> keys;
    private WeakReference<Map<String, byte[]>> bytesRef;

    AvatarCache() {
        this(250, 1024*1024);
    }

    AvatarCache(int maximumNumberOfItems, int maximumNumberOfBytes) {
        this.numberOfBytes = 0;
        this.maximumNumberOfItems = maximumNumberOfItems;
        this.maximumNumberOfBytes = maximumNumberOfBytes;
        this.keys = new LinkedList<String>();
        this.bytesRef = new WeakReference<Map<String, byte[]>>(new HashMap<String, byte[]>());
    }

    public synchronized byte[] get(String key) {
        if (bytesRef.get() == null) {
            this.bytesRef = new WeakReference<Map<String, byte[]>>(new HashMap<String, byte[]>());
            this.numberOfBytes = 0;
        }
        Map<String, byte[]> bytes = bytesRef.get();
        if (bytes.containsKey(key)) {
            keys.remove(key);
            keys.addFirst(key);
            return bytes.get(key);
        } else {
            return null;
        }
    }

    public synchronized void put(String key, byte[] value) {
        if (value == null) {
            return;
        }
        if (bytesRef.get() == null) {
            this.bytesRef = new WeakReference<Map<String, byte[]>>(new HashMap<String, byte[]>());
            this.numberOfBytes = 0;
        }
        int estimatedSize = numberOfBytes + value.length - (
                bytesRef.get().containsKey(key) ? bytesRef.get().get(key).length : 0);

        if (maximumNumberOfBytes > 0 && estimatedSize > maximumNumberOfBytes) {
            evict(keys.getLast());
        }
        numberOfBytes += value.length - (bytesRef.get().containsKey(key)
                ? bytesRef.get().get(key).length : 0);

        if (bytesRef.get().containsKey(key)) {
            bytesRef.get().put(key, value);
        } else {
            if (maximumNumberOfItems > 0 && keys.size() >= maximumNumberOfItems) {
                evict(keys.getLast());
            }
            bytesRef.get().put(key, value);
        }
        keys.addFirst(key);
    }

    void evict(String key) {
        numberOfBytes -= bytesRef.get().get(key).length;
        bytesRef.get().remove(key);
        keys.remove(key);
    }
}