package com.example.comprehensiveapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprehensiveapplication.R;
import com.example.comprehensiveapplication.data.bean.MessageBar;

import java.util.List;

public class MessageBarAdapter extends RecyclerView.Adapter<MessageBarAdapter.ViewHolder> {

    private List<MessageBar> messageBarList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View messageBar;
        ImageView profilePhotoId;
        TextView avatar;
        TextView messageOutline;
        TextView receivedTime;
        TextView messageNum;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageBar = itemView.findViewById(R.id.message_bars);
            profilePhotoId = itemView.findViewById(R.id.profile_photo);
            avatar = itemView.findViewById(R.id.avatar);
            messageOutline = itemView.findViewById(R.id.message_outline);
            receivedTime = itemView.findViewById(R.id.received_time);
            messageNum = itemView.findViewById(R.id.message_num);
        }
    }
    public MessageBarAdapter(List<MessageBar> messageBarList) {
        this.messageBarList = messageBarList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_bar_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageBar messageBar = messageBarList.get(position);
        holder.profilePhotoId.setImageResource(messageBar.getProfilePhotoId());
        holder.avatar.setText(messageBar.getAvatar());

    }

    @Override
    public int getItemCount() {
        return messageBarList.size();
    }
}
