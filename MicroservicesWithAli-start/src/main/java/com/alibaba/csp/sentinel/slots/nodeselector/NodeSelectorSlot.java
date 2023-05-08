//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.alibaba.csp.sentinel.slots.nodeselector;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.ClusterNode;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.spi.Spi;
import java.util.HashMap;
import java.util.Map;

@Spi(
        isSingleton = false,
        order = -10000
)
public class NodeSelectorSlot extends AbstractLinkedProcessorSlot<Object> {
    private volatile Map<String, DefaultNode> map = new HashMap(10);

    public NodeSelectorSlot() {
    }

    public void entry(Context context, ResourceWrapper resourceWrapper, Object obj, int count, boolean prioritized, Object... args) throws Throwable {
        DefaultNode node = (DefaultNode)this.map.get(context.getName());
        if (node == null) {
            synchronized(this) {
                node = (DefaultNode)this.map.get(context.getName());
                if (node == null) {
                    node = new DefaultNode(resourceWrapper, (ClusterNode)null);
                    HashMap<String, DefaultNode> cacheMap = new HashMap(this.map.size());
                    cacheMap.putAll(this.map);
                    cacheMap.put(context.getName(), node);
                    this.map = cacheMap;
                    ((DefaultNode)context.getLastNode()).addChild(node);
                }
            }
        }

        context.setCurNode(node);
        this.fireEntry(context, resourceWrapper, node, count, prioritized, args);
    }

    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        this.fireExit(context, resourceWrapper, count, args);
    }
}
