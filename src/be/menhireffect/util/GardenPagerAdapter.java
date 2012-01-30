package be.menhireffect.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import be.menhireffect.garden.fragments.GardenDetailFragment;

/**
 * Adapter to page between fragments.
 */

public class GardenPagerAdapter extends FragmentPagerAdapter {

	int gardenItemId;
	int gardenItemsCount;
	
    public GardenPagerAdapter(FragmentManager fm) {
        super(fm);
    }
	
    public GardenPagerAdapter(FragmentManager fm, int id, int count) {
        super(fm);
    }
    
    public void setGardenItemId(int id) {
    	this.gardenItemId = id;
    }
    
    public void setGardenItemCount(int number) {
    	this.gardenItemsCount = number;
    }

    @Override
    public int getCount() {
        return this.gardenItemsCount;
    } 
    
    @Override
    public Fragment getItem(int position) {
		Log.i("Menhireffect", "GardenPagerAdapter::getItem ==> Create GardenDetailFragment");
		GardenDetailFragment f = GardenDetailFragment.newInstance(position);
		f.setId(position);
    	return f;            
    }
}
