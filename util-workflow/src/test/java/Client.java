import com.zifang.util.workflow.interfaces.Task;
import com.zifang.util.workflow.interfaces.WorkFlowApplicationContext;
import org.junit.Test;

public class Client {

    @Test
    public void testClient() {
        String filePath = "/Users/zifang/workplace/idea_workplace/components/util-workflow/src/test/resources/workflow.json";

        //一个workflow 对应
        WorkFlowApplicationContext workFlowApplicationContext = new WorkFlowApplicationContext(filePath);

        Task task = workFlowApplicationContext.getTask();

        task.exec();
    }
}
