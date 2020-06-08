package com.slave_mk14.ssssalldelete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DriveListAdapter extends BaseAdapter {
    private ArrayList<DriveListItem> item;

    public DriveListAdapter(ArrayList<DriveListItem> item)
    {
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drive_list_item, viewGroup, false);
        }

        TextView name = view.findViewById(R.id.drive);
        TextView path = view.findViewById(R.id.drivePath);

        name.setText(((DriveListItem) getItem(i)).getDriveName() + " ( " + ((DriveListItem) getItem(i)).getFileSize(((DriveListItem) getItem(i)).getDriveFullSize() - ((DriveListItem) getItem(i)).getDriveFreeSize()) + " / " + ((DriveListItem) getItem(i)).getFileSize(((DriveListItem) getItem(i)).getDriveFullSize()) + " )");
        path.setText(((DriveListItem) getItem(i)).getDrivePath());

        return view;
    }
}
