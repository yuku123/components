package com.zifang.util.zex.leetcode;

import java.util.Stack;

public class _025 {

    static class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public ListNode reverseKGroup(ListNode head, int k) {


        Stack<ListNode> stack = new Stack<>();

        ListNode first = head;
        ListNode last = null;
        ListNode currentHead = head;
        while(currentHead!=null){
            if(stack.size() == k){
                while(stack.size() != 0){
                    if(last == null){
                        last = stack.pop();
                        first = last;
                    } else {
                        ListNode poped = stack.pop();
                        last.next = poped;
                        last = poped;
                    }
                }
            } else {
                stack.push(currentHead);
                currentHead = currentHead.next;
            }
        }

        if(stack.size() == k){
            while(stack.size() != 0){
                if(last == null){
                    last = stack.pop();
                    first = last;
                } else {
                    ListNode poped = stack.pop();
                    last.next = poped;
                    last = poped;
                }
            }
        } else {
            while(true){
                if(stack.size()==1){
                    if(last == null){
                        first = stack.pop();
                    } else {
                        last.next = stack.pop();
                    }
                    break;
                } else {
                    stack.pop();
                }
            }
        }

        return first;
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        ListNode l5 = new ListNode(5);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;

        new _025().reverseKGroup(l1, 3);

    }
}
