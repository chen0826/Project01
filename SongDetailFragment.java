package com.cst2335.project01;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.cst2335.project01.SongActivity;

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
        songId.setText(dataFromActivity.getInt(SongActivity.ITEM_SONG_ID));
        TextView artistName = (TextView)result.findViewById(R.id.row_artistName_f);
        artistName.setText(dataFromActivity.getString(SongActivity.ITEM_ARTIST_NAME));
        TextView artistId = (TextView)result.findViewById(R.id.row_artistId_f);
        artistId.setText(dataFromActivity.getInt(SongActivity.ITEM_ARTIST_ID));
        //show the id:
        TextView idView = (TextView)result.findViewById(R.id.row_head);
        idView.setText("Song From List ID=" + id);

        // get the delete button, and add a click listener:
        Button tofavorateButton = (Button)result.findViewById(R.id.tofaverateButton);
        tofavorateButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
           // parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }




}