//--------------------------------------------------------------------------
// Copyright (c) 2010-2020, En.dennisit or Cn.苏若年
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
// Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
// Neither the name of the dennisit nor the names of its contributors
// may be used to endorse or promote products derived from this software
// without specific prior written permission.
// Author: dennisit@163.com | dobby | 苏若年
//--------------------------------------------------------------------------
package com.plugin.hash;

/**
 * Description:
 * copy from http://blog.csdn.net/wuhuan_wp/article/details/7010071
 * @author dennisit@163.com
 * @version 1.0
 */
import com.plugin.hash.murmur.MurmurHash;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Sharding<S extends ShardingInfo> {

    //S 类封装了机器节点的信息 ，如name、password、ip、port、weight等

    private TreeMap<Long, S> nodes; // 虚拟节点
    private List<S> shards;         // 真实机器节点

    public Sharding(List<S> shards) {
        super();
        this.shards = shards;
        init();
    }

    /**
     *  初始化一致性hash环
     */
    private void init() {
        nodes = new TreeMap<Long, S>();
        // 每个真实机器节点都需要关联虚拟节点
        for (int i = 0; i != shards.size(); i++) {
            final S shardInfo = shards.get(i);
            // 权重即为每个节点上关联的虚节点数目
            for (int n = 0; n < shardInfo.getWeight(); n++) {
                nodes.put(MurmurHashHold.murmurHash.hash("SHARD-" + i + "-NODE-" + n), shardInfo);
            }
        }
    }

    /**
     * 获取所有的节点集合
     * @return
     */
    public List<S> getShardInfos() {
        return shards;
    }

    /**
     * 返回该虚拟节点对应的真实机器节点的信息
     * @param key
     * @return
     */
    public S getShardInfo(String key) {
        SortedMap<Long, S> tail = nodes.tailMap(MurmurHashHold.murmurHash.hash(key));    // 沿环的顺时针找到一个虚拟节点
        if (tail.size() == 0) {
            return nodes.get(nodes.firstKey());
        }
        return tail.get(tail.firstKey()); // 返回该虚拟节点对应的真实机器节点的信息
    }


    private static class MurmurHashHold{
        private static MurmurHash murmurHash = new MurmurHash();
    }

}

