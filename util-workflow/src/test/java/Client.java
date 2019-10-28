import com.zifang.util.workflow.interfaces.WorkFlowApplicationContext;
import org.junit.Test;

public class Client {

    @Test
    public void testClient() throws InterruptedException {
        String filePath = "/Users/zifang/workplace/idea_workplace/components/util-workflow/src/test/resources/workflow.json";

        //一个workflow 对应
        WorkFlowApplicationContext workFlowApplicationContext = new WorkFlowApplicationContext(filePath);

        workFlowApplicationContext.executeTask();

        //Thread.sleep(100000000);
    }
}
