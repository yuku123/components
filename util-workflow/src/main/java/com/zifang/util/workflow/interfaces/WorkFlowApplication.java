package com.zifang.util.workflow.interfaces;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkFlowApplication {

    public static ExecutorService threadPool = Executors.newFixedThreadPool(100);

}
