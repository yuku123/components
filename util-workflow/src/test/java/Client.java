import com.zifang.util.workflow.conponents.WorkFlowApplicationContext;
import org.junit.Test;

public class Client {

    @Test
    public void testClienta() throws InterruptedException {
        //String filePath = "/Users/zifang/workplace/idea_workplace/components/util-workflow/src/test/resources/workflow.json";
        String filePath = "/Users/zifang/workplace/idea_workplace/components/util-workflow/src/test/resources/ex/workflow_read_write_local.json";

        //一个workflow 对应
        WorkFlowApplicationContext workFlowApplicationContext = new WorkFlowApplicationContext(filePath);

        workFlowApplicationContext.executeTask();

        //Thread.sleep(100000000);
    }

    @Test
    public void testClientb() throws InterruptedException {
        //String filePath = "/Users/zifang/workplace/idea_workplace/components/util-workflow/src/test/resources/workflow.json";
        String filePath = "/Users/zifang/workplace/idea_workplace/components/util-workflow/src/test/resources/ex/workflow_read_write_mysql.json";

        //一个workflow 对应
        WorkFlowApplicationContext workFlowApplicationContext = new WorkFlowApplicationContext(filePath);

        workFlowApplicationContext.executeTask();

        //Thread.sleep(100000000);
    }
}
