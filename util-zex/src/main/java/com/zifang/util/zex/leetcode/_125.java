package com.zifang.util.zex.leetcode;

public class _125 {
    public boolean isNumberOrChar(char c){
        if(c >= '0' && c <= '9'){
            return true;
        }

        if(c >= 'a' && c <= 'z'){
            return true;
        }

        if(c >= 'A' && c <= 'Z'){
            return true;
        }

        return false;
    }

    public boolean equa(char c1, char c2){
        return String.valueOf(c1).compareToIgnoreCase(String.valueOf(c2)) == 0;
    }

    public boolean isPalindrome(String s) {

        if(s.equals("")){
            return true;
        }

        int i = 0;
        int j = s.length() -1;

        while(true){
            while(true){
                if(!isNumberOrChar(s.charAt(i))){
                    if(i != s.length() -1){
                        i++;
                    }
                } else {
                    break;
                }
            }

            while(true){
                if(!isNumberOrChar(s.charAt(j))){
                    if(j > 0 ){
                        j--;
                    }
                } else {
                    break;
                }
            }

            if(!equa(s.charAt(i), s.charAt(j))){
                return false;
            }

            if(i == j){
                return true;
            }
            if(i > j){
                return true;
            }
            i++;
            j--;
        }
    }

    public static void main(String[] args) {
        new _125().isPalindrome(" ");
    }
}
