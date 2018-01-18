package com.flushest.bamboo.crawler.core.storage;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.crawler.core.context.TextFile;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2018/1/17 0017.
 */
@Slf4j
public class FileStorage implements Storage<TextFile> {
    @Override
    public TextFile get() throws InterruptedException {
        throw new UnsupportedOperationException("unsupported method : get()");
    }

    @Override
    public synchronized boolean put(TextFile textFile) {
        File file = textFile.getFile();
        try {
            FileOutputStream out = new FileOutputStream(file, true);
            FileChannel channel = out.getChannel();
            FileLock lock = channel.lock();
            if(lock != null) {
                byte[] bytes = textFile.getContent().getBytes(Charset.forName("UTF-8"));
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                buffer.put(bytes);
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
                channel.close();
            }
        } catch (IOException e) {
            throw new BambooRuntimeException("error at store file " + textFile, e);
        }
        return false;
    }
}
