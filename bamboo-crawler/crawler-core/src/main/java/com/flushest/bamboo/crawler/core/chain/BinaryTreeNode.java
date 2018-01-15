package com.flushest.bamboo.crawler.core.chain;

/**
 * Created by Administrator on 2017/11/18 0018.
 */
public class BinaryTreeNode<T> {
    private T current;
    private BinaryTreeNode<T> leftSubtree;
    private BinaryTreeNode<T> rightSubtree;

    public BinaryTreeNode(T current, BinaryTreeNode<T> leftSubtree, BinaryTreeNode<T> rightSubtree) {
        this.current = current;
        this.leftSubtree = leftSubtree;
        this.rightSubtree = rightSubtree;
    }

    public T getCurrent() {
        return current;
    }

    public BinaryTreeNode<T> getLeftSubtree() {
        return leftSubtree;
    }

    public BinaryTreeNode<T> getRightSubtree() {
        return rightSubtree;
    }

}
