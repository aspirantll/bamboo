package com.flushest.bamboo.crawler.core.context;

import com.gargoylesoftware.htmlunit.WebWindow;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2018/1/21 0021.
 */
@Setter
@Getter
public class DynamicContext {
    private WebWindow webWindow;

    public DynamicContext(WebWindow webWindow) {
        this.webWindow = webWindow;
    }
}
