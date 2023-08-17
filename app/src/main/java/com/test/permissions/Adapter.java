package com.test.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.permissions.MyPermission;
import com.github.permissions.PermissionCallback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<PermissionItem> list = new ArrayList<>();

    public void setList(List<PermissionItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater from = LayoutInflater.from(parent.getContext());
        return new ViewHolder(from.inflate(R.layout.permission_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PermissionItem permissionItem = list.get(holder.getAdapterPosition());
        StringBuilder builder = new StringBuilder();
        List<String> list = new ArrayList<>();
        List<PermissionItem.Item> permission = permissionItem.getPermission();
        for (PermissionItem.Item item : permission) {
            list.add(item.permission);
            item.hasPermission = MyPermission.hasPermission(holder.itemView.getContext(), item.permission);
            builder.append(item.hasPermission ? "有" : "无");
            builder.append("权限:");
            builder.append(item.permission.replace("android.permission.", ""));
            builder.append("\n");
        }
        holder.tvPermission.setText(builder.toString());
        holder.btRequest.setText("请求权限" + permissionItem.getRequestCount() + "次");
        holder.btRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPermission.get(findActivity(holder.itemView.getContext())).request(list, new PermissionCallback() {
                    @Override
                    public void agreeAll(List<String> agreeList) {
                        List<PermissionItem.Item> permission = new ArrayList<>();
                        permissionItem.setPermission(permission);
                        permissionItem.addRequestCount();
                        Log.i("=====", "=========================agreeAll=========================");
                        for (String strItem : agreeList) {
                            Log.i("=====", "===同意==" + strItem);
                            PermissionItem.Item pair = new PermissionItem.Item(strItem, true);
                            permission.add(pair);
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void denied(List<String> agreeList, List<String> deniedList) {
                        permissionItem.addRequestCount();
                        List<PermissionItem.Item> permission = new ArrayList<>();
                        permissionItem.setPermission(permission);
                        Log.i("=====", "========================denied==========================");
                        for (String strItem : agreeList) {
                            Log.i("=====", "===同意==" + strItem);
                            permission.add(new PermissionItem.Item(strItem, true));
                        }
                        for (String strItem : deniedList) {
                            Log.i("=====", "===拒绝==" + strItem);
                            permission.add(new PermissionItem.Item(strItem, false));
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public static Activity findActivity(Context context) {
        if (context == null) {
            return null;
        } else {
            if (context instanceof Activity) {
                return (Activity) context;
            } else if (context instanceof ContextWrapper) {
                ContextWrapper wrapper = (ContextWrapper) context;
                return findActivity(wrapper.getBaseContext());
            } else {
                return null;
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPermission;
        public Button btRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPermission = itemView.findViewById(R.id.tvPermission);
            btRequest = itemView.findViewById(R.id.btRequest);
        }
    }
}
