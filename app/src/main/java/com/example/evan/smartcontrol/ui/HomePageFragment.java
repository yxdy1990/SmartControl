package com.example.evan.smartcontrol.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evan.smartcontrol.R;
import com.orhanobut.logger.Logger;
//import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import com.haier.uhome.usdk.api.uSDKManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment {
    private Unbinder unbinder;
    private TextView sdkVersion;

    @OnClick(R.id.local_btn)
    public void onLocalBtnClick(View view) {
        Logger.d("Local Device List Btn Clicked.");
    }

    @OnClick(R.id.cloud_btn)
    public void onCloudBtnClick(View view) {
        Logger.d("Cloud Device List Btn Clicked.");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.d("HomePageFragment onCreate.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.d("HomePageFragment onCreateView.");

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        unbinder = ButterKnife.bind(this, view);
        sdkVersion = (TextView) view.findViewById(R.id.sdk_version);
        sdkVersion.setText("uSDK Version: " + uSDKManager.getSingleInstance().getuSDKVersion());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Logger.d("HomePageFragment onAttach.");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logger.d("HomePageFragment onDetach.");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Logger.d("HomePageFragment onDestroyView.");
        unbinder.unbind();
    }
}
