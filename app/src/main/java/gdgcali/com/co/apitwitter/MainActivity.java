package gdgcali.com.co.apitwitter;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import co.com.gdgcali.conexion.twitter.TwitterJSON;
import co.com.gdgcali.modelo.Tweet;


public class MainActivity extends ActionBarActivity {

    private ListView lvTimeLine;
    private TweetAdapter adapter;
    private static final String TERM = "gdgcali";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTimeLine = (ListView) findViewById(R.id.lv_timeline);

        new TweetSearchTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateListView(ArrayList<Tweet> tweets) {
        adapter = new TweetAdapter(this, R.layout.fragment_fila_tweet, tweets);
        lvTimeLine.setAdapter(adapter);
    }

    class TweetSearchTask extends AsyncTask<Object, Void, ArrayList<Tweet>> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPostExecute(ArrayList<Tweet> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (!result.isEmpty()) {
                updateListView(result);
            } else {
                Toast.makeText(
                        MainActivity.this,
                        getResources().getString(
                                R.string.label_tweets_not_found),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(getResources().getString(
                    R.string.label_tweet_search_loader));
            progressDialog.show();
        }

        @Override
        protected ArrayList<Tweet> doInBackground(Object... params) {
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();

            try {

                tweets = TwitterJSON.getTweets(TERM);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return tweets;
        }
    }
}
