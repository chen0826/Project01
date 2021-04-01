package com.cst2335.project01;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
//import com.cst2335.project01.SongActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongDetailFragment extends Fragment {


    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(SongActivity.ITEM_ID );

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_song_detail, container, false);

        //show thesong infor
        TextView songTitle = (TextView)result.findViewById(R.id.row_songTitle_f);
        songTitle.setText(dataFromActivity.getString(SongActivity.ITEM_SONG_TITLE));
        TextView songId = (TextView)result.findViewById(R.id.row_songId_f);
        songId.setText(String.valueOf(dataFromActivity.getInt(SongActivity.ITEM_SONG_ID)));
        //: http://www.songsterr.com/a/wa/song?id=XXX
        songId.setOnClickListener( click->{
            int song_id_url=dataFromActivity.getInt(SongActivity.ITEM_SONG_ID);
            visitSongGitarPage(song_id_url);
        } );

        TextView artistName = (TextView)result.findViewById(R.id.row_artistName_f);
        artistName.setText(dataFromActivity.getString(SongActivity.ITEM_ARTIST_NAME));
        TextView artistId = (TextView)result.findViewById(R.id.row_artistId_f);
        artistId.setText(String.valueOf(dataFromActivity.getInt(SongActivity.ITEM_ARTIST_ID)));
        artistId.setOnClickListener( click->{
            int artist_id_url=dataFromActivity.getInt(SongActivity.ITEM_ARTIST_ID);
            broweAritistPageById(artist_id_url);
        } );
        // http://www.songsterr.com/a/wa/artist?id=XXX
        
        //show the id:
        TextView idView = (TextView)result.findViewById(R.id.row_head);
        idView.setText("Song From List ID=" + id);

        // get the delete button, and add a click listener:
        Button saveTofavorateBtn = (Button)result.findViewById(R.id.savetofavourite_btn);
        saveTofavorateBtn .setOnClickListener( clk -> {
            //Tell the parent activity to remove
           // parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        Button seeFavorateBtn = (Button)result.findViewById(R.id.seeFavourite_btn);
        seeFavorateBtn.setOnClickListener( clk -> {

            Intent nextActivity = new Intent(SongDetailFragment.this, SongThirdActivity.class);
            nextActivity.putExtras(dataToPass); //send data to next activity
            startActivity(nextActivity); //make the transition
            //Tell the parent activity to remove
            // parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });





        return result;
    }

    private void broweAritistPageById(int artist_id_url) {
        String  url="http://www.songsterr.com/a/wa/artist?id="+String.valueOf(artist_id_url);
        Intent ii =new Intent(Intent.ACTION_VIEW);
        ii.setData( Uri.parse(url));
        startActivity( ii );
    }

    private void visitSongGitarPage(int song_id_url) {
        String  url="http://www.songsterr.com/a/wa/song?id="+String.valueOf(song_id_url);
        Intent i =new Intent(Intent.ACTION_VIEW);
        i.setData( Uri.parse(url));
        startActivity( i );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }




}