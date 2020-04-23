package middleware;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: rocky
 * @Date: Created in 2018/5/13.
 */
public class ZookeeperTestSetNodeDataSync implements Watcher {
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String ADDRESS = "10.0.40.10:2181";
    private static final String PREFIX_SYNC = "/mytest-sync-setData2-";
    private static   ZooKeeper zooKeeper ;
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTestSetNodeDataSync());
        countDownLatch.await();
        zooKeeper.create(PREFIX_SYNC, "hello6data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("the data of node:" + new String(zooKeeper.getData(PREFIX_SYNC, true, null)));
        Stat stat = zooKeeper.setData(PREFIX_SYNC, "hello6data".getBytes(), -1);
        System.out.println("czxid::"+stat.getCzxid()+",mzxid::" + stat.getMzxid() + ",version::" +  stat.getVersion());
        Stat stat2 = zooKeeper.setData(PREFIX_SYNC, "hello6data".getBytes(), stat.getVersion());
        System.out.println("czxid::"+stat2.getCzxid()+",mzxid::" + stat2.getMzxid() + ",version::" +  stat2.getVersion());
        try {
            zooKeeper.setData(PREFIX_SYNC, "hello6data".getBytes(), stat.getVersion());
        } catch (Exception e) {
            System.out.println("exception: " + e);
        }
        Thread.sleep(Integer.MAX_VALUE);

    }
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                countDownLatch.countDown();
            }else if(Event.EventType.NodeDataChanged == event.getType()){
                try {
                    System.out.println("watch the data of:" +  event.getPath() + " is::" + new String(zooKeeper.getData(event.getPath(), true, null)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
