package com.lsstop.loadbalancer;

import com.lsstop.entity.URL;

import java.util.List;
import java.util.UUID;

/**
 * 一致性哈希
 *
 * @author lss
 * @date 2022/09/18
 */
public class HashBalance implements LoadBalance {

    private final int HASHCODE;

    public HashBalance() {
        int hash = UUID.randomUUID().hashCode();
        hash = hash < 0 ? Math.abs(hash) : hash;
        HASHCODE = (hash >> 16) & hash;
    }

    @Override
    public URL select(List<URL> list) {
        return list.get(HASHCODE % list.size());
    }
}
