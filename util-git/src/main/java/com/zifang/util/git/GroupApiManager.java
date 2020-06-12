package com.zifang.util.git;

import org.apache.commons.codec.binary.Base64;
import org.gitlab4j.api.*;
import org.gitlab4j.api.models.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GroupApiManager {

    /**
     * 创建组
     *
     * @param name 组名称
     * @param path 组path
     * @param desc 组desc
     * @return
     * @throws GitLabApiException
     */
    public Group createGroup(String name, String path, String desc) throws GitLabApiException {
        GroupApi groupApi = GitlabApiWrapper.getGroupApi();
        GroupParams params = new GroupParams();
        params.withName(name).withPath(path).withDescription(desc);
        Group group = groupApi.createGroup(params);
        return group;
    }


    /**
     * 删除组
     *
     * @param groupId 组id
     * @throws GitLabApiException
     */
    public void deleteGroup(String groupId) throws GitLabApiException {
        GroupApi groupApi = GitlabApiWrapper.getGroupApi();
        groupApi.deleteGroup(groupId);
    }

    /**
     * 搜索组
     *
     * @param search
     * @return
     * @throws GitLabApiException
     */
    public List<Group> searchGroups(String search) throws GitLabApiException {
        GroupApi groupApi = GitlabApiWrapper.getGroupApi();
        return groupApi.getGroups(search);
    }

    /**
     * 查询组
     *
     * @param groupId
     * @return
     * @throws GitLabApiException
     */
    public Group getGroup(String groupId) throws GitLabApiException {
        GroupApi groupApi = GitlabApiWrapper.getGroupApi();
        return groupApi.getGroup(groupId);
    }

    /**
     * 获取用户列表（默认pageSize=96）
     *
     * @return
     * @throws GitLabApiException
     */
    public List<User> getUsers() throws GitLabApiException {
        UserApi userApi = GitlabApiWrapper.getUserApi();
        List<User> list = userApi.getUsers();
        return list;
    }

    /**
     * 根据username获取用户
     *
     * @param username
     * @return
     * @throws GitLabApiException
     */
    public User getUser(String username) throws GitLabApiException {
        UserApi userApi = GitlabApiWrapper.getUserApi();
        User user = userApi.getUser(username);
        return user;
    }

    /**
     * 根据userid获取用户
     *
     * @param userId
     * @return
     * @throws GitLabApiException
     */
    public User getUser(Integer userId) throws GitLabApiException {
        UserApi userApi = GitlabApiWrapper.getUserApi();
        User user = userApi.getUser(userId);
        return user;
    }


    /**
     * 获取项目列表
     *
     * @throws GitLabApiException
     */
    public List<Project> getProjects() throws GitLabApiException {
        ProjectApi projectApi = GitlabApiWrapper.getProjectApi();
        List<Project> list = projectApi.getProjects();
        return list;
    }


    /**
     * 搜索项目
     *
     * @param search
     * @return
     * @throws GitLabApiException
     */
    public List<Project> searchProjects(String search) throws GitLabApiException {
        ProjectApi projectApi = GitlabApiWrapper.getProjectApi();
        List<Project> list = projectApi.getProjects(search);
        return list;
    }

    /**
     * 精确匹配 搜索项目
     *
     * @param projectName 项目名字
     * @return
     * @throws GitLabApiException
     */
    public Project getProject(String projectName) throws GitLabApiException {

        assert projectName != null; // 必须为非空

        ProjectApi projectApi = GitlabApiWrapper.getProjectApi();
        List<Project> list = projectApi.getProjects(projectName);
        for (Project sub : list){
            projectName.equals(sub.getName()); // 精确匹配
            return sub;
        }
        throw new RuntimeException("没有找到名字为："+projectName+"的项目");
    }

    /**
     * 创建项目
     *
     * @param namespaceId
     * @param projectName
     * @return
     * @throws GitLabApiException
     */
    public Project createProject(Integer namespaceId, String projectName) throws GitLabApiException {
        ProjectApi projectApi = GitlabApiWrapper.getProjectApi();
        Project project = projectApi.createProject(namespaceId, projectName);
        return project;
    }


    /**
     * 删除项目
     *
     * @param projectId
     * @throws GitLabApiException
     */
    public void deleteProject(String projectId) throws GitLabApiException {
        ProjectApi projectApi = GitlabApiWrapper.getProjectApi();
        projectApi.deleteProject(projectId);
    }

    /**
     * 获取项目分支列表
     *
     * @throws GitLabApiException
     */
    public List<Branch> getBranches(String projectId) throws GitLabApiException {
        RepositoryApi repositoryApi = GitlabApiWrapper.getRepositoryApi();
        List<Branch> branches = repositoryApi.getBranches(projectId);
        return branches;
    }


    /**
     * 创建分支
     *
     * @param projectId  项目id
     * @param branchName 分支名
     * @param ref        引用分支名
     * @return
     * @throws GitLabApiException
     */
    public Branch createBranch(String projectId, String branchName, String ref) throws GitLabApiException {
        RepositoryApi repositoryApi = GitlabApiWrapper.getRepositoryApi();
        //创建分支
        Branch branch = repositoryApi.createBranch(projectId, branchName, ref);
        return branch;
    }

    /**
     * 删除分支
     *
     * @param projectId  项目id
     * @param branchName 分支名
     * @return
     * @throws GitLabApiException
     */
    public void deleteBranch(String projectId, String branchName) throws GitLabApiException {
        RepositoryApi repositoryApi = GitlabApiWrapper.getRepositoryApi();
        repositoryApi.deleteBranch(projectId, branchName);
    }


    /**
     * 获取项目文件目录
     *
     * @param projectId
     * @param filePath
     * @param branchName
     * @param recursive  是否递归
     * @return
     * @throws GitLabApiException
     */
    public List<TreeItem> getTree(String projectId, String filePath, String branchName, Boolean recursive) throws GitLabApiException {
        RepositoryApi repositoryApi = GitlabApiWrapper.getRepositoryApi();
        List<TreeItem> list = repositoryApi.getTree(projectId, filePath, branchName, recursive);
        return list;


    }

    /**
     * 获取指定后缀的文件
     *
     * @param projectId project唯一标识
     * @param filePath
     * @param branchName
     * @param recursive  是否递归
     * @param suffix
     * @return
     * @throws GitLabApiException
     */
    public List<TreeItem> getTreeBySuffix(String projectId, String filePath, String branchName, Boolean recursive, String suffix) throws GitLabApiException {
        List<TreeItem> list = this.getTree(projectId, filePath, branchName, recursive);
        return list.stream()
                .filter(treeItem -> treeItem.getName().endsWith(suffix))
                .collect(Collectors.toList());

    }

    /**
     * 得到 文件数 根据文件的类型 与后缀进行过滤
     * @param project project
     * @param filePath 从哪个路径开始扫描 默认应该为 "" 根路径
     * @param branchName 分支名
     * @param recursive  是否递归
     * @param type TreeItem.Type.BLOB 是实体文件 TreeItem.Type.TREE是路径相关
     * @param suffix 后缀
     * @return
     * @throws GitLabApiException
     * */
    public List<TreeItem> getTreeByTypeAndSuffix(Project project, String filePath, String branchName, Boolean recursive, TreeItem.Type type, String suffix) throws GitLabApiException {
        List<TreeItem> list = this.getTree(String.valueOf(project.getId()), filePath, branchName, recursive);
        return list.stream()
                .filter(treeItem -> type.equals(treeItem.getType()))
                .filter(treeItem -> treeItem.getName().endsWith(suffix))
                .collect(Collectors.toList());
    }

    /**
     * 得到 文件数 根据文件的类型 与后缀进行过滤
     * @param project project
     * @param filePath 从哪个路径开始扫描 默认应该为 "" 根路径
     * @param branchName 分支名
     * @param recursive  是否递归
     * @param type TreeItem.Type.BLOB 是实体文件 TreeItem.Type.TREE是路径相关
     * @return
     * @throws GitLabApiException
     * */
    public List<TreeItem> getTreeByType(Project project, String filePath, String branchName, Boolean recursive, TreeItem.Type type) throws GitLabApiException {
        List<TreeItem> list = this.getTree(String.valueOf(project.getId()), filePath, branchName, recursive);
        return list.stream()
                .filter(treeItem -> type.equals(treeItem.getType()))
                .collect(Collectors.toList());
    }


    public boolean checkFilePathExist(Project project, String branchName, String filePath) throws GitLabApiException {

        // 获得目录
        String pathRoot = getParentPath(filePath);

        List<TreeItem> treeItems = getTreeByType(project,pathRoot,branchName,false,TreeItem.Type.BLOB);

        for(TreeItem treeItem: treeItems){
            if(filePath.equals(treeItem.getPath())){
                return true;
            }
        }
        return false;
    }

    private String getParentPath(String filePath) {
        if(!filePath.contains("/")){//是根路径
            return "";
        }else{
            return filePath.substring(0,filePath.lastIndexOf("/"));
        }
    }

    /**
     * 获取文件内容
     *
     * @param project
     * @param branchName 分支名
     * @param filePath   文件全路径
     * @throws Exception
     */
    public String getRepositoryFileContent(Project project, String branchName, String filePath) throws Exception {
        RepositoryFileApi repositoryFileApi = GitlabApiWrapper.getRepositoryFileApi();
        RepositoryFile repositoryFile = repositoryFileApi.getFile(project.getId(), filePath, branchName, true);
        String content = new String(new Base64().decode(repositoryFile.getContent()), "UTF-8");
        return content;
    }


    /**
     * 提交（新增）
     *
     * @param project    项目
     * @param branchName 分支
     * @param commitMsg  提交备注
     * @param content    commit的内容
     * @param filePath   commit的文件全路径
     * @return
     * @throws Exception
     */
    public Commit createCommit(Project project, String branchName, String commitMsg, String content, String filePath) throws GitLabApiException {
        CommitsApi commitsApi = GitlabApiWrapper.getCommitsApi();
        CommitAction commitAction = new CommitAction()
                .withAction(CommitAction.Action.CREATE)
                .withContent(content)
                .withFilePath(filePath);

        Commit commit = commitsApi.createCommit(
                project, branchName, commitMsg, null, null, null, Arrays.asList(commitAction));
        return commit;
    }

    /**
     * 提交(批量)（新增）
     *
     * @param project    项目
     * @param branchName 分支
     * @param commitMsg  提交备注
     * @param commitActions   commit合集
     * @return
     * @throws Exception
     */
    public Commit createCommitBatch(Project project, String branchName, String commitMsg,List<CommitAction> commitActions) throws GitLabApiException {
        CommitsApi commitsApi = GitlabApiWrapper.getCommitsApi();

        return commitsApi.createCommit(
                project,
                branchName,
                commitMsg,
                null,
                null,
                null,
                commitActions);
    }

    /**
     * 提交（修改）
     *
     * @param project
     * @param branchName
     * @param commitMsg
     * @param content
     * @param filePath
     * @return
     * @throws Exception
     */
    public Commit updateCommit(Project project, String branchName, String commitMsg, String content, String filePath) throws GitLabApiException {
        CommitsApi commitsApi = GitlabApiWrapper.getCommitsApi();
        CommitAction commitAction = new CommitAction()
                .withAction(CommitAction.Action.UPDATE)
                .withContent(content)
                .withFilePath(filePath);

        Commit commit = commitsApi.createCommit(
                project, branchName, commitMsg, null, null, null, Arrays.asList(commitAction));
        return commit;
    }

    /**
     * 提交（删除）
     *
     * @param project
     * @param branchName
     * @param commitMsg
     * @param filePath
     * @return
     * @throws Exception
     */
    public Commit deleteCommit(Project project, String branchName, String commitMsg, String filePath) throws GitLabApiException {
        CommitsApi commitsApi = GitlabApiWrapper.getCommitsApi();
        CommitAction commitAction = new CommitAction()
                .withAction(CommitAction.Action.DELETE)
                .withFilePath(filePath);

        Commit commit = commitsApi.createCommit(
                project, branchName, commitMsg, null, null, null, Arrays.asList(commitAction));
        return commit;
    }


}
