package willem.weiyu.casual;

import willem.weiyu.casual.common.CasualResponse;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 11:46
 */
public class DefaultFuture {
    private CasualResponse response;
    private volatile boolean isSuccess = false;
    private final Object object = new Object();

    public CasualResponse getResponse(int timeout){
        synchronized(object){
            while (!isSuccess){
                try{
                    object.wait(timeout);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    public void setResponse(CasualResponse response){
        if (isSuccess){
            return;
        }
        synchronized(object){
            this.response = response;
            this.isSuccess = true;
            object.notify();
        }
    }
}
