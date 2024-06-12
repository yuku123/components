package com.zifang.util.zex.leetcode;

public class _092 {
    static class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public ListNode reverseBetween(ListNode head, int left, int right) {

        if(left == right){
            return head;
        }

        ListNode leftPre = null;
        ListNode rightPost = null;
        int index = 1;
        ListNode currentNode = head;
        ListNode[] listNodes = new ListNode[right - left + 1];
        while(true){

            if(index<= right && index >= left){
                listNodes[listNodes.length - (index-left) -1] = currentNode;
            }

            if(index + 1 == left){
                leftPre = currentNode;
            }

            if(index == right){
                rightPost = currentNode.next;
                break;
            }

            currentNode = currentNode.next;
            index = index + 1;
        }

        for(int i =0 ; i< listNodes.length; i++){
            if(i == 0){
                leftPre.next = listNodes[i];
            } else if(i == listNodes.length -1) {
                listNodes[i-1].next = listNodes[i];
                listNodes[i].next = rightPost;
            } else {
                listNodes[i-1].next = listNodes[i];
            }
        }

        return head;
    }
    public static void main(String[] args) {

        ListNode l5 = new ListNode(5);
//        ListNode l4 = new ListNode(4,l5);
        ListNode l3 = new ListNode(3,l5);
//        ListNode l2 = new ListNode(2,l3);
//        ListNode l1 = new ListNode(1,l2);
        new _092().reverseBetween(l3, 1,2);

        // A：65-90
        // a: 97- 122
    }
}
