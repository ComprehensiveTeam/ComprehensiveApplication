package top.caoxuan.comprehensiveapplication.ui.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import top.caoxuan.comprehensiveapplication.R;
import top.caoxuan.comprehensiveapplication.adapter.ToolAdapter;
import top.caoxuan.comprehensiveapplication.data.bean.Tool;

public class ToolsFragment extends Fragment {

    //private ToolsViewModel toolsViewModel;

    private List<Tool> toolList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);*/
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        /*final TextView textView = root.findViewById(R.id.text_tools);
        toolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        /*ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, tools);
        ListView listView = root.findViewById(R.id.list_view);
        listView.setAdapter(adapter);*/
        initTools();
        RecyclerView recyclerView = root.findViewById(R.id.tools_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ToolAdapter adapter = new ToolAdapter(toolList);
        recyclerView.setAdapter(adapter);
        return root;

    }

    private void initTools() {
        toolList.add(new Tool("LBS"));
        for (int i = 0; i<15; i++) {
            Tool tool = new Tool("敬请期待");
            toolList.add(tool);
        }
    }
}