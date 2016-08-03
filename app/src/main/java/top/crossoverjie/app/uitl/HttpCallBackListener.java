package top.crossoverjie.app.uitl;

/**
 * Created by Administrator on 2016/8/3.
 */
public interface HttpCallBackListener {
    public void onFinish(String response) ;
    public void onError(Exception e) ;
}
