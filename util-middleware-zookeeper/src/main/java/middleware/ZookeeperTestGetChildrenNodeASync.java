package middleware;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: rocky
 * @Date: Created in 2018/5/13.
 */
public class ZookeeperTestGetChildrenNodeASync implements Watcher {
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String ADDRESS = "192.168.1.8:2181";
    private static final String PREFIX_SYNC = "/mytest-async-getChild-";
    private static   ZooKeeper zooKeeper ;
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTestGetChildrenNodeASync());
        countDownLatch.await();
        zooKeeper.create(PREFIX_SYNC, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.create(PREFIX_SYNC + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.getChildren(PREFIX_SYNC, true, new IChildren2Callback(), null);
        zooKeeper.create(PREFIX_SYNC + "/c2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.getChildren(PREFIX_SYNC, true, new IChildren2Callback(), null);
        Thread.sleep(1000);
        zooKeeper.create(PREFIX_SYNC + "/c3", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zooKeeper.getChildren(PREFIX_SYNC, true, new IChildren2Callback(), null);
        Thread.sleep(Integer.MAX_VALUE);

    }
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                countDownLatch.countDown();
            }else if(Event.EventType.NodeChildrenChanged == event.getType()){
                try {
                    System.out.println("get Child:" + zooKeeper.getChildren(event.getPath(), true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
class IChildren2Callback implements  AsyncCallback.Children2Callback {
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        System.out.println(rc + "::" + path + "::" +ctx +"::" + children + "::" + stat);
    }

}