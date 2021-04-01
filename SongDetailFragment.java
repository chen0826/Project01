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


    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;
    SQLiteDatabase db;

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

        // get the delete button, and add a click listener:
        Button saveTofavorateBtn = (Button) result1.findViewById(R.id.savetofavourite_btn);
        saveTofavorateBtn.setOnClickListener( click -> {
            Log.i( "SAVE", "savaclicked");
            Toast.makeText( parentActivity.getBaseContext(), " than invoice",
                    Toast.LENGTH_SHORT).show();
            /*
            int song_id_ts=dataFromActivity.getInt(SongActivity.ITEM_SONG_ID);

            String song_title_ts=songTitleV.getText().toString();
            String aritist_name_ts=artistNameV.getText().toString();
            int artist_id_ts=dataFromActivity.getInt(SongActivity.ITEM_ARTIST_ID);
            long idinDB=saveTofavorateDBgetID( song_title_ts,  song_id_ts, aritist_name_ts,  artist_id_ts);
            String msg="success save to favoirt DB id:"+String.valueOf(idinDB);
            Log.i( "SAVE", msg );
            makeToastnotice( msg );
            // get toast to notice save success
             */



            //songFavourateList.add(new SongEntity( songTitle, songId, artistName, artistId, id));
            /*
             String textMsg = msgText.getText().toString();
            long id = saveMsgToDBAndGetID( textMsg, true );//return id and insert db sametime
            Message message = new Message( textMsg, true, id );
            messageList.add( message );
            myAdapter.notifyDataSetChanged();
            msgText.setText( " " );
             */


           // parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        Button seeFavouriteBtu = (Button) result1.findViewById(R.id.goFavourite_btn);
        seeFavouriteBtu.setOnClickListener( clk -> {

            Intent nextActivity = new Intent(getContext(), SongThirdActivity.class);
            //nextActivity.putExtras(dataToPass); //send data to next activity
            startActivityForResult(nextActivity, 111); //make the transition
            //Tell the parent activity to remove
            // parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return result1;

    private void makeToastnotice(String  msg) {
        Toast toast= Toast.makeText( parentActivity.getApplicationContext(),
                msg, Toast.LENGTH_LONG );
        toast.setGravity( Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
        toast.show();

    }

    private long saveTofavorateDBgetID(String song_title_ts, int song_id_ts, String aritist_name_ts, int artist_id_ts) {
        ContentValues newSongValues = new ContentValues();

        newSongValues.put(SongOpener.COL_SONGID, song_id_ts);
        newSongValues.put(SongOpener.COL_SONGTITLE, song_title_ts);
        newSongValues.put(SongOpener.COL_ARTISTID, artist_id_ts);
        newSongValues.put(SongOpener.COL_ARTISTNAME, aritist_name_ts);
        Long newID=db.insert(SongOpener.TABLE_NAME,null,newSongValues);
        return newID;
  /*

    //need rewrite to songdb
    private long saveMsgToDBAndGetID(String textMsg, boolean issentB) {
        //convert issent from boolean to int 1 or 0
        int issentI = issentB ? 1:0 ;
        ContentValues newMessagerValues = new ContentValues();
        newMessagerValues.put( MyOpener.COL_MESSAGE, textMsg);
        newMessagerValues.put(  MyOpener.COL_ISSENT, issentI );
        long newId = db.insert( MyOpener.TABLE_NAME, null, newMessagerValues );
        return newId;
    }

 */





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