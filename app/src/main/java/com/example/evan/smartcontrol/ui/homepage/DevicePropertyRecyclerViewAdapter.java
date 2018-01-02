package com.example.evan.smartcontrol.ui.homepage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evan.smartcontrol.R;
import com.haier.uhome.usdk.api.uSDKDeviceAttribute;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2017/12/30.
 */

public class DevicePropertyRecyclerViewAdapter extends RecyclerView.Adapter<DevicePropertyRecyclerViewAdapter.ViewHolder> {
    private List<uSDKDeviceAttribute> mValues;
    private OnListFragmentInteractionListener mListListener;

    public DevicePropertyRecyclerViewAdapter(List<uSDKDeviceAttribute> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListListener = listener;
    }

    public void setRecyclerViewDataSource(List<uSDKDeviceAttribute> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_device_control_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.attrObj = mValues.get(position);
        holder.attrName.setText(mValues.get(position).getName());
        holder.attrValue.setText(mValues.get(position).getValue());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListListener != null) {
                    mListListener.onListFragmentInteraction(mValues.get(position).getName());
                } else {
                    Logger.e("Property List Listener is Null!");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mValues != null) {
            return mValues.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView attrName;
        public final TextView attrValue;
        public uSDKDeviceAttribute attrObj;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            attrName = view.findViewById(R.id.list_property_name);
            attrValue = view.findViewById(R.id.list_property_value);
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(String attrName);
    }
}
