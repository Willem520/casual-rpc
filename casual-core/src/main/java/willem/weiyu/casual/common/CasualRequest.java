package willem.weiyu.casual.common;

import lombok.Data;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/23 15:52
 */
@Data
public class CasualRequest {
    /**
     * 调用编号
     */
    private String requestId;
    /**
     * 类名
     */
    private String interfaceName;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 请求参数类型
     */
    private Class<?>[] parameterType;
    /**
     * 请求参数
     */
    private Object[] parameters;
    /**
     * 版本
     */
    private String version;
}
