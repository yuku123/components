package com.zifang.util.core.beans;

import junit.framework.TestCase;

import java.beans.PropertyEditorSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPropertyEditorSupport extends TestCase {

    public void testPropertyEditorSupport() {
        PropertyEditorSupport support = new UserPropertyEditorSupport();
        support.setAsText("User [age=18, name=yang]");
        System.out.println(support.getValue());
    }

    static class UserPropertyEditorSupport extends PropertyEditorSupport {
        private static final Pattern pattern = Pattern.compile("User \\[age=(\\d+), name=(\\S+)\\]");

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (text == null) {
                return;
            }
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                int age = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);

                User user = new User(age, name);
                setValue(user);
            }
        }
    }

    static class User {
        private int age;
        private String name;

        public User() {
            super();
        }

        public User(int age, String name) {
            this.age = age;
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}