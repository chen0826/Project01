package com.cst2335.project01;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.support.design.widget.Snackbar;
//import android.support.design.widget.CoordinatorLayout;


import com.google.android.material.snackbar.Snackbar;

import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;
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
    Toolbar  songHelpBar;
    Menu songMenu;
   // private Object AppCompatActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View resultView =  inflater.inflate(R.layout.fragment_song_detail, container, false);
        //show thesong infor
        final View resultViewFinal = resultView;

        songHelpBar=(Toolbar)resultViewFinal.findViewById( R.id.toolbar_song );
        songHelpBar.inflateMenu( R.menu.song_menu );
        songMenu=songHelpBar.getMenu();
        // ((AppCompatActivity) getActivity()).getDelegate().setSupportActionBar(songHelpBar);
        //parentActivity.getDelegate().setSupportActionBar(songHelpBar);
        songHelpBar.setOnMenuItemClickListener( new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                songHelpBarAction(item);
                return false;
            }
        });

         songTitleV = (TextView) resultViewFinal.findViewById(R.id.row_songTitle_f);
         songIdV = (TextView) resultViewFinal.findViewById(R.id.row_songId_f);
         artistNameV = (TextView) resultViewFinal.findViewById(R.id.row_artistName_f);
         artistIdV = (TextView) resultViewFinal.findViewById(R.id.row_artistId_f);
         idView = (TextView) resultViewFinal.findViewById(R.id.row_head);

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(SongActivity.ITEM_ID );

        //show the id:
        idView.setText("Song From searchList or DB ID=" + id);
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

            deleteSongEntityFromDBbyID(id);
           // makeToastnotice( "it deleted from your database" );
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
           // makeToastnotice( "it deleted from your database" );
            Snackbar.make(resultView, "It deleted from your database forever",
                    Snackbar.LENGTH_INDEFINITE).show();

                });


        Button gotoBtn = (Button) resultViewFinal.findViewById(R.id.button_goto);
        gotoBtn.setOnClickListener( clk -> {

            Log.i( "button ", "gggggggggggg!!!!!!!!!!!!!!");

            Intent nextActivity = new Intent(getContext(), SongThirdActivity.class);
            //nextActivity.putExtras(dataToPass); //send data to next activity
            startActivityForResult(nextActivity, REQUIRED_CODE); //make the transition
        });

        if(dataFromActivity.getBoolean(SongThirdActivity.ISBACK)){
            saveBtn.setVisibility(View.GONE);
            deleBtn.setVisibility( View.VISIBLE );
          //  idDb = dataFromThirdActivity.getLong(SongThirdActivity.ITEM_ID );
            idView.setText("Song From FavouriteDB ID=" + id);
        };

        return resultViewFinal;
    }

    private void songHelpBarAction(MenuItem item) {
      //  String msg=null;
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.help_item:
                popHelpWindow();
             //   msg = "You clicked help icon";
                break;
            case R.id.backtosearch_item:
                Intent backActivity = new Intent( getContext(), SongActivity.class);
                startActivity(backActivity);
             //  msg = "You clicked on backtosearch page";
                break;
            case R.id.music_item:
                Intent musicDB = new Intent( getContext(), SongThirdActivity.class);
                startActivity(musicDB);
                //  msg = "You clicked on backtosearch page";
                break;


       


        }
       // makeToastnotice(msg);

    }

    private void popHelpWindow() {

       StringBuilder helpMsg=new StringBuilder("HELP TIPS");
        helpMsg.append("\n");
       helpMsg.append("if you click song ID , you will go to the music gitar notes with play function.");
       helpMsg.append("\n");
       helpMsg.append("if you click artist ID , you will go to the artist songlist and further");
        helpMsg.append("\n");
        helpMsg.append("click the save to favorite to save to dabtabase song favoriate ");
        helpMsg.append("\n");
        helpMsg.append("click go to favorite to see list of favoriate song  dabtabase ");


        //String msg=" iiiihelp help";
        AlertDialog.Builder  alertDialogBuilder = new AlertDialog.Builder( getContext());
        alertDialogBuilder.setTitle( "HELP Menu" )
           .setMessage(helpMsg.toString());
       // .setMessage( msg );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void makeToastnotice(String  msg) {

       // Toast toast = Toast.makeText( parentActivity.getApplicationContext(),
        Toast toast = Toast.makeText( getContext(),
                msg,
               // HtmlCompact.fromHtml("<font color='red'> msg </font>".
              //          HtmlCompact.FROM_HTML_MODE_LEGACY);
                Toast.LENGTH_LONG);
       // View view=toast.getView();
        // TextView text=view.findViewById( android.R.id.message );
        // text.setTextColor(getResources().getColor(android.R.color.white, getActivity().getTheme()));
        // text.setShadowLayer( 0,0,0,0 );
       // view.setBackgroundResource( R.color.colorAccent );
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