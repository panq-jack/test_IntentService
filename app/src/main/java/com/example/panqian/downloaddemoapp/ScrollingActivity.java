package com.example.panqian.downloaddemoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Process;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScrollingActivity extends AppCompatActivity {
    private LinearLayout container;
    private Button start;
    private int taskNo=0;
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("ScrollingActivity","onReceive  ui thread  pId:  "+ Process.myPid()+"    tId:  "+Process.myTid());
            if (null!=intent){
                String action=intent.getAction();
                if (DownloadService.ACTION_BROADCAST.equals(action)){
                    String tag=intent.getStringExtra(DownloadService.EXTRA_TAG);
                    TextView textView=(TextView)container.findViewWithTag(tag);
                    if (null!=textView){
                        textView.setText("Task "+(tag)+"  download done! ...");
                    }
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ScrollingActivity","onCreate  ui thread  pId:  "+ Process.myPid()+"    tId:  "+Process.myTid());
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        registerBroadcast();
        container=(LinearLayout)findViewById(R.id.container);
        start=(Button)findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskText();
                startTask();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterBroadcast();
    }

    private void registerBroadcast(){
        IntentFilter intentFilter=new IntentFilter(DownloadService.ACTION_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,intentFilter);
    }
    private void unRegisterBroadcast(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    /*
      add a textView showing  " is running"
     */
    private void addTaskText(){
        TextView textView=new TextView(ScrollingActivity.this);
        textView.setText("Task "+(++taskNo)+"  is running ...");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        textView.setTag(String.valueOf(taskNo));
        container.addView(textView);
    }
    private void startTask(){
        DownloadService.startToDownload(ScrollingActivity.this,String.valueOf(taskNo));
    }

}
