package willem.weiyu.casual.zookeeper;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import lombok.extern.slf4j.Slf4j;
import willem.weiyu.casual.CasualServiceRegistry;
import willem.weiyu.casual.Constant;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/25 12:03
 */
@Slf4j
public class ZookeeperCasualServiceRegistry implements CasualServiceRegistry {
    CuratorFramework client;
    public static final String CHARSET_UTF8="UTF-8";

    public ZookeeperCasualServiceRegistry(String zkAddress){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zkAddress, Constant.ZK_SESSION_TIMEOUT,
                Constant.ZK_CONNECTION_TIMEOUT,
                retryPolicy);
        client.start();
        log.info("connected zookeeper:[{}]",zkAddress);
    }

    @Override
    public void register(String serviceName, String serviceAddress) throws Exception {
        String registryPath = Constant.ZK_REGISRY_PATH;
        //创建registry节点（持久）
        if (client.checkExists().forPath(registryPath) == null){
            client.create().withMode(CreateMode.PERSISTENT).forPath(registryPath);
            log.info("create registry node:[{}]", registryPath);
        }
        //创建service节点（持久）
        String servicePath = registryPath +"/"+serviceName;
        if (client.checkExists().forPath(servicePath) == null){
            client.create().withMode(CreateMode.PERSISTENT).forPath(servicePath);
            log.info("create service node:[{}]", servicePath);
        }
        //创建address节点（临时）
        String addressPath = servicePath+"/"+serviceAddress;
        if (client.checkExists().forPath(addressPath) == null){
            client.create().withMode(CreateMode.EPHEMERAL).forPath(addressPath,serviceAddress.getBytes(CHARSET_UTF8));
            log.info("create address node:[{}]",addressPath);
        }
    }

    @Override
    public String discover(String serviceName) {
        String servicePath = Constant.ZK_REGISRY_PATH+"/"+serviceName;
        try {
            if (client.checkExists().forPath(servicePath) == null){
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }
            List<String> addressList = client.getChildren().forPath(servicePath);
            if (addressList == null || addressList.isEmpty()){
                throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
            }
            // 获取 address 节点
            String address;
            int size = addressList.size();
            if (size == 1) {
                // 若只有一个地址，则获取该地址
                address = addressList.get(0);
                log.info("get only address node: {}", address);
            } else {
                // 若存在多个地址，则随机获取一个地址
                address = addressList.get(ThreadLocalRandom.current().nextInt(size));
                log.info("get random address node: {}", address);
            }
            // 获取 address 节点的值
            String addressPath = servicePath + "/" + address;
            byte[] dataByte = client.getData().forPath(addressPath);
            if (dataByte == null || dataByte.length < 1){
                throw new RuntimeException(String.format("can not find any value on address path: %s",
                        addressPath));
            }
            return new String(dataByte,CHARSET_UTF8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
