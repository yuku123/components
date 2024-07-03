package com.zifang.util.zex.leetcode;

public class _067 {
    public String addBinary(String a, String b) {
        char[] ac = a.toCharArray();
        char[] bc = b.toCharArray();

        int length = Math.max(ac.length, bc.length);

        int[] r = new int[length+1];

        int c = 0;
        for(int i= 0;i < length; i++){
            int aa = ac.length-1 <= i? 0: ac[ac.length-1-i]-'0';
            int bb = bc.length-1 <= i? 0: bc[bc.length-1-i]-'0';

            int sum = aa + bb + c;

            if(sum >= 2){
                r[r.length -1-i] = sum % 2;
                c = 1;
            } else {
                r[r.length -1-i] = sum;
                c = 0;
            }
        }

        if(c == 1){
            r[0] = 1;
        }

        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < r.length; i++){
            if(i== 0 && r[i]==0){
                continue;
            }

            sb.append(r[i]);
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        new _067().addBinary("11","1");
    }
}
