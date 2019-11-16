package com.example.comprehensiveapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprehensiveapplication.activity.ChatUIActivity;
import com.example.comprehensiveapplication.R;
import com.example.comprehensiveapplication.data.bean.MessageBar;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MessageBarAdapter extends RecyclerView.Adapter<MessageBarAdapter.ViewHolder> {

    private List<MessageBar> messageBarList;

    private Context context;

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

    public MessageBarAdapter(List<MessageBar> messageBarList, Context context) {
        this.messageBarList = messageBarList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_bar_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(view.getContext(), ChatUIActivity.class);
                        intent.putExtra("requestType", 0);
                        intent.putExtra("account", context.getSharedPreferences("loginUser", MODE_PRIVATE).getString("account", ""));
                        intent.putExtra("receiver", 1);
                        view.getContext().startActivity(intent);
                    case 1:
                        intent = new Intent(view.getContext(), ChatUIActivity.class);
                        intent.putExtra("requestType", 3);
                        intent.putExtra("account", context.getSharedPreferences("loginUser", MODE_PRIVATE).getString("account", ""));
                        intent.putExtra("receiver", 1);
                        view.getContext().startActivity(intent);
                        break;
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageBar messageBar = messageBarList.get(position);
        holder.profilePhotoId.setImageResource(messageBar.getProfilePhotoId());
        holder.avatar.setText(messageBar.getAvatar());
        holder.messageOutline.setText(messageBar.getMessageOutline());
        holder.receivedTime.setText(messageBar.getReceivedTime());
        holder.messageNum.setText(String.valueOf(messageBar.getMessageNum()));

    }

    @Override
    public int getItemCount() {
        return messageBarList.size();
    }
}
