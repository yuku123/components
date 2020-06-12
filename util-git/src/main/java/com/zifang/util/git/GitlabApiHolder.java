package com.zifang.util.git;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;

/**
 * @Author chenjunbin
 * @Date 2020-05-27
 * @Description
 */
public class GitlabApiHolder {

    public static final String URL = "";

    private static final String userName = "";
    private static final String pwd = "";

    private static final String token = "";

    public static GitlabApiHolder INSTANCE = new GitlabApiHolder();

    private Object api;

    private GitlabApiHolder() {
        //api = new GitLabApi(GitLabApi.ApiVersion.V4, URL, token);
        try {
            api = GitLabApi.oauth2Login(URL, userName, pwd, null, null, true);
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
    }

    public GitLabApi getApi() {
        return (GitLabApi) api;
    }

}
