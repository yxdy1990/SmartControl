package com.example.evan.smartcontrol.ui.homepage;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.evan.smartcontrol.MainActivity;
import com.example.evan.smartcontrol.R;
import com.example.evan.smartcontrol.util.AppConstant;


public class DeviceListActivity extends AppCompatActivity {
    public static final int LOCAL_DEV_TYPE = 1;
    public static final int CLOUD_DEV_TYPE = 2;

    private LocalDeviceFragment localDeviceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        initView(this.getIntent().getExtras().getInt(AppConstant.DEVICE_TYPE));
    }

    private void initView(int deviceType) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 显示导航栏
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 显示设备列表
        if (deviceType == LOCAL_DEV_TYPE) {
            if (localDeviceFragment == null) {
                localDeviceFragment = new LocalDeviceFragment();
                transaction.add(R.id.device_list_layout, localDeviceFragment);
            } else {
                transaction.show(localDeviceFragment);
            }
        } else if (deviceType == CLOUD_DEV_TYPE) {
        }
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //returnHome(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void returnHome(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void setFullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
