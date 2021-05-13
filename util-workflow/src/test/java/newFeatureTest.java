import com.zifang.util.workflow.conponents.WorkFlowApplicationContext;

public class newFeatureTest {

    //@Test
    public void testAll() {

        String filePath = "feature/workflow_all.json";

        WorkFlowApplicationContext workFlowApplicationContext = new WorkFlowApplicationContext();
        workFlowApplicationContext.initialByLocalFilePath(filePath);
        workFlowApplicationContext.executeTask();
    }
}
