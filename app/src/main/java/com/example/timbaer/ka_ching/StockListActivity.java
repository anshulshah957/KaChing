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
import android.content.Intent;
import android.app.SearchManager;

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

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
            Toast.makeText(StockListActivity.this, query, Toast.LENGTH_SHORT).show();
        }
    }

    public void fillData() {
        companies = new ArrayList<>();
        addInfo = new HashMap<>();

        companies.add("GOOG");
        companies.add("AAPL");
        companies.add("Dow Jones");
        companies.add("S&P 500");
        companies.add("BA");
        companies.add("BRK-B");
        companies.add("DIS");
        companies.add("GE");

        List<String> GOOG = new ArrayList<>();
        List<String> AAPL = new ArrayList<>();
        List<String> Dow_Jones = new ArrayList<>();
        List<String> SP_500 = new ArrayList<>();
        List<String> BA = new ArrayList<>();
        List<String> BRKB = new ArrayList<>();
        List<String> DIS = new ArrayList<>();
        List<String> GE = new ArrayList<>();

        GOOG.add("addInfo");
        AAPL.add("addInfo");
        Dow_Jones.add("addInfo");
        SP_500.add("addInfo");
        BA.add("addInfo");
        BRKB.add("addInfo");
        DIS.add("addInfo");
        GE.add("addInfo");

        addInfo.put(companies.get(0),GOOG);
        addInfo.put(companies.get(1),AAPL);
        addInfo.put(companies.get(2),Dow_Jones);
        addInfo.put(companies.get(3),SP_500);
        addInfo.put(companies.get(4),BA);
        addInfo.put(companies.get(5),BRKB);
        addInfo.put(companies.get(6),DIS);
        addInfo.put(companies.get(7),GE);
    }
}
