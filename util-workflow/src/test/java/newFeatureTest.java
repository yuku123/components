import com.zifang.util.workflow.conponents.WorkFlowApplicationContext;
import org.junit.Test;

public class newFeatureTest {

    @Test
    public void testAll(){

        String filePath = "feature/workflow_all.json";

        WorkFlowApplicationContext workFlowApplicationContext = new WorkFlowApplicationContext();
        workFlowApplicationContext.initialByLocalFilePath(filePath);
        workFlowApplicationContext.executeTask();
    }
}
