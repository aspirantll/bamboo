package com.flushest.bamboo.crawler.core.parser;

import com.flushest.bamboo.common.crawler.exception.ErrorParseException;
import com.flushest.bamboo.crawler.core.chain.BinaryTreeNode;
import com.flushest.bamboo.crawler.core.chain.Chain;
import com.flushest.bamboo.crawler.core.chain.Task;
import com.flushest.bamboo.crawler.core.context.CrawlConfig;
import com.flushest.bamboo.crawler.core.process.Procedure;
import com.flushest.bamboo.framework.extension.ExtensionLoader;
import com.flushest.bamboo.framework.util.ClassUtil;
import com.flushest.bamboo.framework.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Administrator on 2018/6/12 0012.
 */
@Slf4j
public class TaskXmlParser implements TaskParser{
    private Document document;

    public TaskXmlParser(InputStream in) {
        try {
            this.document = Jsoup.parse(in, "UTF-8", "", Parser.xmlParser());
        } catch (IOException e) {
            log.error("occur exception at parsing xml", e);
            throw new ErrorParseException("occur exception at parsing xml", e);
        }
    }

    public List<Task> parse() {
        List<Task> tasks = new ArrayList<>();
        Element root = document.getElementsByTag("Tasks").first();
        if(root != null) {
            for (Element element : root.children()) {
                if ("Task".equals(element.tagName())){
                    Map<String, Object> propertiesMap = new HashMap<>();
                    //Task配置
                    propertiesMap.putAll(propertiesMap(element));
                    Map<String, Object> taskProperties = new HashMap<>();
                    taskProperties.put("taskId", propertiesMap.get("id"));
                    //爬虫配置
                    propertiesMap.put("crawlConfig", parseCrawlConfig(element.getElementsByTag("CrawlConfig").first(), taskProperties));
                    //动态处理链
                    propertiesMap.put("dynamicChain", getChain(element.getElementsByTag("DynamicChain").first(), taskProperties));
                    //静态处理链
                    propertiesMap.put("staticChain", getChain(element.getElementsByTag("StaticChain").first(), taskProperties));

                    //组装Task
                    Task task = new Task();
                    ClassUtil.assemblyClass(task, propertiesMap);
                    tasks.add(task);
                }else {
                    log.warn("find not Task node :  " + element.toString());
                }
            }
        }else {
            throw new ErrorParseException("cannot find tag: Tasks");
        }
        return tasks;
    }

    protected Map<String, Object> propertiesMap(Element element) {
        return propertiesMap(element, new HashMap<>());
    }

    protected Map<String, Object> propertiesMap(Element element, Map<String, Object> parentProperties) {
        Map<String, Object> propertiesMap = new HashMap<>(parentProperties);
        for (Attribute attribute : element.attributes()) {
            propertiesMap.put(attribute.getKey(), attribute.getValue());
        }
        return propertiesMap;
    }


    protected CrawlConfig parseCrawlConfig(Element configElement, Map<String, Object> parentProperties) {
        Map<String, Object> configProperties = getConfigProperties(configElement, parentProperties);

        CrawlConfig config = new CrawlConfig();
        ClassUtil.assemblyClass(config, configProperties);
        return config;
    }

    private Map<String, Object> getConfigProperties(Element configElement, Map<String, Object> parentProperties) {
        Map<String, Object> propertyMap = new HashMap<>(parentProperties);
        Elements propertyElements = configElement.getElementsByTag("Property");
        for (Element element : propertyElements) {
            String key = element.attr("name");
            String value = element.attr("value");
            propertyMap.put(key, value);
        }
        return propertyMap;
    }

    protected Chain getChain(Element element, Map<String, Object> parentProperties) {
        Map<String, ProcedureNode> procedureNodeMap = parseProcedures(element, parentProperties);
        ProcedureNode root = findRoot(procedureNodeMap);
        return new Chain(initProcedureTree(root, procedureNodeMap, new HashMap<>()));
    }

    private BinaryTreeNode<Procedure> initProcedureTree(ProcedureNode node, Map<String, ProcedureNode> procedureNodeMap, Map<ProcedureNode, BinaryTreeNode> initedNodes) {
        BinaryTreeNode<Procedure> treeNode = new BinaryTreeNode<>(node.procedure);
        initedNodes.put(node, treeNode);

        //左子树
        if(StringUtil.hasText(node.left)&&!"none".equals(node.left)) {
            ProcedureNode leftNode = procedureNodeMap.get(node.left);
            if(leftNode == null) {
                throw new ErrorParseException("cannot find left procedure:" + node.left + " for procedure[" + node.id + "]");
            }

            if(initedNodes.containsKey(leftNode)) {
                treeNode.setLeftSubtree(initedNodes.get(leftNode));
            }else {
                treeNode.setLeftSubtree(initProcedureTree(leftNode, procedureNodeMap, initedNodes));
            }
        }

        //右子树
        if(StringUtil.hasText(node.right)&&!"none".equals(node.right)) {
            ProcedureNode rightNode = procedureNodeMap.get(node.right);
            if(rightNode == null) {
                throw new ErrorParseException("cannot find right procedure:" + node.left + " for procedure[" + node.id + "]");
            }

            if(initedNodes.containsKey(rightNode)) {
                treeNode.setRightSubtree(initedNodes.get(rightNode));
            }else {
                treeNode.setRightSubtree(initProcedureTree(rightNode, procedureNodeMap, initedNodes));
            }
        }
        return treeNode;
    }

    private ProcedureNode findRoot(Map<String, ProcedureNode> procedureNodeMap) {
        Map<String, ProcedureNode> copyOfNodeMap = new HashMap<>(procedureNodeMap);
        for(ProcedureNode node : procedureNodeMap.values()) {
            copyOfNodeMap.remove(node.left);
            copyOfNodeMap.remove(node.right);
        }
        if (copyOfNodeMap.size() > 1) {
            throw new ErrorParseException("there were many root node");
        }else if(copyOfNodeMap.isEmpty()) {
            throw new ErrorParseException("there is no root node");
        }
        return copyOfNodeMap.values().iterator().next();
    }

    private Map<String, ProcedureNode> parseProcedures(Element element, Map<String, Object> parentProperties) {
        Map<String, ProcedureNode> procedureNodeMap = new HashMap<>();
        for (Element e : element.children()) {
            Procedure procedure = (Procedure) ExtensionLoader.getExtensionLoader(Procedure.class).getExtension(e.tagName());
            if(procedure == null) {
                throw new ErrorParseException("unknown procedureTag:" + e.tagName());
            }
            ClassUtil.assemblyClass(procedure, propertiesMap(e, parentProperties));
            procedure.afterProperties();

            ProcedureNode node = new ProcedureNode();
            node.id = e.attr("id");
            node.left = e.attr("left");
            node.right = e.attr("right");
            node.procedure = procedure;

            procedureNodeMap.put(node.id, node);
        }
        return procedureNodeMap;
    }


    private static class ProcedureNode {
        private String id;
        private Procedure procedure;
        private String left;
        private String right;
    }
}
