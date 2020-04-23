package middleware;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: rocky
 * @Date: Created in 2018/5/12.
 */
public class ZookeeperTestDeleteNodeSync implements Watcher {
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String ADDRESS = "192.168.1.8:2181";
    private static final String PREFIX_SYNC = "/mytest-sync-delete-";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTestDeleteNodeSync());
        countDownLatch.await();
        zooKeeper.create(PREFIX_SYNC, "mydelete".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.create(PREFIX_SYNC + "/c1", "mydelete".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        try {
            zooKeeper.delete(PREFIX_SYNC, -1);
        } catch (Exception e) {
            System.out.println("faile to delete path:"+PREFIX_SYNC);
        }
        zooKeeper.delete(PREFIX_SYNC + "/c1", -1);
        System.out.println("success to delete /c1");
        zooKeeper.delete(PREFIX_SYNC , -1);
        Thread.sleep(Integer.MAX_VALUE);

    }
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath())
                countDownLatch.countDown();
        }
    }
}