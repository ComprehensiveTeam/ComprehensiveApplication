package top.caoxuan.comprehensiveapplication.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.caoxuan.comprehensiveapplication.R;
import top.caoxuan.comprehensiveapplication.data.bean.Msg;

@Deprecated
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<Msg> mMsgList;


    public MsgAdapter(List<Msg> msgList) {
        Log.d("cxDebug", "MsgAdapter");

        mMsgList = msgList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //早期
        //LinearLayout leftLayout;
        //LinearLayout rightLayout;
        ConstraintLayout leftLayout;
        ConstraintLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftLayout = itemView.findViewById(R.id.left_layout);
            rightLayout = itemView.findViewById(R.id.right_layout);
            leftMsg = itemView.findViewById(R.id.left_msg);
            rightMsg = itemView.findViewById(R.id.right_msg);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item2, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);
        if (msg.getType() == Msg.TYPE_RECEIVED) {
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.leftMsg.setText(msg.getMessage());
        } else if (msg.getType() == Msg.TYPE_SENT) {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
