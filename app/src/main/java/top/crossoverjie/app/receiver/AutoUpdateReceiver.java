package top.crossoverjie.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import top.crossoverjie.app.service.AutoUpdateService;

/**
 * Created by Administrator on 2016/8/8.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateService.class) ;
        context.startService(i) ;
    }
}
