package com.zifang.util.devops.git;

public class GitlabApiWrapper {

    private static GitLabApi api;

    static {
        api = GitlabApiHolder.INSTANCE.getApi();
    }

    /**
     * 获取项目api
     *
     * @return
     */
    public static ProjectApi getProjectApi() {
        return api.getProjectApi();
    }

    /**
     * 获取组api
     *
     * @return
     */
    public static GroupApi getGroupApi() {
        return api.getGroupApi();
    }

    /**
     * 获取用户api
     *
     * @return
     */
    public static UserApi getUserApi() {
        return api.getUserApi();
    }

    /**
     * 获取仓库api
     *
     * @return
     */
    public static RepositoryApi getRepositoryApi() {
        return api.getRepositoryApi();
    }

    /**
     * 获取仓库文件api
     *
     * @return
     */
    public static RepositoryFileApi getRepositoryFileApi() {
        return api.getRepositoryFileApi();
    }

    /**
     * 获取提交api
     *
     * @return
     */
    public static CommitsApi getCommitsApi() {
        return api.getCommitsApi();
    }


}
