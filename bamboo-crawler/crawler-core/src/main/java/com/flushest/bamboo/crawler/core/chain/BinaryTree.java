package com.flushest.bamboo.crawler.core.chain;

/**
 * Created by Administrator on 2017/11/18 0018.
 */
public class BinaryTree<T> {
    private T current;
    private BinaryTree<T> leftSubtree;
    private BinaryTree<T> rightSubtree;

    public BinaryTree(T current, BinaryTree<T> leftSubtree, BinaryTree<T> rightSubtree) {
        this.current = current;
        this.leftSubtree = leftSubtree;
        this.rightSubtree = rightSubtree;
    }

    public T getCurrent() {
        return current;
    }

    public BinaryTree<T> getLeftSubtree() {
        return leftSubtree;
    }

    public BinaryTree<T> getRightSubtree() {
        return rightSubtree;
    }

}
