package com.example.comprehensiveapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprehensiveapplication.R;
import com.example.comprehensiveapplication.adapter.MessageBarAdapter;
import com.example.comprehensiveapplication.adapter.ToolAdapter;
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
        MessageBarAdapter adapter = new MessageBarAdapter(messageBarList);
        recyclerView.setAdapter(adapter);
        return root;
    }

    private void initMessageBar() {
        for (int i = 0; i < 16; i++) {
            MessageBar messageBar = new MessageBar(R.mipmap.ic_launcher,"小明的亲戚","在吗？","21:21",1);
            messageBarList.add(messageBar);
        }
    }
}