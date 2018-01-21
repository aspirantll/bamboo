package com.flushest.bamboo.crawler.core;

import com.flushest.bamboo.crawler.core.chain.BinaryTreeNode;
import com.flushest.bamboo.crawler.core.chain.Chain;
import com.flushest.bamboo.crawler.core.chain.Task;
import com.flushest.bamboo.crawler.core.constant.FieldName;
import com.flushest.bamboo.crawler.core.context.CrawlConfig;
import com.flushest.bamboo.crawler.core.process.*;
import com.flushest.bamboo.framework.initcfg.StartUpCoreConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2018/1/20 0020.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StartUpCoreConfig.class})
public class ProduceTest {

    private CrawlController crawlController;
    @Test
    public void test() throws Exception {

        CrawlConfig config = new CrawlConfig();
        config.setDirectory("D:\\outputDirectory\\");
        config.setLoadJsEngine(false);
        config.setLoadCssEngine(false);
        //config.addSeeds("https://www.miaobige.com/");
        config.addSeeds("https://www.miaobige.com/read/15046/");
        config.setMaxWaitTime(60000L);
        config.setClientInterval(300000L);
/*
        InputProcedure inputProcedure = new InputProcedure("[id=key]", "大文豪");
        BinaryTreeNode<Procedure> root = new BinaryTreeNode<>(inputProcedure);

        ClickProcedure clickProcedure = new ClickProcedure("button[type=submit,class=serBtn]");
        BinaryTreeNode<Procedure> rL = new BinaryTreeNode<>(clickProcedure);
        root.setLeftSubtree(rL);

        ClickProcedure clickProcedure1 = new ClickProcedure("a[text=大文豪]");
        BinaryTreeNode<Procedure> rLL = new BinaryTreeNode<>(clickProcedure1);
        rL.setLeftSubtree(rLL);*/

        ClickProcedure clickProcedure2 = new ClickProcedure("a[text=第一章 骗子]");
        BinaryTreeNode<Procedure> rLLL = new BinaryTreeNode<>(clickProcedure2);
        //rLL.setLeftSubtree(rLLL);

        BranchProcedure branchProcedure = new BranchProcedure(FieldName.CURRENT_URL, "like", "%.html");
        BinaryTreeNode<Procedure> rLLLL = new BinaryTreeNode<>(branchProcedure);
        rLLL.setLeftSubtree(rLLLL);

        SinkHtmlProcedure sinkHtmlProcedure = new SinkHtmlProcedure("[id=center]","1");
        BinaryTreeNode<Procedure> rLLLLL = new BinaryTreeNode<>(sinkHtmlProcedure);
        rLLLL.setLeftSubtree(rLLLLL);

        BinaryTreeNode<Procedure> next = new BinaryTreeNode<>(new ClickProcedure("a[text=下一章]", ElementSelector.StrictLevel.NONE));
        rLLLLL.setLeftSubtree(next);
        next.setLeftSubtree(rLLLL);

        BinaryTreeNode<Procedure> fileName = new BinaryTreeNode<>( new StaticTextProcedure(FieldName.FILE_NAME, "天道图书馆.txt"));
        BinaryTreeNode<Procedure> title =  new BinaryTreeNode<>(new StaticTextProcedure(FieldName.CONTENT, "h1", true));
        fileName.setLeftSubtree(title);

        BinaryTreeNode<Procedure> content = new BinaryTreeNode<>(new StaticTextProcedure(FieldName.CONTENT, "[id=content]", true));
        title.setLeftSubtree(content);

        BinaryTreeNode<Procedure> sink = new BinaryTreeNode<>(new SinkFileProduce());
        content.setLeftSubtree(sink);



        Task task = new Task("1", config, new Chain<>(rLLL), new Chain<>(fileName));

        task.start();

        task.getDynamicWorkers().get(0).join();

        /*StaticWorker staticWorker = new StaticWorker(task);
        staticWorker.start();
        staticWorker.join();*/


    }
/*

    @Test
    public void inputTest() {
        WebWindow webWindow = windowFetcher.fetch(webURL);

        input(webWindow);

        HtmlPage page = (HtmlPage) webWindow.getEnclosedPage();
        HtmlInput hi = (HtmlInput) page.getElementById("key");
        System.out.print("inputTest: " + hi.getValueAttribute());
    }

    private void input(WebWindow window) {
        InputProcedure inputProcedure = new InputProcedure("[id=key]", "大文豪");
        inputProcedure.process(window);
    }

    private void click(WebWindow window) {
        ClickProcedure clickProcedure = new ClickProcedure("button[type=submit,class=serBtn]");
        clickProcedure.process(window);
    }

    private void click1(WebWindow window) {
        ClickProcedure clickProcedure = new ClickProcedure("a[text=大文豪]");
        clickProcedure.process(window);
    }

    private void click2(WebWindow window) {
        ClickProcedure clickProcedure = new ClickProcedure("a[text=第八百一十四章：取陈氏而代之]");
        clickProcedure.process(window);
    }

    private void sinkHtml(WebWindow window) {
        SinkHtmlProcedure sinkHtmlProcedure = new SinkHtmlProcedure("[id=center]","1");
        sinkHtmlProcedure.process(window);
    }

    private boolean pageBranch(WebWindow window) {
        BranchProcedure branchProcedure = new BranchProcedure(FieldName.CURRENT_URL, "like", "%.html");
        return branchProcedure.process(window);
    }

    @Test
    public void clickProduce() {
        WebWindow webWindow = windowFetcher.fetch(webURL);
        input(webWindow);
        click(webWindow);
        System.out.println(((HtmlPage)webWindow.getEnclosedPage()).asText());
    }

    @Test
    public void pageBranch() {
        WebWindow webWindow = windowFetcher.fetch(webURL);
        input(webWindow);
        click(webWindow);
        System.out.print(pageBranch(webWindow));
    }

    @Test
    public void click2Produce() {
        WebWindow webWindow = windowFetcher.fetch(webURL);
        input(webWindow);
        click(webWindow);
        click1(webWindow);
        System.out.print(ThreadLocalManager.contextThreadLocalManager.get().get(FieldName.CURRENT_URL));
    }

    @Test
    public void sinkHtml() throws InterruptedException {
        WebWindow webWindow = windowFetcher.fetch(webURL);
        input(webWindow);
        click(webWindow);
        click1(webWindow);
        click2(webWindow);
        boolean isLeft = pageBranch(webWindow);
        if(isLeft) {
            sinkHtml(webWindow);
            Page page = ResourceManagerFactory.getResourceManager(Page.class).accept("1");
            System.out.print(page.getHtml());
        }else {
            System.out.println("没有啊");
        }
    }
*/


}
