package be.menhireffect.garden;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import be.menhireffect.garden.fragments.GardenGridFragment;

public class OnlineGridItemsActivity extends FragmentActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        //grdItemList garden_grid
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment f = GardenGridFragment.newInstance(1);
		
		fragmentTransaction.replace(R.id.mainFragment, f);
		fragmentTransaction.commit();

	}

}
