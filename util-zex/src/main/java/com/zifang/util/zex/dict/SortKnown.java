package com.zifang.util.zex.dict;

import com.zifang.util.core.util.FileUtil;
import org.junit.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author zifang
 */
public class SortKnown {

    static Pattern pattern = Pattern.compile("[0-9]*");

    @Test
    public void showKnown() {
        Set<String> set = getKownSet();
        set.forEach(System.out::println);
    }

    @Test
    public void analysisPrinciple() {

        List<String> strings = getPrincipleSet();

        strings.forEach(System.out::println);
    }

    @Test
    public void minus() {
        Set<String> kownSet = getKownSet();
        List<String> principleSet = getPrincipleSet();

        principleSet.stream().distinct()
                .filter(e -> !kownSet.contains(e)).collect(Collectors.toList())
                .forEach(System.out::println);

    }

    private Set<String> getKownSet() {
        String str = FileUtil.readFile("known.txt");

        List<String> strings = Arrays.asList(str.split("\n"));
        strings = strings.stream().map(String::toLowerCase).collect(Collectors.toList());
        strings.sort(Comparator.naturalOrder());
        return new LinkedHashSet<>(strings);

    }

    private List<String> getPrincipleSet() {
        String str = FileUtil.readFile("principle.txt");

        List<String> lines = Arrays.asList(str.split("\n"));

        Set<String> set = new LinkedHashSet<>();

        for (String s : lines) {
            String ss = s.replace(".", "")
                    .replace("(", " ")
                    .replace(")", " ")
                    .replace(",", " ")
                    .replace("“", " ")
                    .replace("”", " ")
                    .replace(";", " ")
                    .replace("’", " ")
                    .replace("?", " ")
                    .replace(":", " ")
                    .replace("!", " ")
                    .replace("—", " ")
                    .toLowerCase();
            List<String> sarry = Arrays.asList(ss.split(" "));

            for (String sss : sarry) {
                if (!sss.startsWith("#") && !sss.startsWith("$") && !sss.startsWith("[")) {
                    if (!"".equals(sss) && !isStartWithNumber(sss)) {
                        set.add(sss);
                    }
                }
            }
        }
        ArrayList<String> strings = new ArrayList<>(set);
        strings.sort(Comparator.naturalOrder());

        return strings;
    }


    public static boolean isStartWithNumber(String str) {
        Matcher isNum = pattern.matcher(str.charAt(0) + "");
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

}
