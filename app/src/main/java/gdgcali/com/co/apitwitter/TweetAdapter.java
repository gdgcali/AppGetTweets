package gdgcali.com.co.apitwitter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import co.com.gdgcali.co.com.gdgcali.utils.BitmapManager;
import co.com.gdgcali.co.com.gdgcali.utils.DateUtils;
import co.com.gdgcali.modelo.Tweet;

/**
 * Created by JuanGuillermo on 23/01/2015.
 */
public class TweetAdapter extends ArrayAdapter<Tweet> {

    private Context context;
    private ArrayList<Tweet> tweets;

    public TweetAdapter(Context context, int viewResourceId,
                        ArrayList<Tweet> tweets) {
        super(context, viewResourceId, tweets);
        this.context = context;
        this.tweets = tweets;
    }

    static class ViewHolder {

        public ImageView avatar;
        public TextView name;
        public TextView screenName;
        public TextView text;
        public TextView createdAt;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.fragment_fila_tweet, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.avatar = (ImageView) convertView
                    .findViewById(R.id.avatar);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.screenName = (TextView) convertView
                    .findViewById(R.id.screen_name);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
            viewHolder.createdAt = (TextView) convertView
                    .findViewById(R.id.created_at);
            convertView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        BitmapManager.getInstance().loadBitmap(
                tweets.get(position).getProfileImageURL(), holder.avatar);
        holder.name.setText(tweets.get(position).getNombre());
        holder.screenName.setText("@" + tweets.get(position).getScreenName());
        holder.text.setText(tweets.get(position).getTexto());
        holder.createdAt.setText(DateUtils.setDateFormat(tweets.get(position).getCreadoPor()));

        return convertView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
    }
}
