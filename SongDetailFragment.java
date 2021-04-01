package com.cst2335.project01;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//import com.cst2335.project01.SongActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongDetailFragment extends Fragment {


    public static final int REQUIRED_CODE = 222;
    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;
    SQLiteDatabase songDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(SongActivity.ITEM_ID );

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_song_detail, container, false);

        //show thesong infor
        final View result1 = result;
        TextView songTitleV = (TextView) result1.findViewById(R.id.row_songTitle_f);
        songTitleV.setText(dataFromActivity.getString(SongActivity.ITEM_SONG_TITLE));
        TextView songIdV = (TextView) result1.findViewById(R.id.row_songId_f);
        songIdV.setText(String.valueOf(dataFromActivity.getInt(SongActivity.ITEM_SONG_ID)));
        //: http://www.songsterr.com/a/wa/song?id=XXX
        songIdV.setOnClickListener( click->{
            int song_id_url=dataFromActivity.getInt(SongActivity.ITEM_SONG_ID);
            visitSongGitarPage(song_id_url);
        } );

        TextView artistNameV = (TextView) result1.findViewById(R.id.row_artistName_f);
        artistNameV.setText(dataFromActivity.getString(SongActivity.ITEM_ARTIST_NAME));
        TextView artistIdV = (TextView) result1.findViewById(R.id.row_artistId_f);
        artistIdV.setText(String.valueOf(dataFromActivity.getInt(SongActivity.ITEM_ARTIST_ID)));
        artistIdV.setOnClickListener( click->{
            int artist_id_url=dataFromActivity.getInt(SongActivity.ITEM_ARTIST_ID);
            broweAritistPageById(artist_id_url);
        } );
        // http://www.songsterr.com/a/wa/artist?id=XXX
        
        //show the id:
        TextView idView = (TextView) result1.findViewById(R.id.row_head);
        idView.setText("Song From List ID=" + id);

        Button saveBtn = (Button) result1.findViewById(R.id.button_save);
        saveBtn.setOnClickListener( clk -> {

                    Log.i( "button ", "savesave!!!!!!!!!!!!!!" );


                    int song_id_ts = dataFromActivity.getInt( SongActivity.ITEM_SONG_ID );
                    String song_title_ts = songTitleV.getText().toString();
                    String aritist_name_ts = artistNameV.getText().toString();
                    int artist_id_ts = dataFromActivity.getInt( SongActivity.ITEM_ARTIST_ID );
            Log.i( "HTTP", "***********"+aritist_name_ts+song_title_ts
                    +String.valueOf( song_id_ts )+song_title_ts
                    +String.valueOf( artist_id_ts )
            );
                   long idinDB = saveTofavorateDBgetId( song_title_ts, song_id_ts, aritist_name_ts, artist_id_ts );
                    String msg = "success save to favoirte DB id:"+ String.valueOf( idinDB );
                    Log.i( "SAVE", msg );
                    // get toast to notice save success
                    makeToastnotice( msg );
                });

        Button deleBtn = (Button) result1.findViewById(R.id.button_dele);
        deleBtn.setOnClickListener( clk -> {
            Log.i( "button ", "ddddddddd!!!!!!!!!!!!!!!!!!!!!!");
           parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
                });

        Button gotoBtn = (Button) result1.findViewById(R.id.button_goto);
        gotoBtn.setOnClickListener( clk -> {

            Log.i( "button ", "gggggggggggg!!!!!!!!!!!!!!");

            Intent nextActivity = new Intent(getContext(), SongThirdActivity.class);
            //nextActivity.putExtras(dataToPass); //send data to next activity
            startActivityForResult(nextActivity, REQUIRED_CODE); //make the transition
        });
        return result1;
    }
    private void makeToastnotice(String  msg) {
        Toast toast = Toast.makeText( parentActivity.getApplicationContext(),
                msg, Toast.LENGTH_LONG );
        toast.show();
    }


    private long saveTofavorateDBgetId( String songtitlets, int song_id_ts, String  aritistnamets, int artist_id_ts)
        {
            SongOpener dbOpener = new SongOpener(getContext());
            songDb = dbOpener.getWritableDatabase();
            ContentValues newSongValue = new ContentValues();
            newSongValue.put( SongOpener.COL_SONGID, song_id_ts );
            newSongValue.put( SongOpener.COL_SONGTITLE, songtitlets );
            newSongValue.put( SongOpener.COL_ARTISTID, artist_id_ts );
            newSongValue.put( SongOpener.COL_ARTISTNAME, aritistnamets );
            if(newSongValue!=null && !newSongValue.isEmpty()){
            long newIdd = songDb.insert( SongOpener.TABLE_NAME, null, newSongValue );
            return newIdd;
            }
            return Long.parseLong( null );
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