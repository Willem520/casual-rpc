package willem.weiyu.casual.common;

import lombok.Data;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/23 15:55
 */
@Data
public class CasualResponse {
    /**
     * 调用编号
     */
    private String requestId;
    /**
     * 抛出的异常
     */
    private Throwable throwable;
    /**
     * 返回结果
     */
    private Object result;
}
