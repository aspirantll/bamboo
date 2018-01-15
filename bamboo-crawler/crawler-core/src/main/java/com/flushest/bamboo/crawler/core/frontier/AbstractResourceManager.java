package com.flushest.bamboo.crawler.core.frontier;

import com.flushest.bamboo.crawler.core.storage.QueueStorage;
import com.flushest.bamboo.framework.extension.ExtensionLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
public abstract class AbstractResourceManager<T> implements ResourceManager<T> {

    protected Map<String, QueueStorage<T>> queueMap;

    public AbstractResourceManager() {
        queueMap = new HashMap<>();
    }

    protected QueueStorage<T> getQueueStorage(String key) {
        QueueStorage<T> queue = queueMap.get(key);
        if(queue == null) {
            synchronized (this) {
                if (queue == null) {
                    ExtensionLoader<QueueStorage> extensionLoader = ExtensionLoader.getExtensionLoader(QueueStorage.class);
                    queue = extensionLoader.getExtension("*");
                    queueMap.put(key, queue);
                }
            }
        }
        return queue;
    }

    @Override
    public T accept(String key) throws InterruptedException {
        QueueStorage<T> queue = getQueueStorage(key);
        return queue.get();
    }

    @Override
    public boolean offer(String key, T t) {
        QueueStorage<T> queue = getQueueStorage(key);
        return queue.put(t);
    }




}
