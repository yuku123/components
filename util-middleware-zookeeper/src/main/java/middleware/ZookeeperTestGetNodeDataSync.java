package middleware;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: rocky
 * @Date: Created in 2018/5/13.
 */
public class ZookeeperTestGetNodeDataSync implements Watcher {
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String ADDRESS = "10.0.40.10:2181";
    private static final String PREFIX_SYNC = "/mytest-sync-getData4-";
    private static   ZooKeeper zooKeeper ;
    private  static final Stat stat = new Stat();
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTestGetNodeDataSync());
        countDownLatch.await();
        zooKeeper.create(PREFIX_SYNC, "hellodata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("the data of node:" + new String(zooKeeper.getData(PREFIX_SYNC, true, stat)));
        System.out.println("czxid::"+stat.getCzxid()+",mzxid::" + stat.getMzxid() + ",version::" +  stat.getVersion());
        zooKeeper.setData(PREFIX_SYNC, "hello2data".getBytes(), -1);
        Thread.sleep(Integer.MAX_VALUE);

    }
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                countDownLatch.countDown();
            }else if(Event.EventType.NodeDataChanged == event.getType()){
                try {
                    System.out.println("the data of:" +  event.getPath() + " is::" + new String(zooKeeper.getData(event.getPath(), true, stat)));
                    System.out.println("watch czxid::"+stat.getCzxid()+",mzxid::" + stat.getMzxid() + ",version::" +  stat.getVersion());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}