package com.stone.persistent.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.stone.persistent.recyclerview.adapter.MainListAdapter;

import library2.ParentRecyclerView;

public class MainActivity2 extends AppCompatActivity implements Handler.Callback {
    private static final int MSG_TYPE_REFRESH_FINISHED = 110;
    private static final int MSG_TYPE_TABS_LOADED = 111;

    private MainListAdapter listAdapter;
    private Handler uiHandler;

    private ParentRecyclerView parentRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        uiHandler = new Handler(this);

        parentRecyclerView = findViewById(R.id.main_recycler_view2);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new MainListAdapter(this);
        parentRecyclerView.setAdapter(listAdapter);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uiHandler.sendEmptyMessage(MSG_TYPE_TABS_LOADED);
            }
        });
        findViewById(R.id.floating_view).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                MainActivity2.this.startActivity(intent);
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_TYPE_TABS_LOADED:
                if (listAdapter != null)
                    listAdapter.onTabsLoaded();
        }
        return false;
    }
}
