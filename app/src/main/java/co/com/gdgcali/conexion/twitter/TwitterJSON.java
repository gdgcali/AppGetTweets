package co.com.gdgcali.conexion.twitter;

import android.util.Base64;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import co.com.gdgcali.modelo.Tweet;

/**
 * Created by JuanGuillermo on 23/01/2015.
 */
public class TwitterJSON {

    public static final String TAG = "TwitterJSON";

    public static final String URL_ROOT_TWITTER_API = "https://api.twitter.com";
    public static final String URL_SEARCH = URL_ROOT_TWITTER_API + "/1.1/search/tweets.json?q=";
    public static final String URL_AUTENTICACION = URL_ROOT_TWITTER_API + "/oauth2/token";

    public static final String CONSUMER_KEY = "sYOXRUWr5q40vLCuKCvHy9mbp";
    public static final String CONSUMER_SECRET = "zIDFi3tTxsMia1t3kfBMC7LXqw4Wg2NbToKagnjmAMad2nngvf";

    public static String autenticacion(){

        HttpURLConnection httpConnection = null;
        OutputStream outputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder response = null;

        try {

            URL url = new URL(URL_AUTENTICACION);
            httpConnection = (HttpURLConnection)url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);

            String accessCredential = CONSUMER_KEY + ":"
                    + CONSUMER_SECRET;
            String authorization = "Basic "
                    + Base64.encodeToString(accessCredential.getBytes(),
                    Base64.NO_WRAP);
            String param = "grant_type=client_credentials";

            httpConnection.addRequestProperty("Authorization", authorization);
            httpConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            httpConnection.connect();

            outputStream = httpConnection.getOutputStream();
            outputStream.write(param.getBytes());
            outputStream.flush();
            outputStream.close();

            bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            String line;
            response = new StringBuilder();

            while((line = bufferedReader.readLine()) != null){
                response.append(line);
            }

            Log.d(TAG, "Codigo de Respuesta = "+ String.valueOf(httpConnection.getResponseCode()));
            Log.d(TAG, "Respuesta = "+ String.valueOf(response.toString()));

        }catch (Exception e)
        {
            Log.e(TAG, "Error Autenticacion = "+ Log.getStackTraceString(e));
        }
        finally {
            if (httpConnection != null){
                httpConnection.disconnect();
            }
        }

        return response.toString();
    }

    public static ArrayList<Tweet> getTweets(String terminoBusqueda){

        ArrayList<Tweet> lstTweets = new ArrayList<>();

        HttpURLConnection httpConnection = null;
        BufferedReader bufferedReader = null;
        StringBuilder respuesta = new StringBuilder();

        try {

            URL url = new URL(URL_SEARCH + terminoBusqueda
                    + "&count=20");
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");

            String jsonAutenticacion = autenticacion();
            JSONObject jsonObjeto = new JSONObject(jsonAutenticacion);

            String token = jsonObjeto.getString("token_type") + " " + jsonObjeto.getString("access_token");

            httpConnection.setRequestProperty("Authorization", token);
            httpConnection.setRequestProperty("Content-Type",
                    "application/json");
            httpConnection.connect();

            bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));

            String line;
            while((line = bufferedReader.readLine()) != null){
                respuesta.append(line);
            }

            Log.d(TAG,
                    "GET Codigo Respuesta: "
                            + String.valueOf(httpConnection.getResponseCode()));
            Log.d(TAG, "JSON Respuesta: " + respuesta.toString());

            JSONObject jsonResponse = new JSONObject(respuesta.toString());
            JSONArray jsonArray = jsonResponse.getJSONArray("statuses");
            JSONObject object;
            Tweet tweet;

            for (int i = 0; i < jsonArray.length(); i++) {
                object = (JSONObject) jsonArray.get(i);
                tweet = new Tweet();
                tweet.setId(object.getString("id_str"));
                tweet.setNombre(object.getJSONObject("user").getString("name"));
                tweet.setScreenName(object.getJSONObject("user").getString(
                        "screen_name"));
                tweet.setProfileImageURL(object.getJSONObject("user")
                        .getString("profile_image_url"));
                tweet.setTexto(object.getString("text"));
                tweet.setCreadoPor(object.getString("created_at"));
                lstTweets.add(i, tweet);
            }

        }catch (Exception e){
            Log.e(TAG, "GET error: " + Log.getStackTraceString(e));
            return null;
        }
        finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }

        return lstTweets;
    }



}
