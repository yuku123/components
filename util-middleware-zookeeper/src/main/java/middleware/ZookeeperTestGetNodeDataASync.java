package middleware;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: rocky
 * @Date: Created in 2018/5/13.
 */
public class ZookeeperTestGetNodeDataASync implements Watcher {
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String ADDRESS = "10.0.40.10:2181";
    private static final String PREFIX_SYNC = "/mytest-async-getData8-";
    private static   ZooKeeper zooKeeper ;
    private  static final Stat stat = new Stat();
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTestGetNodeDataASync());
        countDownLatch.await();
        zooKeeper.create(PREFIX_SYNC, "hello7data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.getData(PREFIX_SYNC, true, new IDataCallback(), null);
        for(int i=0; i<3; i++){
            zooKeeper.setData(PREFIX_SYNC, "hello6data".getBytes(), -1);
        }
        Thread.sleep(Integer.MAX_VALUE);
    }
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                countDownLatch.countDown();
            }else if(Event.EventType.NodeDataChanged == event.getType()){
                try {
                    zooKeeper.getData(PREFIX_SYNC, true, new IDataCallback(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
class IDataCallback implements AsyncCallback.DataCallback {
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        System.out.println("watch rc::" + rc + ", path::" + path + ", ctx::" + ctx +", data::" + new String(data));
        System.out.println("watch czxid::"+stat.getCzxid()+",mzxid::" + stat.getMzxid() + ",version::" +  stat.getVersion());
    }
}