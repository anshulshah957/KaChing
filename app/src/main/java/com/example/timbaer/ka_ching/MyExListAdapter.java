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
import java.util.ArrayList;
import com.jjoe64.graphview.GraphView;
import android.view.View;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MyExListAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> companies;
    Map<String, List<Integer>> addInfo;
    Typeface custom_font;
    List<String> arrayList;
    boolean isFirst = true;

    public MyExListAdapter(Context context, List<String> companies, Map<String, List<Integer>> addInfo) {
        this.context = context;
        this.companies = companies;
        this.addInfo = addInfo;
        try {
            custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/large_font.ttf");
        } catch (Exception e) {
            Log.d("MyExListAdapter", e.toString());
        }
        if (isFirst) {
            this.arrayList = new ArrayList<String>();
            this.arrayList.addAll(companies);
            isFirst = false;
        }
    }

    public void filter(String query) {
        companies.clear();
        if (query.isEmpty()) {
            companies.addAll(arrayList);
        }
        else {
            for (String company : arrayList) {
                if (company.contains(query)) {
                    companies.add(company);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return companies.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
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
        final List<Integer> addedInfo = addInfo.get(companies.get(groupPosition));

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_child, null);
        }
        final GraphView graph = (GraphView) convertView.findViewById(R.id.graph);
        graph.removeAllSeries();
        DataPoint[] toAdd = new DataPoint[addedInfo.size()];
        for (int i = 0; i < toAdd.length; i++) {
            toAdd[i] = new DataPoint(i, addedInfo.get(i));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(toAdd);
        graph.addSeries(series);
        graph.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Audio audio = new Audio();
                Thread audioPlayer = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        audio.playData(addedInfo);
                    }
                });
                audioPlayer.start();
                try {
                    audioPlayer.join();
                } catch (InterruptedException e) {
                    Log.d("AudioInterrupt",e.toString());
                }

            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
