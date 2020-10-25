package com.example.asyu.draggridview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DragGridViewAdapter extends BaseAdapter {

    private List<String> list;

    private int hidePos = AdapterView.INVALID_POSITION;

    public DragGridViewAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder {
        TextView txv;
    }

    //隐藏item
    public void hideView(int pos) {
        hidePos = pos;
        notifyDataSetChanged();
    }

    //显示隐藏了的item
    public void showHideView() {
        hidePos = AdapterView.INVALID_POSITION;
        notifyDataSetChanged();
    }

    //处理当item的位置发生改变时，更新显示
    public void updataView(int oldPos, int newPos) {
        if (oldPos > newPos) {//item从后往前拖动
            list.add(newPos, list.get(oldPos));
            list.remove(oldPos + 1);
        } else if (oldPos < newPos) {//item从前往后拖动
            list.add(newPos + 1, list.get(oldPos));
            list.remove(oldPos);
        }
        hidePos = newPos;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drag_grid_item, null);
            viewHolder.txv = (TextView) view.findViewById(R.id.txv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (i != hidePos) {
            viewHolder.txv.setText(list.get(i));
        } else {
            viewHolder.txv.setText("");
        }
        return view;
    }
}