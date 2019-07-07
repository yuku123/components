package leetcode.q5;

public class Solution2 {


    public String longestPalindrome(String s) {
        char[] cs = s.toCharArray();
        // first kind
        int result = 0;
        int lens = 0;
        String result_str = "";

        for (int i = 1; i < cs.length; i++) {
            lens = 0;
            if (String.valueOf(cs[i]).equals(String.valueOf(cs[i - 1]))) {
                while (true) {
                    if (i - 1 - lens < 0 || i + lens > cs.length - 1) {
                        if (lens > result) {
                            result = lens;
                            result_str = new String(cs, i - lens, i - 1 + lens);
                        }
                        break;
                    }
                    String start = String.valueOf(cs[i - 1 - lens]);
                    String end = String.valueOf(cs[i + lens]);
                    if (start.equals(end)) {
                        lens++;
                    } else {
                        if (lens > result) {
                            result = lens;
                            result_str = new String(cs, i - lens, i + lens - 1);
                        }
                        break;
                    }

                }
            } else if (i + 1 <= cs.length - 1 && String.valueOf(cs[i - 1]).equals(String.valueOf(cs[i + 1]))) {
                while (true) {
                    if (i - 1 - lens > 0 && i + 1 + lens < cs.length - 1) {
                        String start = String.valueOf(cs[i - 1 - lens]);
                        String end = String.valueOf(cs[i + 1 + lens]);
                        if (start.equals(end)) {
                            lens++;
                        } else {
                            if (lens > result) {
                                result = lens;
                                result_str = new String(cs, i - 1 - lens, i + 1 + lens);
                            }
                            break;
                        }
                    } else {
                        if (lens > result) {
                            result = lens;
                            result_str = new String(cs, i - lens, i + lens);
                        }
                        break;
                    }
                }
            }
        }
        return result_str;
    }

    public static void main(String[] args) {
        Solution2 solution = new Solution2();
        System.out.println(solution.longestPalindrome("abadd"));

    }
}
