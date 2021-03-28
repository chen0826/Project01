package com.cst2335.project01;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    ArrayList<SongEntity> songList = new ArrayList<>( );
    MyListAdapter mySongAdapter;
    //ArrayList<Contact> contactsList = new ArrayList<>();
    private static int ACTIVITY_VIEW_SONGLIST = 33;
    int positionClicked = 0;
    //MyOwnAdapter myAdapter;
    SQLiteDatabase db;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songstar);

        ImageView songHeadImage= findViewById( R.id.imageViewSong );
        EditText searchView =  findViewById( R.id.editTextSearch );
        ImageButton searchbtn= findViewById(R.id.song_searchButton);
        ProgressBar progressBar=findViewById(R.id.progressBarSong);

        loadDataFromDatabase(); //get any previously saved songlist objects


        ListView songListV = findViewById(R.id.listViewSong);
        songListV.setAdapter( mySongAdapter = new MyListAdapter());
        songListV.setOnItemClickListener( (parent, view, pos, id) -> {

           // songList.remove(pos);
          //  mySongAdapter.notifyDataSetChanged();
        }   );



    }

    private void loadDataFromDatabase()
    {
        //get a database connection:
        SongOpener dbOpener = new SongOpener( this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
       //String [] columns = {MyOpener.COL_ID, MyOpener.COL_EMAIL, MyOpener.COL_NAME};
        String [] columns = {SongOpener.COL_ID, SongOpener.COL_SONGTITLE, SongOpener.COL_SONGID,
                SongOpener.COL_ARTISTNAME, SongOpener.COL_ARTISTID };

        //query all the results from the database:
        Cursor results = db.query(false, SongOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int songTitleColumnIndex = results.getColumnIndex(SongOpener.COL_SONGTITLE);
        int songIdColIndex = results.getColumnIndex(SongOpener.COL_SONGID);
        int artistNameColumnIndex = results.getColumnIndex(SongOpener.COL_ARTISTNAME);
        int artistIdColIndex = results.getColumnIndex(SongOpener.COL_ARTISTID);
        int idColIndex = results.getColumnIndex(SongOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String songTitle = results.getString(songTitleColumnIndex );
            int songId = results.getInt(songIdColIndex);
            String artistName = results.getString(artistNameColumnIndex);
            int artistId = results.getInt(artistIdColIndex );
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
           // contactsList.add(new Contact(name, email, id));
            songList.add(new SongEntity( songTitle, songId, artistName, artistId, id));

        }

        //At this point, the contactsList array has loaded every row from the cursor.
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