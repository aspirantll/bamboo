package com.flushest.bamboo.crawler.core.chain;

import com.flushest.bamboo.common.crawler.exception.ChainNodeNotFoundException;
import com.flushest.bamboo.common.crawler.exception.ResetChainException;

/**
 * Created by Administrator on 2017/11/16 0016.
 */
public class Chain<T> {
    private BinaryTreeNode<T> wholeTree;
    private BinaryTreeNode<T> currentNode;

    public Chain() {

    }

    public Chain(BinaryTreeNode<T> wholeTree) {
        this.wholeTree = wholeTree;
        reset();
    }

    public void setWholeTree(BinaryTreeNode<T> wholeTree) {
        this.wholeTree = wholeTree;
        reset();
    }

    /**
     * 判断是否存在左右子树
     * @param isLeftSubtree
     * @return
     */
    public boolean hasMore(boolean isLeftSubtree) {
        if(isLeftSubtree) {
            return currentNode.getLeftSubtree()!=null;
        }else {
            return currentNode.getRightSubtree()!=null;
        }
    }

    public T current() {
        return currentNode.getCurrent();
    }

    /**
     * 当前节点下移到下一层子树
     * @param isLeftSubtree
     * @return
     */
    public T next(boolean isLeftSubtree) {
        BinaryTreeNode<T> next = isLeftSubtree?currentNode.getLeftSubtree():currentNode.getRightSubtree();
        if(next==null) {
            throw new ChainNodeNotFoundException();
        }
        currentNode = next;
        return current();
    }

    /**
     * 重置当前节点为Chain起点
     */
    public void reset() {
        if(wholeTree==null) {
            throw new ResetChainException();
        }
        currentNode = wholeTree;
    }


}
