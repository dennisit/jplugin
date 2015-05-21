/*
 * Copyright (c) 2015, www.jd.com. All rights reserved.
 *
 * 警告：本计算机程序受著作权法和国际公约的保护，未经授权擅自复制或散布本程序的部分或全部、以及其他
 * 任何侵害著作权人权益的行为，将承受严厉的民事和刑事处罚，对已知的违反者将给予法律范围内的全面制裁。
 *
 */
package com.plugin.hash;

/**
 * Description:
 * copy from http://blog.csdn.net/wuhuan_wp/article/details/7010071
 * @author pudongping
 * @version 1.0.1
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

