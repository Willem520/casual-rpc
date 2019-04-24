package willem.weiyu.casual.protocol;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 10:00
 */

public interface CasualSerializer {
    <T> byte[] serialize(T obj);
    <T> T deSerialize(byte[] data, Class<T> clz);
}
