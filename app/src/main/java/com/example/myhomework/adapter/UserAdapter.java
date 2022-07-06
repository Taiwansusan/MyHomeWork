package com.example.myhomework.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhomework.R;
import com.example.myhomework.model.UserData;
import com.example.myhomework.ui.LogoView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<UserData> items;
    private LayoutInflater mInflater;
    Context context;
    ItemListener itemListener;

    public UserAdapter(Context context, ArrayList<UserData> mImgarraylist , ItemListener itemListener){
        this.context = context;
        this.items = mImgarraylist;
        this.itemListener = itemListener;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.user_item, parent, false));
    }
    //LinkedTreeMap
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mHolder, final int i) {
        MyViewHolder holder = (MyViewHolder)mHolder;
        UserData userData = items.get(mHolder.getAdapterPosition());
        holder.mImageView.setImage(userData.getAvatar_url(), LogoView.CIRCLEIMAGE);

        holder.mUserName.setText(userData.getLogin());
        if (userData.isSite_admin()) {
            holder.mUserStaff.setVisibility(View.VISIBLE);
        } else {
            holder.mUserStaff.setVisibility(View.GONE);
        }
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.ItemListener(mHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mUserName;
        LogoView mImageView;
        LinearLayout mLinearLayout;
        TextView mUserStaff;

        public MyViewHolder(View itemView) {
            super(itemView);
            mLinearLayout = itemView.findViewById(R.id.user_layout);
            mImageView = itemView.findViewById(R.id.user_image);
            mUserName = itemView.findViewById(R.id.user_name);
            mUserStaff = itemView.findViewById(R.id.user_staff);

        }
    }
}
