package top.caoxuan.comprehensiveapplication.adapter;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.caoxuan.comprehensiveapplication.base.BaseInterface;
import top.caoxuan.comprehensiveapplication.R;
import top.caoxuan.comprehensiveapplication.data.bean.IMessage;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> implements BaseInterface {

    private List<IMessage> IMessageList;

    private SharedPreferences pref;

    public MessageAdapter(List<IMessage> IMessageList, SharedPreferences pref) {
        this.IMessageList = IMessageList;
        this.pref = pref;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout leftLayout;
        TextView leftMsg;
        ImageView leftAvatar;
        TextView leftNickname;

        ConstraintLayout rightLayout;
        TextView rightMsg;
        ImageView rightAvatar;
        TextView rightNickname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftLayout = itemView.findViewById(R.id.left_layout);
            leftMsg = itemView.findViewById(R.id.left_msg);
            leftAvatar = itemView.findViewById(R.id.left_avatar);
            leftNickname = itemView.findViewById(R.id.left_nickname);

            rightLayout = itemView.findViewById(R.id.right_layout);
            rightMsg = itemView.findViewById(R.id.right_msg);
            rightAvatar = itemView.findViewById(R.id.right_avatar);
            rightNickname = itemView.findViewById(R.id.right_nickname);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item2, parent, false);
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IMessage IMessage = IMessageList.get(position);
        if (IMessage.getAction() == ACTION_MESSAGE_RECEIVE) {
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.VISIBLE);

            holder.leftMsg.setText(IMessage.getMessage());
        } else if (IMessage.getAction() == ACTION_MESSAGE_SEND) {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);

            holder.rightMsg.setText(IMessage.getMessage());
            holder.rightNickname.setText(pref.getString("Nickname", ""));

            //holder.rightNickname.
        }
    }

    @Override
    public int getItemCount() {
        return IMessageList.size();
    }


}
