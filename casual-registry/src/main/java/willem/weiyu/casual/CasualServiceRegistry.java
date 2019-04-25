package willem.weiyu.casual;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/25 11:57
 */
public interface CasualServiceRegistry {
    /**
     * 注册服务名称与服务地址
     * @param serviceName
     * @param serviceAddress
     */
    void register(String serviceName, String serviceAddress) throws Exception;

    /**
     * 根据服务名称查找服务地址
     * @param serviceName
     * @return
     */
    String discover(String serviceName);
}
