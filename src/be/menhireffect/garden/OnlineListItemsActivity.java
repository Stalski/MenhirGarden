package be.menhireffect.garden;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import be.menhireffect.garden.fragments.GardenDetailFragment;
import be.menhireffect.garden.fragments.GardenListFragment;
import be.menhireffect.util.GardenPagerAdapter;

public class OnlineListItemsActivity extends FragmentActivity implements OnGardenItemSelectedListener {
	
	Boolean mDetailsInline = false;
	int mDetailsCount = 0;
	int mDetailId = 0;

	GardenPagerAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			this.setDetailId(extras.getInt("id"));
			this.setDetailsCount(extras.getInt("num"));
			this.setDetailsInline(extras.getBoolean("detail"));
			mAdapter = new GardenPagerAdapter(getSupportFragmentManager(), extras.getInt("id"), extras.getInt("num"));
		}
		else {
			this.setDetailsInline(false);
			mAdapter = new GardenPagerAdapter(getSupportFragmentManager());
		}
		
        setContentView(R.layout.main);
		
		if (this.mDetailsInline) {
			
			Log.i("Menhireffect", "OnlineListItemsActivity::onCreate ==> Display Details inline");

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			GardenDetailFragment f = GardenDetailFragment.newInstance(2);
			f.setId(this.mDetailId);
			//f.setGardenItem(mAdapter.getItem(this.mDetailId));
			ft.replace(R.id.mainFragment, f);
			ft.commit();
			
		}
		else {
			
			Log.i("Menhireffect", "OnlineListItemsActivity::onCreate ==> Display list");

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			Fragment f = GardenListFragment.newInstance(2);
			ft.replace(R.id.mainFragment, f);
			ft.commit();
		}
	}
	
	public void setDetailId(int id) {
		this.mDetailId = id;
	}
	
	public void setDetailsCount(int number) {
		this.mDetailsCount = number;
	}
	
	public void setDetailsInline(Boolean detailsInline) {
		this.mDetailsInline = detailsInline;
	}
	
	public void onGardenItemSelected(int position) {
		this.mDetailsInline = true;
	    //this.mDetailId = gardenItem.getId();
	}

}
