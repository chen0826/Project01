package com.cst2335.project01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongThirdActivity extends AppCompatActivity {
    ArrayList<SongEntity> songFavourateList = new ArrayList<>( );
    ListView songFaveListV ;
    FavoSongListAdapter myFaveSongAdapter;
    SQLiteDatabase db;
    private AppCompatActivity parentActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_song_third );

        songFaveListV = findViewById(R.id.Favourate_listViewSong);
        songFaveListV.setAdapter( myFaveSongAdapter = new FavoSongListAdapter() );
        loadDataFromDatabase();
        myFaveSongAdapter.notifyDataSetChanged();

        songFaveListV.setOnItemClickListener( (parent, view, position, id) -> {
           showSong (position);
        }   );

    }

    class FavoSongListAdapter extends BaseAdapter {

        public int getCount() { return songFavourateList.size();}

        public Object getItem(int position) { return songFavourateList.get( position); }

        public long getItemId(int position) { return (long) position; }

        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            View newView = inflater.inflate(R.layout.row_song_layout, parent, false);

            //set what the text should be for this row:
            SongEntity songRow= (SongEntity) getItem(position);

            TextView songTitle = (TextView)newView.findViewById(R.id.row_songTitle);
            TextView songId = (TextView)newView.findViewById(R.id.row_songId);
            TextView artistName = (TextView)newView.findViewById(R.id.row_artistName);
            TextView artistId = (TextView)newView.findViewById(R.id.row_artistId);
            TextView rowId = (TextView)newView.findViewById(R.id.row_id);


            songTitle.setText(songRow.getSongTitle());
            songId.setText( "songID: " +String.valueOf(songRow.getSongId()));
            artistName.setText( "artist name: "+ songRow.getArtistName());
            artistId.setText( "artistID: "+ String.valueOf(songRow.getArtistId()));
            rowId.setText("list No:" +String.valueOf(songRow.getId()) );

            return newView;
        }
    }
    protected void showSong(int position)
    {

        SongEntity selectedSong = songFavourateList.get(position);
        View song_view = getLayoutInflater().inflate(R.layout.selected_song_dialog, null);

        TextView songId=song_view.findViewById(R.id.row_songId_d);
        TextView songtitle=song_view.findViewById(R.id.row_songTitle_d);
        TextView artistName=song_view.findViewById(R.id.row_artistName_d);
        TextView artistId=song_view.findViewById(R.id.row_artistId_d);

        songId.setText("songID:"+String.valueOf( selectedSong.getSongId()));
        songtitle.setText("Title: "+selectedSong.getSongTitle());
        artistId.setText("artistID: "+ String.valueOf(selectedSong.getArtistId()));
        artistName.setText("artist name: "+ selectedSong.getArtistName());


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You clicked on Song #" + position)
                .setMessage("You like remove it from your favorite?")
                .setView(song_view) //add the 3 edit texts showing the contact information
                .setPositiveButton("Yes", (click, b) -> {
                    deleteSongEntityFromDB(selectedSong );
                    songFavourateList.remove(position);
                    myFaveSongAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("No", (click, b) -> {
                    //  //remove the contact from database
                    //  contactsList.remove(position); //remove the contact from contact list
                    //  //there is one less item so update the list
                })
                .setNeutralButton("back to pre-page", (click, b) -> {
                    finish();
                })
                .create().show();


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
            songFavourateList.add(new SongEntity( songTitle, songId, artistName, artistId, id));

        }

        //At this point, the contactsList array has loaded every row from the cursor.
    }









    protected void updateSong(SongEntity s)
    {
        //Create a ContentValues object to represent a database row:
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(SongOpener.COL_SONGID, s.getSongId());
        updatedValues.put(SongOpener.COL_SONGTITLE, s.getSongTitle());
        updatedValues.put(SongOpener.COL_ARTISTID, s.getArtistId());
        updatedValues.put(SongOpener.COL_ARTISTNAME, s.getArtistName());


        //now call the update function:
        db.update(SongOpener.TABLE_NAME, updatedValues, SongOpener.COL_ID + "= ?", new String[] {Long.toString(s.getId())});
    }

    protected void  deleteSongEntityFromDB(SongEntity s)
    {
        db.delete(SongOpener.TABLE_NAME, SongOpener.COL_ID + "= ?", new String[] {Long.toString(s.getId())});
    }




}