package com.example.evan.smartcontrol.ui.homepage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.MenuItem;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.evan.smartcontrol.R;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UdpBroadcastActivity extends AppCompatActivity {
    private boolean handleStatus = false;
    private String currentFromIPAddr = "";
    final String HANDLER_PARAM_KEY1 = "SourceIPAddr";
    final String HANDLER_PARAM_KEY2 = "RecvedUdpData";
    final MyHandler handler = new MyHandler(this);

    @BindView(R.id.udp_msg_view)
    TextView mTextView;

    @OnClick(R.id.udp_send_btn)
    public void onSendBtnClick(View view) {
        if (handleStatus == false) {
            UdpThread thread = new UdpThread();

            refreshMsgView("接收到的数据: \n", false);
            handleStatus = true;
            thread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp_broadcast);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);

        mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class MyHandler extends Handler {
        private final WeakReference<UdpBroadcastActivity> mActivity;

        public MyHandler(UdpBroadcastActivity activity) {
            mActivity = new WeakReference<UdpBroadcastActivity>(activity);
        }

        public void handleMessage(android.os.Message msg) {
            UdpBroadcastActivity activity = mActivity.get();

            if (activity != null) {
                String ip = msg.getData().getString(activity.HANDLER_PARAM_KEY1);;
                String data = msg.getData().getString(activity.HANDLER_PARAM_KEY2);

                activity.currentFromIPAddr = ip;
                activity.refreshMsgView(data, true);
            }
        }
    }

    private class UdpThread extends Thread {
        @Override
        public void run() {
            Logger.d("UdpThread start run...");

            udpHandler("{\"cmd\": \"GetDevsName\",\"dir\": \"request\"}");
        }
    }

    private void udpHandler(String sndMsg) {
        DatagramSocket socket = null;
        int port = 49001;
        try {
            socket = new DatagramSocket(port);
            socket.setBroadcast(true);
            socket.setReuseAddress(true);
            socket.setSoTimeout(3000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        byte[] sndBytes = sndMsg.getBytes();
        byte[] rcvBytes = new byte[1024 * 20]; // 20K
        DatagramPacket sndPacket = new DatagramPacket(sndBytes, sndMsg.length(), addr, port);
        DatagramPacket rcvPacket = new DatagramPacket(rcvBytes, rcvBytes.length);
        try {
            socket.send(sndPacket);
            Logger.d("UDP Socket Send : \n" + sndMsg);
            while (true) {
                try {
                    socket.receive(rcvPacket);
                    String recvData = new String(rcvPacket.getData()).trim();
                    String ipAddr = rcvPacket.getAddress().getHostAddress().toString();
                    Logger.d("UDP Socket Recv from: " + ipAddr + "\n" + recvData);

                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString(HANDLER_PARAM_KEY1, ipAddr);
                    bundle.putString(HANDLER_PARAM_KEY2, recvData);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    Logger.d("Message sended.");
                } catch (SocketTimeoutException e) {
                    Logger.d("Recv timeout, exit!");
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handleStatus = false;
    }

    private void refreshMsgView(String msg, boolean json) {
        if (msg != null) {
            if (json) {
                mTextView.append(parseJsonData(msg));
            } else {
                mTextView.setText(msg);
            }
        }
    }

    private String parseJsonData(String jsonStr) {
        JsonReader reader = new JsonReader(new StringReader(jsonStr));
        StringBuffer buffer = new StringBuffer("");
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String key = reader.nextName();
                if ("dir".equals(key)) {
                    String dir = reader.nextString();
                    if (!"response".equals(dir)) {
                        break;
                    }
                } else if ("sendId".equals(key)) {
                    String mac = reader.nextString();
                    buffer.append("目标网关: " + mac + ",  IP: " + currentFromIPAddr + "-->\n");
                } else if ("list".equals(key)) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();
                        while (reader.hasNext()) {
                            String subKey = reader.nextName();
                            if ("devId".equals(subKey)) {
                                String devId = reader.nextString();
                                buffer.append("DevId: " + devId + ",  ");
                            } else if ("name".equals(subKey)) {
                                String name = reader.nextString();
                                buffer.append("Name: " + name + "\n");
                            }  else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
            buffer.append("\n");
            reader.endObject();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
