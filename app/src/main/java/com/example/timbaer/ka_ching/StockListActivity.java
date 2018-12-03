package com.example.timbaer.ka_ching;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import android.widget.Toast;

public class StockListActivity extends AppCompatActivity {
    ExpandableListView expandableListView;

    List<String> companies;
    Map<String, List<String>> addInfo;
    ExpandableListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_list);

        expandableListView = (ExpandableListView) findViewById(R.id.dynamic);
        fillData();

        listAdapter = new MyExListAdapter(this, companies, addInfo);
        expandableListView.setAdapter(listAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(StockListActivity.this,
                        companies.get(groupPosition) + " : " + addInfo.get(companies.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void fillData() {
        companies = new ArrayList<>();
        addInfo = new HashMap<>();

        companies.add("GOOG");
        companies.add("AAPL");

        List<String> GOOG = new ArrayList<>();
        List<String> AAPL = new ArrayList<>();

        GOOG.add("Chart");
        GOOG.add("addInfo");
        GOOG.add("Button");
        AAPL.add("Chart");
        AAPL.add("addInfo");
        AAPL.add("Button");

        addInfo.put(companies.get(0),GOOG);
        addInfo.put(companies.get(1),AAPL);

    }
}
