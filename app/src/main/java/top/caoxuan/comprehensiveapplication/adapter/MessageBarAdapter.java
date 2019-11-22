package top.caoxuan.comprehensiveapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.caoxuan.comprehensiveapplication.ChatGroupUIActivity;
import top.caoxuan.comprehensiveapplication.ChatWithFriendUIActivity;
import top.caoxuan.comprehensiveapplication.R;
import top.caoxuan.comprehensiveapplication.data.bean.MessageBar;

public class MessageBarAdapter extends RecyclerView.Adapter<MessageBarAdapter.ViewHolder> {

    final private int TYPE_FRIEND = 1;

    final private int TYPE_GROUP = 2;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_bar_item, parent, false);
        switch (viewType) {
            case TYPE_FRIEND:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_message_bar_item, parent, false);
                break;
            case TYPE_GROUP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_message_bar_item, parent, false);
                break;
            default:
                break;

        }
        final ViewHolder holder = new ViewHolder(view);
        /*final int mViewType = viewType;
        final View mView = view;
         holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                Class<?> cls = new Class<>();
                switch (mViewType) {
                    case TYPE_FRIEND:
                        cls = ChatUIActivity.class;
                        break;
                    case TYPE_GROUP:
                        cls = ChatGroupUIActivity.class;
                        break;
                        default:
                            Log.d("CXException","viewType错误");
                            break;
                }
                    Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(mView.getContext(), cls);
                        intent.putExtra("request_type", 0);
                        intent.putExtra("receiver", 0);
                        mView.getContext().startActivity(intent);
                    case 1:
                        intent = new Intent(mView.getContext(), cls);
                        intent.putExtra("request_type", 3);
                        intent.putExtra("receiver", 1);
                        mView.getContext().startActivity(intent);
                        break;
                }

            }
        });*/
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        MessageBar messageBar = messageBarList.get(position);
        holder.profilePhotoId.setImageResource(messageBar.getProfilePhotoId());
        holder.avatar.setText(messageBar.getAvatar());
        holder.messageOutline.setText(messageBar.getMessageOutline());
        holder.receivedTime.setText(messageBar.getReceivedTime());
        holder.messageNum.setText(String.valueOf(messageBar.getMessageNum()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int mViewType = holder.getItemViewType();
                final int mPosition = position;
                Class<?> cls = null;
                switch (mViewType) {
                    case TYPE_FRIEND:
                        cls = ChatWithFriendUIActivity.class;
                        break;
                    case TYPE_GROUP:
                        cls = ChatGroupUIActivity.class;
                        break;
                    default:
                        Log.d("CXException", "MessageBarAdapter:barType错误");
                        break;
                }
                Class<?> finalCls = cls;

                Intent intent;
                switch (mPosition) {
                    case 0:
                        intent = new Intent(context, finalCls);
                        intent.putExtra("request_type", 0);
                        //intent.putExtra("self", context.getSharedPreferences("UserProfile", MODE_PRIVATE).getInt("self", 0));
                        intent.putExtra("receiver", 0);
                        context.startActivity(intent);
                    case 1:
                        intent = new Intent(context, finalCls);
                        intent.putExtra("request_type", 3);
                        //intent.putExtra("self", context.getSharedPreferences("UserProfile", MODE_PRIVATE).getInt("self", 0));
                        intent.putExtra("groupId", 1);
                        context.startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(context, finalCls);
                        intent.putExtra("request_type", 3);
                        context.startActivity(intent);
                        break;
                }
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        MessageBar messageBar = messageBarList.get(position);
        return messageBar.getViewType();
    }

    @Override
    public int getItemCount() {
        return messageBarList.size();
    }
}
