package com.cst2335.project01;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;


public class SongActivity extends AppCompatActivity {
    private ArrayList<SongEntity> songList = new ArrayList<>( );
    private MyListAdapter mySongAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songstar);

        ImageView songHeadImage= findViewById( R.id.imageViewSong );
        EditText searchView =  findViewById( R.id.editTextSearch );
        ImageButton searchbtn= findViewById(R.id.song_searchButton);
        ProgressBar progressBar=findViewById(R.id.progressBarSong);


        ListView songListV = findViewById(R.id.listViewSong);
        songListV.setAdapter( mySongAdapter = new MyListAdapter());
        songListV.setOnItemClickListener( (parent, view, pos, id) -> {

           // songList.remove(pos);
          //  mySongAdapter.notifyDataSetChanged();
        }   );



    }




    private class MyListAdapter extends BaseAdapter {

        public int getCount() { return songList.size();}

        public Object getItem(int position) { return "This is row " + position; }

        public long getItemId(int position) { return (long) position; }

        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            View newView = inflater.inflate(R.layout.row_listview_song_layout, parent, false);

            //set what the text should be for this row:
            TextView tView = newView.findViewById(R.id.inputliveV);
            tView.setText( getItem(position).toString() );

            //return it to be put in the table
            return newView;
        }
    }















}