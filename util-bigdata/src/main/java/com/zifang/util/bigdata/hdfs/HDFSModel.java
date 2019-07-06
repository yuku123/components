package com.zifang.util.bigdata.hdfs;


public class HDFSModel {

//    public static void downloadModelFromHDFS(String modelName,String user_id) throws IOException, InterruptedException{
//
//        //String fileName = modelPath.substring(modelPath.lastIndexOf("/"), modelPath.lastIndexOf("."));
//        String fileFullName = modelName + ".RData";
//
//        String modelOutputPath = FileURLUtils.getUSER_HDFS_MODELDOWNLOAD(user_id)+"/"+fileFullName;
//        String[] cmd2 = new String[] { "com.zifang.util.bigdata.hdfs", "dfs", "-get", "csm/"+user_id+"/" + modelName + "/"+fileFullName , modelOutputPath};
//        Process pro2 = Runtime.getRuntime().exec(cmd2);
//        pro2.waitFor();
//        pro2.destroy();
//    }
//
//    public static void uploadModelToHDFS(String renameModel,String user_id) throws IOException, InterruptedException{
//
//        String localBuildModelPath = FileURLUtils.getUSER_HDFS_MODELUPLOAD(user_id);
//        for(String localModel:new File(localBuildModelPath).list()){
//
//            String localOldModel = localBuildModelPath + "/"+localModel;
//            String reNameNewModel = localBuildModelPath +"/"+renameModel+".RData";
//            new File(localOldModel).renameTo(new File(reNameNewModel));
//
//            String fileName = renameModel + ".DRata";
//
//            String[] cmd1 = new String[] { "com.zifang.util.bigdata.hdfs", "dfs", "-mkdir", "csm/"+user_id+"/" + renameModel };
//            Process pro1 = Runtime.getRuntime().exec(cmd1);
//            pro1.waitFor();
//            String[] cmd2 = new String[] { "com.zifang.util.bigdata.hdfs", "dfs", "-put", reNameNewModel, "csm/"+user_id+"/" + renameModel + "/." };
//            Process pro2 = Runtime.getRuntime().exec(cmd2);
//            pro2.waitFor();
//            pro1.destroy();
//            pro2.destroy();
//        }
//    }
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//        //String modelPath = "/home/piday/work/workspace/zhangch/piday-csm-tomcat/src/com/piday/r/model.RData";
//        String user_id = "user001";
//        String renameModel = "model1";
//        uploadModelToHDFS(renameModel,user_id);
//        //downloadModelFromHDFS(modelPath, user_id);
//    }

}
