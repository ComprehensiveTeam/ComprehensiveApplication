package com.example.comprehensiveapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprehensiveapplication.R;
import com.example.comprehensiveapplication.adapter.MessageBarAdapter;
import com.example.comprehensiveapplication.data.bean.MessageBar;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    //private HomeViewModel homeViewModel;

    private List<MessageBar> messageBarList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);*/
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        initMessageBar();
        RecyclerView recyclerView = root.findViewById(R.id.message_bars);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        MessageBarAdapter adapter = new MessageBarAdapter(messageBarList, this.getActivity());
        recyclerView.setAdapter(adapter);
        return root;
    }

    private void initMessageBar() {
        MessageBar messageBar;
        messageBar = new MessageBar(R.mipmap.ic_launcher, "系统消息", "系统给你发来了一条消息", "18:59", 1);
        messageBarList.add(messageBar);
        messageBar = new MessageBar(R.mipmap.ic_launcher, "公共频道", "有人给你发来了一条消息", "18:59", 1);
        messageBarList.add(messageBar);
        for (int i = 2; i < 16; i++) {
            messageBar = new MessageBar(R.mipmap.ic_launcher, "小明的亲戚", "在吗？", "21:21", 1);
            messageBarList.add(messageBar);
        }
    }
}