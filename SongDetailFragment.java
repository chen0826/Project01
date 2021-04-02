package com.cst2335.project01;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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
    private Bundle dataFromThirdActivity;
    private long id;
    private long idDb;
    private AppCompatActivity parentActivity;
    SQLiteDatabase songDb;
    Button saveBtn;
    Button deleBtn;

    TextView songTitleV ;
    TextView songIdV ;
    TextView artistNameV ;
    TextView artistIdV ;
    TextView idView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        View resultView =  inflater.inflate(R.layout.fragment_song_detail, container, false);
        //show thesong infor
        final View resultViewFinal = resultView;

         songTitleV = (TextView) resultViewFinal.findViewById(R.id.row_songTitle_f);
         songIdV = (TextView) resultViewFinal.findViewById(R.id.row_songId_f);
         artistNameV = (TextView) resultViewFinal.findViewById(R.id.row_artistName_f);
         artistIdV = (TextView) resultViewFinal.findViewById(R.id.row_artistId_f);
         idView = (TextView) resultViewFinal.findViewById(R.id.row_head);

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(SongActivity.ITEM_ID );

        //show the id:
        idView.setText("Song From searchList ID=" + id);
        songTitleV.setText(dataFromActivity.getString(SongActivity.ITEM_SONG_TITLE));
        songIdV.setText(String.valueOf(dataFromActivity.getInt(SongActivity.ITEM_SONG_ID)));
        artistNameV.setText(dataFromActivity.getString(SongActivity.ITEM_ARTIST_NAME));
        artistIdV.setText(String.valueOf(dataFromActivity.getInt(SongActivity.ITEM_ARTIST_ID)));

        songIdV.setOnClickListener( click->{
            int song_id_url=dataFromActivity.getInt(SongActivity.ITEM_SONG_ID);
            visitSongGitarPage(song_id_url);
        } );

        artistIdV.setOnClickListener( click->{
            int artist_id_url=dataFromActivity.getInt(SongActivity.ITEM_ARTIST_ID);
            broweAritistPageById(artist_id_url);
        } );


        saveBtn = (Button) resultViewFinal.findViewById(R.id.button_save);
        saveBtn.setOnClickListener( clk -> {

                    Log.i( "button ", "savesave!!!!!!!!!!!!!!" );
                    int song_id_ts = dataFromActivity.getInt( SongActivity.ITEM_SONG_ID );
                    String song_title_ts = songTitleV.getText().toString();
                    String aritist_name_ts = artistNameV.getText().toString();
                    int artist_id_ts = dataFromActivity.getInt( SongActivity.ITEM_ARTIST_ID );
                    Log.i( "HTTP", "***********"+aritist_name_ts+song_title_ts
                                              +String.valueOf( song_id_ts )+song_title_ts
                                              +String.valueOf( artist_id_ts ));
                    long idinDB = saveTofavorateDBgetId( song_title_ts, song_id_ts, aritist_name_ts, artist_id_ts );
                    String msg = "success save to favoirte DB id:"+ String.valueOf( idinDB );
                    Log.i( "SAVE", msg );
                    // get toast to notice save success
                    makeToastnotice( msg );
                });

        deleBtn = (Button) resultViewFinal.findViewById(R.id.button_dele);
        deleBtn.setOnClickListener( clk -> {
            Log.i( "button ", "ddddddddd!!!!!!!!!!!!!!!!!!!!!!");

            deleteSongEntityFromDBbyID(idDb);
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
                });


        Button gotoBtn = (Button) resultViewFinal.findViewById(R.id.button_goto);
        gotoBtn.setOnClickListener( clk -> {

            Log.i( "button ", "gggggggggggg!!!!!!!!!!!!!!");

            Intent nextActivity = new Intent(getContext(), SongThirdActivity.class);
            //nextActivity.putExtras(dataToPass); //send data to next activity
            startActivityForResult(nextActivity, REQUIRED_CODE); //make the transition
        });

        if(dataFromActivity.getBoolean(SongActivity.ISBACK)){
            saveBtn.setVisibility(View.GONE);
            deleBtn.setVisibility( View.VISIBLE );
            idDb = dataFromThirdActivity.getLong(SongThirdActivity.ITEM_ID );
            idView.setText("Song From FavouriteList ID=" + idDb);
        };

        return resultViewFinal;
    }
/*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if( requestCode == REQUIRED_CODE && resultCode == 500 ){
            saveBtn.setVisibility(View.GONE);
            deleBtn.setVisibility( View.VISIBLE );

            dataFromThirdActivity = getArguments();
            idDb = dataFromThirdActivity.getLong(SongThirdActivity.ITEM_ID );

            //show the id:
            idView.setText("Song From FavouriteList ID=" + idDb);
            songTitleV.setText(dataFromThirdActivity.getString(SongThirdActivity.ITEM_SONG_TITLE));
            songIdV.setText(String.valueOf(dataFromThirdActivity.getInt(SongThirdActivity.ITEM_SONG_ID)));
            artistNameV.setText(dataFromThirdActivity.getString(SongThirdActivity.ITEM_ARTIST_NAME));
            artistIdV.setText(String.valueOf(dataFromThirdActivity.getInt(SongThirdActivity.ITEM_ARTIST_ID)));


            int e = Log.e( "RESLUT", "In function"+dataFromThirdActivity.getString(SongThirdActivity.ITEM_SONG_TITLE) );
        }

    }
*/
    private void makeToastnotice(String  msg) {
        Toast toast = Toast.makeText( parentActivity.getApplicationContext(),
                msg, Toast.LENGTH_LONG );
       // toast.setGravity(Gravity.CENTER_HORIZONTAL,0, 0);
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

    public  void  deleteSongEntityFromDBbyID(long idDb)
    {
        SongOpener dbOpener = new SongOpener(getContext());
        songDb = dbOpener.getWritableDatabase();
        songDb.delete(SongOpener.TABLE_NAME, SongOpener.COL_ID + "= ?", new String[] {Long.toString(idDb)});
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