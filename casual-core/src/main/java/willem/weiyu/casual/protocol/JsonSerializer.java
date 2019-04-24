package willem.weiyu.casual.protocol;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 10:03
 */
public class JsonSerializer implements CasualSerializer {

    public <T> byte[] serialize(T obj) {
        return JSONObject.toJSONBytes(obj);
    }

    public <T> T deSerialize(byte[] data, Class<T> clazz) {
        return JSONObject.parseObject(data, clazz);
    }
}
