package willem.weiyu.casual.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 10:50
 */
public class ChannelRepository {
    private Map<String, Channel> channelMap = new ConcurrentHashMap();

    public ChannelRepository put(String key, Channel channel){
        channelMap.putIfAbsent(key, channel);
        return this;
    }

    public Channel get(String key){
        return channelMap.get(key);
    }

    public void remove(String key){
        channelMap.remove(key);
    }

    public int size(){
        return channelMap.size();
    }
}
