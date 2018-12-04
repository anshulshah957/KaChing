package com.example.timbaer.ka_ching;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.content.Context;
import android.widget.TextView;
import java.util.List;
import java.util.Map;
import android.graphics.Typeface;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.View;


public class MyExListAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> companies;
    Map<String, List<String>> addInfo;
    Typeface custom_font;

    public MyExListAdapter(Context context, List<String> companies, Map<String, List<String>> addInfo) {
        this.context = context;
        this.companies = companies;
        this.addInfo = addInfo;
        try {
            custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/large_font.ttf");
        } catch (Exception e) {
            Log.d("NoFont", e.toString());
        }
    }
    @Override
    public int getGroupCount() {
        return companies.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return addInfo.get(companies.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return companies.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return addInfo.get(companies.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String company = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_parent, null);
        }

        TextView txtParent = (TextView) convertView.findViewById(R.id.txtParent);
        txtParent.setTypeface(custom_font);
        txtParent.setTextSize(25);
        txtParent.setText(company);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String addedInfo = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_child, null);
        }

        TextView txtChild = (TextView) convertView.findViewById(R.id.txtChild);
        txtChild.setTypeface(custom_font);
        txtChild.setTextSize(18);
        txtChild.setText(addedInfo);
        final ImageButton playGraph = (ImageButton) convertView.findViewById(R.id.imageButton);
        playGraph.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Toast.makeText(playGraph.getContext(),"ImageButton Clicked: " + companies.get(groupPosition), Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
