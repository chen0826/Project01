package com.cst2335.project01;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;


public class SongActivity extends AppCompatActivity {
    private ArrayList<String> elements = new ArrayList<>( Arrays.asList( "One", "Two" ) );
    private MyListAdapter myAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songstar);


        TextView =  findViewById( R.id.text );



        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener( click -> {
            elements.add("Hi");
            myAdapter.notifyDataSetChanged();
        });


        ListView myList = findViewById(R.id.theListView);
        myList.setAdapter( myAdapter = new MyListAdapter());
        myList.setOnItemClickListener( (parent, view, pos, id) -> {

            elements.remove(pos);
            myAdapter.notifyDataSetChanged();
        }   );



    }




    private class MyListAdapter extends BaseAdapter {

        public int getCount() { return elements.size();}

        public Object getItem(int position) { return "This is row " + position; }

        public long getItemId(int position) { return (long) position; }

        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            View newView = inflater.inflate(R.layout.row_layout, parent, false);

            //set what the text should be for this row:
            TextView tView = newView.findViewById(R.id.textGoesHere);
            tView.setText( getItem(position).toString() );

            //return it to be put in the table
            return newView;
        }
    }















}