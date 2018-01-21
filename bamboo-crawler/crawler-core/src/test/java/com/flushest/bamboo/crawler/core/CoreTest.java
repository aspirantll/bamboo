package com.flushest.bamboo.crawler.core;

import com.flushest.bamboo.crawler.core.chain.BinaryTreeNode;
import com.flushest.bamboo.crawler.core.chain.Chain;
import com.flushest.bamboo.crawler.core.chain.Task;
import com.flushest.bamboo.crawler.core.context.CrawlConfig;
import com.flushest.bamboo.crawler.core.process.InputProcedure;
import com.flushest.bamboo.crawler.core.process.Procedure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2018/1/20 0020.
 */


@RunWith(SpringRunner.class)
@SpringBootTest
public class CoreTest {

    private CrawlController crawlController;

    @Before
    public void before() {
        crawlController = new CrawlController();
    }

    @Test
    public void test() {
        CrawlConfig config = new CrawlConfig();

        config.setDirectory("D:\\file\\");

        config.addSeeds("https://www.miaobige.com/");

        Procedure dynamicRoot = new InputProcedure("", "");

        //BinaryTreeNode<Procedure> dynamicTree = new BinaryTreeNode<>();

        Chain<Procedure> dynamicChain = new Chain<>();


        //Task task = new Task("001",config);
    }
}
