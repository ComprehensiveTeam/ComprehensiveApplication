package com.example.comprehensiveapplication.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprehensiveapplication.LBSActivity;
import com.example.comprehensiveapplication.R;
import com.example.comprehensiveapplication.data.bean.Tool;

import java.util.List;

public class ToolAdapter extends RecyclerView.Adapter<ToolAdapter.ViewHolder> {

    private List<Tool> mToolList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View toolView;
        ImageView toolImage;
        TextView toolName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            toolView = itemView;
            toolImage = itemView.findViewById(R.id.tool_image);
            toolName = itemView.findViewById(R.id.tool_name);
        }
    }

    public ToolAdapter(List<Tool> ToolList) {
        this.mToolList = ToolList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tool_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.toolView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (0 == position) {
                    Intent intent = new Intent(view.getContext(), LBSActivity.class);
                    view.getContext().startActivity(intent);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tool tool = mToolList.get(position);
        holder.toolImage.setImageResource(R.mipmap.ic_launcher_round);
        holder.toolName.setText(tool.getToolName());

    }

    @Override
    public int getItemCount() {
        return mToolList.size();
    }

}
