package com.zifang.util.devops.git.github;

import org.junit.Test;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.List;

public class LoginTest {

    private static String secret = "xx";

    @Test
    public  void test0() throws IOException {
        GitHub github = new GitHubBuilder().withOAuthToken(secret).build();
        GHCreateRepositoryBuilder g = github.createRepository("aaaaa");
        g.private_(true);
        g.create();
    }

    @Test
    public void test1() throws IOException {
        GitHub github = new GitHubBuilder().withOAuthToken(secret).build();
        PagedIterable<GHRepository>s =  github.listAllPublicRepositories();
        List<GHRepository> ss = s.toList();
        System.out.println(ss);
    }
}
