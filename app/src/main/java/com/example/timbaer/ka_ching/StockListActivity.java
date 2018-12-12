package com.example.timbaer.ka_ching;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import android.widget.Toast;
import android.widget.SearchView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.toolbox.JsonArrayRequest;

public class StockListActivity extends AppCompatActivity {
    ExpandableListView expandableListView;

    List<String> companies;
    Map<String, List<Integer>> addInfo;
    private MyExListAdapter listAdapter;

    private  JSONArray response;
    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_list);
        Log.d("MyTag", "onCreate");
        requestQueue = Volley.newRequestQueue(this);

        expandableListView = (ExpandableListView) findViewById(R.id.dynamic);
        fillData();

        listAdapter = new MyExListAdapter(this, companies, addInfo);
        expandableListView.setAdapter(listAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                String tickerName = (String) listAdapter.getGroup(groupPosition);
                Toast.makeText(StockListActivity.this, tickerName, Toast.LENGTH_SHORT).show();
                startApiCall(tickerName.toLowerCase());
                return false;
            }
        });

        final SearchView searchView = (SearchView) findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("ToastCheck", "ToastCheck");
                Toast.makeText(searchView.getContext(), query, Toast.LENGTH_LONG).show();
                startApiCall(query.toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                (StockListActivity.this).listAdapter.filter(newText);
                return false;
            }
        });
    }

    public void fillData() {
        companies = new ArrayList<>();
        addInfo = new HashMap<>();

        companies.add("GOOG");
        companies.add("AAPL");
        companies.add("DJI");
        companies.add("INX");
        companies.add("BA");
        companies.add("BRK-B");
        companies.add("DIS");
        companies.add("GE");

        List<Integer> GOOG = new ArrayList<>();
        List<Integer> AAPL = new ArrayList<>();
        List<Integer> DJI = new ArrayList<>();
        List<Integer> INX = new ArrayList<>();
        List<Integer> BA = new ArrayList<>();
        List<Integer> BRKB = new ArrayList<>();
        List<Integer> DIS = new ArrayList<>();
        List<Integer> GE = new ArrayList<>();


        addInfo.put(companies.get(0),GOOG);
        addInfo.put(companies.get(1),AAPL);
        addInfo.put(companies.get(2),DJI);
        addInfo.put(companies.get(3),INX);
        addInfo.put(companies.get(4),BA);
        addInfo.put(companies.get(5),BRKB);
        addInfo.put(companies.get(6),DIS);
        addInfo.put(companies.get(7),GE);
    }
    //"https://api.iextrading.com/1.0/stock/" + ticker + "/chart/6m"
    /**
     *
     * Make a call to the IEX API
     *
     * @param ticker stock symbol for the app to look up
     */
    public void startApiCall(final String ticker) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "https://api.iextrading.com/1.0/stock/" + ticker + "/chart/6m",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        apiCallDone(response, ticker);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("APIFAIL", error.toString());

                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Handle the response from our IEX API
     *
     * @param response response from our IEX API
     */
    void apiCallDone(final JSONArray response, final String ticker) {
        Log.d("API", response.toString());
        List<Integer> toPut = new ArrayList<Integer>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonobject = response.getJSONObject(i);
                String open = jsonobject.getString("open");
                Float toAddFirst = Float.parseFloat(open);
                float toAddFloat = toAddFirst;
                int toAddInt = Math.round(toAddFloat);
                Integer toAdd = new Integer(toAddInt);
                toPut.add(toAdd);
            }
        } catch(Exception e) {
            Log.d("JSONEXCEPTION", e.toString());
        }
        addInfo.put(ticker.toUpperCase(), toPut);
        listAdapter.notifyDataSetChanged();
    }
}