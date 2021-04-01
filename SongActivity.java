package com.cst2335.project01;//package com.cst2335.project01;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.KeyboardView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cst2335.project01.R;
import com.cst2335.project01.SongEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.view.View.VISIBLE;


public class SongActivity extends AppCompatActivity {
    ArrayList<SongEntity> songList = new ArrayList<>( );
    MySongListAdapter mySongAdapter;
    private static int ACTIVITY_VIEW_SONGLIST = 33;
    int positionClicked = 0;
    SQLiteDatabase db;
    ProgressBar progressBarSong;
    ListView songListV;
    EditText searchView;


    public static final String ITEM_SONG_TITLE= "SONGTITLE";
    public static final String ITEM_SONG_ID = "SONGID";
    public static final String  ITEM_ARTIST_NAME= "ARTISTNAME";
    public static final String  ITEM_ARTIST_ID= "ARTISTID";
    public static final String  ITEM_ID= "ITEMID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songstar);

        ImageView songHeadImage= findViewById( R.id.imageViewSong );
        searchView =  findViewById( R.id.editTextSearch );
        searchView.requestFocus();
        ImageButton searchBtn= findViewById(R.id.song_searchButton);
        progressBarSong=findViewById(R.id.progressBarSong);

        searchBtn.setOnClickListener( click -> {

            String searchterm="";
            searchterm=searchView.getText().toString();
            // http://www.songsterr.com/a/ra/songs.xml?pattern=XXX
            // http://www.songsterr.com/a/ra/songs.json?pattern=XXX
            String songJsonURL="https://www.songsterr.com/a/ra/songs.json?pattern="+searchterm;
            progressBarSong.setVisibility( VISIBLE );
            MySongHTTPRequest songReq = new MySongHTTPRequest();
            songReq.execute(songJsonURL);  //Type 1
        });


        songListV = findViewById(R.id.listViewSong);
        songListV.setAdapter( mySongAdapter = new MySongListAdapter() );
        mySongAdapter.notifyDataSetChanged();
       // songListV.setOnItemClickListener( (parent, view, position, id) -> {
       //   showSong (position);
      //  }   );

        boolean isTablet = findViewById( R.id.fragmentLocation_song) != null; //check if the FrameLayout is loaded
        songListV.setOnItemClickListener( (parent, view, position, id) -> {

            SongEntity selectedSong = songList.get(position);
            long ii =mySongAdapter.getItemId(position);

            Bundle dataToPass = new Bundle();
            dataToPass.putString( ITEM_SONG_TITLE, selectedSong.getSongTitle());
            dataToPass.putInt( ITEM_SONG_ID, selectedSong.getSongId() );
            dataToPass.putString( ITEM_ARTIST_NAME, selectedSong.getArtistName());
            dataToPass.putInt( ITEM_ARTIST_ID, selectedSong.getArtistId() );
            dataToPass.putLong( ITEM_ID, ii );
           /*
            songId.setText("songID:"+String.valueOf( selectedSong.getSongId()));
            songtitle.setText("Title: "+selectedSong.getSongTitle());
            artistId.setText("artistID: "+ String.valueOf(selectedSong.getArtistId()));
            artistName.setText("artist name: "+ selectedSong.getArtistName());

            */

            if(isTablet)
            {
                SongDetailFragment dFragment = new SongDetailFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(SongActivity.this, SongSecondActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });







    }
/*
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
*/
/*
    protected void showSong(int position)
    {

        SongEntity selectedSong = songList.get(position);
        View song_view = getLayoutInflater().inflate(R.layout.selected_song_dialog, null);

        TextView songId=song_view.findViewById(R.id.row_songId_d);
        TextView songtitle=song_view.findViewById(R.id.row_songTitle_d);
        TextView artistName=song_view.findViewById(R.id.row_artistName_d);
        TextView artistId=song_view.findViewById(R.id.row_artistId_d);

        songId.setText("songID:"+String.valueOf( selectedSong.getSongId()));
        songtitle.setText("Title: "+selectedSong.getSongTitle());
        artistId.setText("artistID: "+ String.valueOf(selectedSong.getArtistId()));
        artistName.setText("artist name: "+ selectedSong.getArtistName());

        Bundle dataToPass = new Bundle();
        dataToPass.putString(ITEM_SELECTED, source.get(position) );
        dataToPass.putInt(ITEM_POSITION, position);
        dataToPass.putLong(ITEM_ID, id);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You clicked on Song #" + position)
                .setMessage("You like save it to your favorite?")
                .setView(song_view) //add the 3 edit texts showing the contact information
                .setPositiveButton("Yes", (click, b) -> {

                })
                .setNegativeButton("No", (click, b) -> {
                   // deleteContact(selectedContact); //remove the contact from database
                  //  contactsList.remove(position); //remove the contact from contact list
                  //  myAdapter.notifyDataSetChanged(); //there is one less item so update the list
                })
                .setNeutralButton("dismiss", (click, b) -> {


                })
                .create().show();


    }
 */

/*
    protected void updateSong(SongEntity s)
    {
        //Create a ContentValues object to represent a database row:
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(MyOpener.COL_NAME, c.getName());
        updatedValues.put(MyOpener.COL_EMAIL, c.getEmail());

        //now call the update function:
        db.update(MyOpener.TABLE_NAME, updatedValues, MyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }

    protected void deleteContact(Contact c)
    {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }

*/



    static class MySongListAdapter extends BaseAdapter {

        public int getCount() { return songList.size();}

        public Object getItem(int position) { return songList.get( position); }

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

    private class MySongHTTPRequest extends AsyncTask< String, Integer, String>
    {
        //Type3                Type1
        public String doInBackground(String ... args)
        {
            String result = null;
            try {
                URL url= null;
                url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                publishProgress(10);
                Log.i("HTTP", "do in background step one url"+url);
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                publishProgress(50);
                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString(); //result is the whole string
                Log.i("HTTP", "do in background "+ result );
                publishProgress(100);
            }
            catch (Exception e)
            {
               e.printStackTrace(); 
            }
            return result;
        }
        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
            progressBarSong.setProgress(args[0]);
        }
        //Type3
        public void onPostExecute(String str)
        {   // convert string to JSON: Look at slide 27:
            Log.i("HTTP", str.toString());
            //Log.d("data", s.toString());
            JSONArray  jsonArray=null;
            songList.clear();
           try {

               jsonArray= new JSONArray(str);
               for (int i=0; i < jsonArray.length(); i++){

                   JSONObject jsonObject= null;
                   try {
                       jsonObject = jsonArray.getJSONObject(i);
                       String songIdStr=jsonObject.getString("id");
                       String songTitle=jsonObject.getString("title");
                       JSONObject jsonObjectAtrist= jsonObject.getJSONObject("artist");
                       String artistIdStr=jsonObjectAtrist.getString("id");
                       String artistName=jsonObjectAtrist.getString("name");
                       Integer songId= Integer.valueOf(songIdStr);
                       int artistId=Integer.parseInt(artistIdStr);
                       SongEntity songrow= new SongEntity(songTitle, songId, artistName, artistId, i);
                       songList.add(songrow);
                       } catch (JSONException jsonException) {
                           jsonException.printStackTrace();
                       }
               }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //songListV.setAdapter( mySongAdapter = new MySongListAdapter() );
            mySongAdapter.notifyDataSetChanged();
            progressBarSong.setVisibility(View.GONE);
            searchView.setText("");
        }
    }

}