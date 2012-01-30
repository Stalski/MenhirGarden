package be.menhireffect.garden;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import be.menhireffect.util.GardenPagerAdapter;

public class GardenDetailActivity extends FragmentActivity {

    GardenPagerAdapter mAdapter;
    
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//        //grdItemList garden_grid
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main);
//
//		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
//				.beginTransaction();
//		GardenDetailFragment f = GardenDetailFragment.newInstance(3);
//
//		Bundle extras = getIntent().getExtras();
//		if (extras != null) {
//			f.setId(extras.getInt("id"));
//		}
//		
//		fragmentTransaction.replace(R.id.mainFragment, f);
//		fragmentTransaction.commit();
//
//	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mAdapter = new GardenPagerAdapter(getSupportFragmentManager(), extras.getInt("id"), extras.getInt("num"));
		}
		else {
			mAdapter = new GardenPagerAdapter(getSupportFragmentManager());
		}
    }

}
