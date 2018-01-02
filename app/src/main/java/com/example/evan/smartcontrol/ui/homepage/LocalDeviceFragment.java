package com.example.evan.smartcontrol.ui.homepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.evan.smartcontrol.util.AppConstant;
import com.haier.uhome.usdk.api.interfaces.IuSDKDeviceManagerListener;
import com.haier.uhome.usdk.api.uSDKCloudConnectionState;
import com.haier.uhome.usdk.api.uSDKDevice;
import com.haier.uhome.usdk.api.uSDKDeviceManager;
import com.orhanobut.logger.Logger;

import com.example.evan.smartcontrol.R;
import com.example.evan.smartcontrol.ui.homepage.dummy.DummyContent;
import com.example.evan.smartcontrol.ui.homepage.dummy.DummyContent.DummyItem;

import java.util.List;


/**
 * A fragment representing a list of Items.
 */
public class LocalDeviceFragment extends Fragment {
    private IuSDKDeviceManagerListener usdkDeviceListener;
    private OnListFragmentInteractionListener mListener;
    private LocalDeviceRecyclerViewAdapter mListAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocalDeviceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usdkDeviceListener = new IuSDKDeviceManagerListener() {
            @Override
            public void onDeviceBind(String devsChanged) {
                Logger.d("onDeviceBind: " + devsChanged);
            }

            @Override
            public void onDeviceUnBind(String devsChanged) {
                Logger.d("onDeviceUnBind: " + devsChanged);
            }

            @Override
            public void onDevicesAdd(List<uSDKDevice> devsChanged) {
                onDeviceListDataUpdated();
            }

            @Override
            public void onDevicesRemove(List<uSDKDevice> devsChanged) {
                onDeviceListDataUpdated();
            }

            @Override
            public void onCloudConnectionStateChange(uSDKCloudConnectionState state) {
                Logger.d("onCloudConnectionStateChange: " + state);
            }
        };
        uSDKDeviceManager.getSingleInstance().setDeviceManagerListener(usdkDeviceListener);
        mListener = new OnListFragmentInteractionListener();
        mListAdapter = new LocalDeviceRecyclerViewAdapter(uSDKDeviceManager.getSingleInstance().getDeviceList(), mListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_localdevice, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mListAdapter);
        }
        Logger.d("LocalDeviceFragment onCreateView.");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Logger.d("LocalDeviceFragment onAttach.");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onDeviceListDataUpdated() {
        if (mListAdapter != null) {
            List<uSDKDevice> deviceList = uSDKDeviceManager.getSingleInstance().getDeviceList();
            mListAdapter.setDataSource(deviceList);
            mListAdapter.notifyDataSetChanged();
        }
    }

    public class OnListFragmentInteractionListener {
        void onListFragmentInteraction(uSDKDevice deviceObj) {
            String deviceId = deviceObj.getDeviceId();
            Intent intent = new Intent(getActivity(), DeviceControlActivity.class);
            Bundle bundle = new Bundle();

            bundle.putString(AppConstant.DEVICE_ID, deviceId);
            intent.putExtras(bundle);
            startActivity(intent);
            Logger.d("start DeviceControlActivity.");
        }
    }
}
