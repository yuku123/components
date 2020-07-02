package com.zifang.util.core.composite;

import com.zifang.util.core.pattern.composite.leaf.LeafHelper;
import com.zifang.util.core.pattern.composite.leaf.LeafWrapper;
import com.zifang.util.core.pattern.composite.leaf.Tree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LeafTest {

    @Test
    public void test(){

        List<Node> nodes = getDataList();

        List<LeafWrapper<Integer,Integer,Node>> leafWrappers = new ArrayList<>();

        for(Node e : nodes){
            LeafWrapper<Integer,Integer,Node> leafWrapper = LeafHelper.wrapper(e.getId(),e.getParentId(),e);
            leafWrappers.add(leafWrapper);
        }
        Tree tree = LeafHelper.solveLeafWrapperList(leafWrappers);

        System.out.print(tree);

    }



    private  List<Node> getDataList() {
        List<Node> nodes  = new ArrayList<>();

        nodes.add(new Node(5,3,"EE"));
        nodes.add(new Node(8,4,"HH"));
        nodes.add(new Node(9,5,"II"));
        nodes.add(new Node(1,0,"AA"));
        nodes.add(new Node(2,1,"BB"));
        nodes.add(new Node(6,2,"FF"));
        nodes.add(new Node(7,2,"GG"));
        nodes.add(new Node(3,1,"CC"));
        nodes.add(new Node(4,3,"DD"));
        return nodes;
    }


}
