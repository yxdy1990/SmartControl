package com.example.evan.smartcontrol.ui.homepage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evan.smartcontrol.ui.homepage.LocalDeviceFragment.OnListFragmentInteractionListener;
import com.example.evan.smartcontrol.R;
import com.example.evan.smartcontrol.util.ApplicationUtil;
import com.haier.uhome.usdk.api.uSDKDevice;
import com.orhanobut.logger.Logger;

import java.util.List;


public class LocalDeviceRecyclerViewAdapter extends RecyclerView.Adapter<LocalDeviceRecyclerViewAdapter.ViewHolder> {

    private List<uSDKDevice> mValues;
    private final OnListFragmentInteractionListener mListener;

    public LocalDeviceRecyclerViewAdapter(List<uSDKDevice> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void setDataSource(List<uSDKDevice> items) {
        if (items != null) {
            mValues = items;
        } else {
            Logger.e("RecyclerViewAdapter: Null DataSource!");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_localdevice_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mDevItem = mValues.get(position);
        holder.mTypeView.setText(ApplicationUtil.getuSDKDeviceType(mValues.get(position)));
        holder.mStatusView.setText(ApplicationUtil.getuSDKDeviceStatus(mValues.get(position)));
        holder.mMacView.setText(mValues.get(position).getDeviceId());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mDevItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTypeView;
        public final TextView mStatusView;
        public final TextView mMacView;
        public uSDKDevice mDevItem;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            mTypeView = (TextView) view.findViewById(R.id.device_type);
            mStatusView = (TextView) view.findViewById(R.id.device_status);
            mMacView = (TextView) view.findViewById(R.id.device_mac);
        }
    }
}
