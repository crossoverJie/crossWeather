package top.crossoverjie.app.uitl;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Administrator on 2016/8/8.
 */
public class DialogUitl {

    public static void showDialog(final Context context){
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("请连接网络")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NetworkUtils.openWirelessSettings(context);
                    }
                })
                .create()
                .show();
    }
}
