package com.flushest.bamboo.crawler.core.chain;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/18 0018.
 */
@Setter
@Getter
public class BinaryTreeNode<T> {
    private T current;
    private BinaryTreeNode<T> leftSubtree;
    private BinaryTreeNode<T> rightSubtree;

    public BinaryTreeNode(T current) {
        this.current = current;
    }
}
