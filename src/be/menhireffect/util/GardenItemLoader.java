package be.menhireffect.util;

import java.util.ArrayList;

import android.content.Context;

public class GardenItemLoader extends GardenItemsLoader {

	int id;

    public GardenItemLoader(Context context, int id) {
        super(context);
    }
    
    protected String getRemoteUrl() {
    	return "http://www.menhireffect.be/service/garden/item/" + this.id;
    }

    /**
     * This is where the bulk of our work is done.  This function is
     * called in a background thread and should generate a new set of
     * data to be published by the loader.
     */
    @Override 
    public ArrayList<GardenItem> loadInBackground() {
    	return loadItem(this.id);
    }
    
    private ArrayList<GardenItem> loadItem(int id) {
    	this.id = id;
    	ArrayList<GardenItem> gardenItems = this.loadItems();
    	return gardenItems;
    }
}