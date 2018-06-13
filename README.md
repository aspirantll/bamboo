# bamboo
## 用于实现流程化爬虫，定义基础操作和分支节点共同完成复杂流程操作

Task作为一个爬虫流程，分为两部分处理：一是“动态处理”，对网页加载css或者加载js效果进行处理；还有一个是“静态处理”，对从动态处理得到的静态网页页面进行摘取文本等操作。两部分解耦通过资源管理器关联，分别用两个不同线程执行操作。

## 动态处理
1.点击某一元素(ClickProcedure)
2.填入输入框内容(inputProcedure)
3.分支节点(branchProcedure)
4.输出静态页面到资源管理器(SinkHtmlProcedure)
## 静态处理
1.抓取指定元素文本(StaticTextProcedure)
2.输出文本到文件(SinkFileProcedure)

更多处理有待扩展，资源管理器可扩展

详细使用见\bamboo-crawler\crawler-core\src\test\java\com\flushest\bamboo\crawler\core\produceTest
