package com.flushest.bamboo.crawler.core.context;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2018/1/17 0017.
 */
@Data
@Builder
public class TextFile {
    private String fileName;
    private String content;

    public File getFile() {
        File file = new File(fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new BambooRuntimeException(String.format("创建文件%s失败", fileName), e);
            }
        }
        return file;
    }
}
