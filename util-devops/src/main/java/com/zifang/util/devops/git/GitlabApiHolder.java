package com.zifang.util.devops.git;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;


public class GitlabApiHolder {

    public static final String URL = "";

    private static final String userName = "";
    private static final String pwd = "";

    private static final String token = "";

    public static GitlabApiHolder INSTANCE = new GitlabApiHolder();

    private GitLabApi api;

    private GitlabApiHolder() {
        //api = new GitLabApi(GitLabApi.ApiVersion.V4, URL, token);
        try {
            api = GitLabApi.oauth2Login(URL, userName, pwd, null, null, true);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
    }

    public GitLabApi getApi() {
        return api;
    }

}
