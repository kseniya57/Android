package com.example.kseniya57.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView totalsView;

    SQLiteDatabase db;
    DBHelper dbHelper;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;

    private String[] headers = {"_id", "name", "price", "type"};
    private int[] fields;
    private String sortColumn = "_id";
    private String sortDirection = "ASC";

    private static final int HEADERS_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                MainActivity.this.startActivityForResult(intent, 1);
            }
        });

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        setupListView();
        totalsView = (TextView)findViewById(R.id.totals);
        refreshTotals();
    }

    private String getQuery() {
        return "SELECT * FROM products ORDER by " + sortColumn + " " + sortDirection;
    }

    private void setupListView() {
        listView = (ListView) findViewById(R.id.list);
        listView.addHeaderView(getLayoutInflater().inflate(R.layout.totals, null));
        listView.addHeaderView(getLayoutInflater().inflate(R.layout.header, null));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }
        fields = new int[]  {R.id.item_id, R.id.item_name, R.id.item_price, R.id.item_type};

        cursor = db.rawQuery(getQuery(), null);

        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, headers, fields, 0);
        listView.setAdapter(scAdapter);
    }

    private void recreateCursor() {
        cursor = db.rawQuery(getQuery(), null);
        scAdapter.swapCursor(cursor);
        scAdapter.notifyDataSetChanged();
    }

    private void refreshTotals() {
        int totalPrice = 0;
        Cursor totalsCursor = db.rawQuery("SELECT SUM(price) as totalPrice FROM products", null);
        if (totalsCursor.moveToFirst()) {
            totalPrice = totalsCursor.getInt(totalsCursor.getColumnIndex("totalPrice"));
        }
        totalsView.setText("Количество товаров: " + Integer.toString(cursor.getCount()) + ", Общая стоимость: " + totalPrice);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear_all) {
            String[] where = {};
            db.delete("products", "", where);
            recreateCursor();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                int id = data.getIntExtra("id", 0);
                String name = data.getStringExtra("name");
                int price = data.getIntExtra("price", 0);
                String type = data.getStringExtra("type");
                if (id == 0) {
                    db.execSQL("INSERT INTO " + dbHelper.TABLE_NAME + " (name, price, type) VALUES (\"" + name + "\", " + price + ", \"" + type +"\")");
                } else {
                    db.execSQL("UPDATE " + dbHelper.TABLE_NAME + " SET name = \"" + name + "\", price = " + price + ", type = \"" + type +"\" WHERE _id = " + id);
                }
                recreateCursor();
                refreshTotals();
            }
        }
    }

    public void onEditClick(View v) {
        int index = listView.indexOfChild((View)v.getParent()) - HEADERS_COUNT;
        cursor.moveToPosition(index);
        Intent intent = new Intent(MainActivity.this, ProductActivity.class);
        intent.putExtra("id", cursor.getInt(0));
        intent.putExtra("name", cursor.getString(1));
        intent.putExtra("price", cursor.getInt(2));
        intent.putExtra("type", cursor.getString(3));
        MainActivity.this.startActivityForResult(intent, 1);
    }

    public void onDeleteClick(View v) {
        int id = Integer.parseInt(((TextView)((View)v.getParent()).findViewById(R.id.item_id)).getText().toString());
        db.delete("products", "_id = " + id, null);
        recreateCursor();
    }

    public void sort(View v) {
        String newSortColumn = v.getTag().toString();
        if (newSortColumn == sortColumn) {
            sortDirection = sortDirection == "ASC" ? "DESC" : "ASC";
        } else {
            sortColumn = newSortColumn;
            sortDirection = "ASC";
        }
        recreateCursor();
    }
}
