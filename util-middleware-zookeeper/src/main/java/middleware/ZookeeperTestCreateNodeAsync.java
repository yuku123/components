package middleware;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: rocky
 * @Date: Created in 2018/5/12.
 */
public class ZookeeperTestCreateNodeAsync implements Watcher {
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String ADDRESS = "127.0.0.1:21811";
    private static final String PREFIX_ASYNC = "/mytest-async-create-";
    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTestCreateNodeAsync());
        System.out.println("state:"+zooKeeper.getState());
        countDownLatch.await();
        zooKeeper.create(PREFIX_ASYNC, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                new IStringCallBack(), "my test text...1");
        zooKeeper.create(PREFIX_ASYNC, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                new IStringCallBack(), "my test text...2");
        zooKeeper.create(PREFIX_ASYNC, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
                new IStringCallBack(), "my test text...3");
        Thread.sleep(Integer.MAX_VALUE);
    }
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState())
            countDownLatch.countDown();
    }

}
class IStringCallBack implements AsyncCallback.StringCallback {
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("rc:"+rc+",path:"+path+",ctx:"+ctx+"name,"+name);
    }
}