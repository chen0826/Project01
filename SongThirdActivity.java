package com.cst2335.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class SongThirdActivity extends AppCompatActivity {
    ArrayList<SongEntity> songFavourateList = new ArrayList<>( );
    ListView songFaveListV ;
    SongActivity.MySongListAdapter myFaveSongAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_song_third );

        songFaveListV = findViewById(R.id.Favourate_listViewSong);
        songFaveListV.setAdapter( myFaveSongAdapter = new SongActivity.MySongListAdapter() );
        myFaveSongAdapter.notifyDataSetChanged();

    }










}