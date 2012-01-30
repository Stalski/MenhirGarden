package be.menhireffect.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class GardenItemsLoader extends AsyncTaskLoader<ArrayList<GardenItem>> {

    protected ArrayList<GardenItem> mItems;

    public GardenItemsLoader(Context context) {
        super(context);
    }
    
    protected String getRemoteUrl() {
    	return "http://www.menhireffect.be/service/garden/list";
    }

    /**
     * This is where the bulk of our work is done.  This function is
     * called in a background thread and should generate a new set of
     * data to be published by the loader.
     */
    @Override 
    public ArrayList<GardenItem> loadInBackground() {
    	return loadItems();
    }

    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override 
    public void deliverResult(ArrayList<GardenItem> items) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (items != null) {
                onReleaseResources(items);
            }
        }
        ArrayList<GardenItem> oldItems = items;
        mItems = items;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(items);
        }

        // At this point we can release the resources associated with
        // 'oldItems' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldItems != null) {
            onReleaseResources(oldItems);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override 
    protected void onStartLoading() {
        if (mItems != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(mItems);
        } else {
            // Start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override 
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override 
    public void onCanceled(ArrayList<GardenItem> items) {
        super.onCanceled(items);

        // At this point we can release the resources associated with 'items'
        // if needed.
        onReleaseResources(items);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'items'
        // if needed.
        if (mItems != null) {
            onReleaseResources(mItems);
            mItems = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(ArrayList<GardenItem> items) {
        // For a simple ArrayList<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }
    
    protected ArrayList<GardenItem> loadItems() {

    	ArrayList<GardenItem> items = new ArrayList<GardenItem>();

		String result = null;
		StringBuilder sb = null;

    	// Create InputStream with HttpClient.
		InputStream is = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(this.getRemoteUrl());
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch(Exception e){
			Log.e("log_tag", "Error in http connection" + e.toString());
		}
	
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line = "0";
	     
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			is.close();
			result = sb.toString();
			
		} catch(Exception e){
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
	      	JSONArray jArray = new JSONArray(result);
	      	JSONObject json_data = null;
	      	
	      	for(int i = 0; i < jArray.length(); i++) {
				json_data = jArray.getJSONObject(i);
				GardenItem gardenItem = new GardenItem(json_data.getString("title"), json_data.getString("url"), json_data.getString("image"));
				gardenItem.setId(json_data.getInt("nid"));
				// @TODO Add the real description
				gardenItem.setDescription(gardenItem.getTitle());
				items.add(gardenItem);
	      	}
	      	
		} catch(JSONException e1){
			Log.e("Menhireffect", "JSONException");
		} catch (ParseException e1){
			e1.printStackTrace();
		}
    	
        return items;
    }
}