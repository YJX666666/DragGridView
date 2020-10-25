package com.example.asyu.draggridview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DragGridViewActivity extends AppCompatActivity {

    private DragGridView gvDrag;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_grid_view);
        initView();
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("小黑" + i);
        }
        DragGridViewAdapter adapter = new DragGridViewAdapter(list);
        gvDrag.setAdapter(adapter);
    }

    private void initView() {
        gvDrag = (DragGridView) findViewById(R.id.gv_drag);
    }
}
