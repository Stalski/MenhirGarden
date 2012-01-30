package be.menhireffect.garden.fragments;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import be.menhireffect.garden.OnGardenItemSelectedListener;
import be.menhireffect.garden.OnlineGridItemsActivity;
import be.menhireffect.garden.OnlineListItemsActivity;
import be.menhireffect.garden.R;
import be.menhireffect.util.GardenItem;
import be.menhireffect.util.GardenItemsLoader;
import be.menhireffect.util.ImageThreadLoader;
import be.menhireffect.util.ImageThreadLoader.ImageLoadedListener;

public final class GardenListFragment extends ListFragment 
		implements LoaderManager.LoaderCallbacks<ArrayList<GardenItem>> {
	
    int mNum;
    GardenListAdapter mItemsAdapter;
    private OnGardenItemSelectedListener mOnGardenItemSelectedListener;

    /**
     * Create a new instance of CountingFragment, providing "number"
     * as an argument.
     */
    public static GardenListFragment newInstance(int num) {
        GardenListFragment f = new GardenListFragment();

        // Supply number input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	
        super.onActivityCreated(savedInstanceState);

        // Create an empty adapter we will use to display the loaded data.
        // Remember the ArrayAdapter subclass can take a constructor with items as third param.
        mItemsAdapter = new GardenListAdapter(getActivity(), android.R.layout.simple_list_item_2);
        setListAdapter(mItemsAdapter);
        
		setHasOptionsMenu(true);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        View tv = view.findViewById(R.id.text);
        ((TextView)tv).setText("List (" + mNum + ")");
        
        return view;
        
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
		
		Context context = getActivity().getApplicationContext();

		// refresh menu item
		menu.findItem(R.id.refresh_option_item).setIntent(new Intent(context, OnlineListItemsActivity.class));
        
		// Add a drawable icon to switch to list view.
		MenuItem item = menu.add(R.string.grid);
        item.setIcon(R.drawable.grid);
        item.setIntent(new Intent(context, OnlineGridItemsActivity.class));
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.refresh_option_item:
				getActivity().startActivity(item.getIntent());
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    try {
	        mOnGardenItemSelectedListener = (OnGardenItemSelectedListener) activity;
	    } catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString() + "Must Implement OnGardenItemSelectedListener.");
	    }
	}

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	
		GardenItem item = mItemsAdapter.getItem(position);
		
		// When clicked, show a toast with the TextView text
        Toast.makeText(getActivity().getApplicationContext(), "Loading " + item.getTitle(),
          Toast.LENGTH_SHORT).show();
        
        // Example of how to notify the parent activity from this fragment.
        super.onListItemClick(l, v, position, id);
        mOnGardenItemSelectedListener.onGardenItemSelected(position);
		
		// Example of how to switch complete activity
//		Context context = getActivity().getApplicationContext();
//		Intent i = new Intent(context, OnlineListItemsActivity.class);
//		i.putExtra("id", item.getId());
//		i.putExtra("num", mItemsAdapter.getCount());
//		i.putExtra("detail", true);
//		startActivity(i);

        // Example of how to switch fragments into the mainFragment
        // Switch the main view to the Garden Item in detail.
//        GardenDetailFragment detailFragment = GardenDetailFragment.newInstance(position);
//        detailFragment.setId(position);
//        detailFragment.setGardenItem(item);
//        detailFragment.setGardenItemsAdapter(mItemsAdapter);
//        // Create new transaction
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        // Replace whatever is in the fragment_container view with this fragment,
//        // and add the transaction to the back stack
//        transaction.replace(R.id.mainFragment, detailFragment);
//        transaction.addToBackStack(null);
//        // Commit the transaction
//        transaction.commit();
        
    }

	@Override
	public Loader<ArrayList<GardenItem>> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created. This
		// sample only has one Loader with no arguments, so it is simple.
		return new GardenItemsLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<GardenItem>> loader,
			ArrayList<GardenItem> data) {
		// Set the new data in the adapter.
		mItemsAdapter.setData(data);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<GardenItem>> loader) {
		// Clear the data in the adapter.
		mItemsAdapter.setData(null);
	}
    
	/**
	 * GardenListAdapter
	 * Adapts the List so it's ready for the view.
	 */
	public class GardenListAdapter extends ArrayAdapter<GardenItem> {
	
		private final LayoutInflater mInflater;
		private ImageThreadLoader imageLoader = new ImageThreadLoader();
	
		public GardenListAdapter(Context c, int layout) {
			super(c, layout);
			mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	
		public void setData(List<GardenItem> data) {
			clear();
			if (data != null) {
				for (GardenItem gardenItem : data) {
					add(gardenItem);
				}
			}
		}
	
		/**
		 * Populate new items in the list.
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	
			View view;
			TextView textTitle;
			final ImageView image;
	
			if (convertView == null) {
				view = mInflater.inflate(R.layout.list_item_icon_text, parent,
						false);
			} else {
				view = convertView;
			}
	
			try {
				textTitle = (TextView) view.findViewById(R.id.text);
				image = (ImageView) view.findViewById(R.id.icon);
			} catch (ClassCastException e) {
				Log.e("Menhireffect",
						"Your layout must provide an image view and text view with ID's icon and text.",
						e);
				throw e;
			}
	
			final GardenItem item = getItem(position);
	
			// The image
			Bitmap cachedImage = null;
			try {
				cachedImage = imageLoader.loadImage(item.getImage(),
						new ImageLoadedListener() {
							public void imageLoaded(Bitmap imageBitmap) {
			                	item.setImageBitmap(imageBitmap);
								image.setImageBitmap(imageBitmap);
								notifyDataSetChanged();
							}
						});
			} catch (MalformedURLException e) {
				Log.e("Menhireffect", "Bad remote image URL: " + item.getImage(), e);
			}
	
			if (cachedImage != null) {
				image.setImageBitmap(cachedImage);
			}
	
			// The title
			textTitle.setText(item.getTitle());
	
			return view;
		}
	}
}