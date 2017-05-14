package com.example.panqian.downloaddemoapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by panqian on 2017/5/14.
 */

public class DownloadService extends IntentService {
    private static final String ACTION_DOWNLOAD = "com.example.panqian.downloaddemoapp.action.DOWNLOAD";
    public static final String EXTRA_TAG = "com.example.panqian.downloaddemoapp.extra.TAG";

    public static final String ACTION_BROADCAST="com.example.panqian.downloaddemoapp.action.DONE";
    public DownloadService(){
        super("DownloadService");
    }

    public static void startToDownload(Context context,String tag){
        Intent intent=new Intent(context,DownloadService.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_TAG,tag);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("DownloadService","onCreate  pId="+Process.myPid()+"  tId="+Process.myTid());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DownloadService","onDestroy    pId="+Process.myPid()+"    tId="+ Process.myTid());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (null!=intent){
            final String action=intent.getAction();
            final String tag=intent.getStringExtra(EXTRA_TAG);
            Log.d("DownloadService","action= "+action+"   tag="+tag+"    pId="+Process.myPid()+"    tId="+ Process.myTid());
            if (ACTION_DOWNLOAD.equals(action)){
                handleDownload(tag);
            }
        }
    }

    private void handleDownload(String tag){
        try {
            Thread.sleep(5000);
            //notify ui
            Intent intent=new Intent(ACTION_BROADCAST);
            intent.putExtra(EXTRA_TAG,tag);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
