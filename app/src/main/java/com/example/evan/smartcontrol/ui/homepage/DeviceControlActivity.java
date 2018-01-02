package com.example.evan.smartcontrol.ui.homepage;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evan.smartcontrol.R;
import com.example.evan.smartcontrol.util.AppConstant;
import com.example.evan.smartcontrol.util.ApplicationUtil;
import com.haier.uhome.usdk.api.interfaces.IuSDKCallback;
import com.haier.uhome.usdk.api.interfaces.IuSDKDeviceListener;
import com.haier.uhome.usdk.api.uSDKDeviceAlarm;
import com.haier.uhome.usdk.api.uSDKDeviceAttribute;
import com.haier.uhome.usdk.api.uSDKDeviceManager;
import com.haier.uhome.usdk.api.uSDKDevice;

import com.haier.uhome.usdk.api.uSDKDeviceStatusConst;
import com.haier.uhome.usdk.api.uSDKErrorConst;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceControlActivity extends AppCompatActivity implements DevicePropertyRecyclerViewAdapter.OnListFragmentInteractionListener {
    private uSDKDevice usdkDeviceObj;
    private DevicePropertyRecyclerViewAdapter mListAdapter;

    @BindView(R.id.ctrl_property_name)
    public EditText propertyNameField;

    @BindView(R.id.ctrl_property_value)
    public EditText propertyValueField;

    @BindView(R.id.ctrl_device_status)
    public TextView deviceStatusLabel;

    @BindView(R.id.ctrl_property_list)
    public RecyclerView propertyList;

    @OnClick(R.id.ctrl_set_attr_button)
    public void onSetAttrButtonClicked(View view) {
        String attrName = propertyNameField.getText().toString();
        String attrValue = propertyValueField.getText().toString();
        final Context contex = view.getContext();

        if (usdkDeviceObj != null && attrName != null && attrValue != null) {
            usdkDeviceObj.writeAttribute(attrName, attrValue, 10, new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst uSDKErrorConst) {
                    if (uSDKErrorConst != uSDKErrorConst.RET_USDK_OK) {
                        Toast.makeText(contex, "设备操作失败:" + uSDKErrorConst.getValue(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(contex, "设备操作成功", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devcie_control);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            String deviceId = bundle.getString(AppConstant.DEVICE_ID);
            Logger.d("DevCtrl activity onCreate: " + deviceId);
            usdkDeviceObj = uSDKDeviceManager.getSingleInstance().getDevice(deviceId);
        }
        mListAdapter = new DevicePropertyRecyclerViewAdapter(null, this);
        propertyList.setLayoutManager(new LinearLayoutManager(this));
        propertyList.setAdapter(mListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (usdkDeviceObj != null) {
            usdkDeviceObj.setDeviceListener(new IuSDKDeviceListener() {
                @Override
                public void onDeviceAlarm(uSDKDevice uSDKDevice, List<uSDKDeviceAlarm> list) {
                    Logger.d("onDeviceAlarm -->\n" + list);
                }

                @Override
                public void onDeviceAttributeChange(uSDKDevice uSDKDevice, List<uSDKDeviceAttribute> list) {
                    Logger.d("onDeviceAttributeChange -->\n" + list);

                    List<uSDKDeviceAttribute> attrs = new ArrayList<uSDKDeviceAttribute>();
                    attrs.addAll(uSDKDevice.getAttributeMap().values());
                    mListAdapter.setRecyclerViewDataSource(attrs);
                    mListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onDeviceOnlineStatusChange(uSDKDevice uSDKDevice, uSDKDeviceStatusConst uSDKDeviceStatusConst, int i) {
                    Logger.d("onDeviceOnlineStatusChange -->\n", uSDKDeviceStatusConst);
                    deviceStatusLabel.setText(ApplicationUtil.getuSDKDeviceStatus(uSDKDevice));
                }

                @Override
                public void onDeviceBaseInfoChange(uSDKDevice uSDKDevice) {
                    Logger.d("onDeviceBaseInfoChange -->\n" + uSDKDevice.getDeviceId() + uSDKDevice.getIp());
                }

                @Override
                public void onSubDeviceListChange(uSDKDevice uSDKDevice, ArrayList<uSDKDevice> arrayList) {
                    Logger.d("onSubDeviceListChange -->\n", arrayList);
                }
            });

            usdkDeviceObj.connectNeedProperties(new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst uSDKErrorConst) {
                    if (uSDKErrorConst != uSDKErrorConst.RET_USDK_OK) {
                        Logger.e("Connect Device[%s] failed: %s!", usdkDeviceObj.getDeviceId(), uSDKErrorConst.getValue());
                    }
                }
            });
        } else {
            Logger.e("DevCtrl activity onStart: Null Dev Obj!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Logger.d("DevCtrl activity onResume called.");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Logger.d("DevCtrl activity onPause called.");
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (usdkDeviceObj != null) {
            usdkDeviceObj.disconnect(new IuSDKCallback() {
                @Override
                public void onCallback(uSDKErrorConst uSDKErrorConst) {
                    if (uSDKErrorConst != uSDKErrorConst.RET_USDK_OK) {
                        Logger.e("Disconnect Device[%s] failed: %s!", usdkDeviceObj.getDeviceId(), uSDKErrorConst.getValue());
                    }
                }
            });
            usdkDeviceObj.setDeviceListener(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListFragmentInteraction(String attrName) {
        Logger.d("Property List Item selected: " + attrName);
        propertyNameField.setText(attrName);
    }
}
