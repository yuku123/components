package com.zifang.util.zex.leetcode;

public class 是多少 {

    public String repeat(String s, int times){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i< times; i++){
            sb.append(s);
        }
        return sb.toString();
    }
    public String intToRoman(int num) {

        int q = num/1000;
        int b = (num % 1000)/100;
        int s = (num % 100)/10;
        int g = (num % 10);

        StringBuilder str = new StringBuilder();

        if(q != 0){
            str.append(repeat("M", q));
        }

        if(b != 0){
            if(b == 4){
                str.append("CD");
            } else if(b == 9){
                str.append("CM");
            } else {
                if(b >= 5){
                    str.append("D").append(repeat("C", b - 5));
                } else{
                    str.append(repeat("C",b));
                }
            }
        }

        if(s != 0){
            if(s == 4){
                str.append("XL");
            } else if(s == 9){
                str.append("XC");
            } else {
                if(s >= 5){
                    str.append("L").append(repeat("X", s - 5));
                } else{
                    str.append(repeat("X",s));
                }
            }
        }

        if(g != 0){
            if(g == 4){
                str.append("IV");
            } else if(g == 9){
                str.append("IX");
            } else {
                if(g >= 5){
                    str.append("V").append(repeat("I", g - 5));
                } else{
                    str.append(repeat("I",g));
                }
            }
        }

        return str.toString();
    }

    public static void main(String[] args) {
        String s = "123";
        for(int i = s.length() -1 ; i >= 0 ; i--){
            System.out.println(s.charAt(i));
        }
    }
}
