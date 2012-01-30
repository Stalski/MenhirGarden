package be.menhireffect.garden.fragments;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import be.menhireffect.garden.OnlineGridItemsActivity;
import be.menhireffect.garden.OnlineListItemsActivity;
import be.menhireffect.garden.R;
import be.menhireffect.util.GardenItem;
import be.menhireffect.util.GardenItemsLoader;
import be.menhireffect.util.ImageThreadLoader;
import be.menhireffect.util.ImageThreadLoader.ImageLoadedListener;

public class GardenGridFragment extends Fragment 
              implements LoaderManager.LoaderCallbacks<ArrayList<GardenItem>> {

    int mNum;
    int mLayout;
    // This is the Adapter being used to display the list's data.
	public GardenGridAdapter mItemsAdapter;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static GardenGridFragment newInstance(int num) {
    	GardenGridFragment f = new GardenGridFragment();

        // Supply number input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putInt("layout", R.layout.fragment_grid);
        f.setArguments(args);

        return f;
    }
    
    @Override
    public void onCreate(Bundle savedInstance) {
        
        if (getArguments() != null) {
        	mNum =  getArguments().getInt("num");
        	mLayout = getArguments().getInt("layout");
        }
        
        super.onCreate(savedInstance);
        
    	mItemsAdapter = new GardenGridAdapter(getActivity().getApplicationContext(),
    			R.layout.garden_item_teaser);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
    	// Get the garden grid and add the adapter to it.
        View view = inflater.inflate(mLayout, container, false);

        // Create an empty adapter we will use to display the loaded data.
        GridView gridView = (GridView) view.findViewById(R.id.grdItemList);
        gridView.setAdapter(this.mItemsAdapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	showDetail(position);
            }
        });
        
        View tv = view.findViewById(R.id.text);
        ((TextView)tv).setText("Grid (" + mNum + ")");
        
        return view;
    }
    
    public void showDetail(int position) {
    	
    	GardenItem item = this.mItemsAdapter.getItem(position);
    	
        Toast.makeText(getActivity().getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        
        // Switch the main view to the Garden Item in detail.
        GardenDetailFragment detailFragment = GardenDetailFragment.newInstance(position);
        detailFragment.setId(position);
        detailFragment.setGardenItem(item);
        // Create new transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.mainFragment, detailFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    	
    }

    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i("Menhireffect", "Grid GardenGridFragment::onActivityCreated");
		setHasOptionsMenu(true);

    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
		
		Context context = getActivity().getApplicationContext();

		// refresh menu item
		menu.findItem(R.id.refresh_option_item).setIntent(new Intent(context, OnlineGridItemsActivity.class));
        
		// Add a drawable icon to switch to list view.
		MenuItem item = menu.add(R.string.list);
        item.setIcon(R.drawable.icon_list);
        item.setIntent(new Intent(context, OnlineListItemsActivity.class));
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
    public Loader<ArrayList<GardenItem>> onCreateLoader(int id, Bundle args) {
    	return new GardenItemsLoader(getActivity().getApplicationContext());
    }

    @Override 
    public void onLoadFinished(Loader<ArrayList<GardenItem>> loader, ArrayList<GardenItem> data) {
        // Set the new data in the adapter.
    	mItemsAdapter.setData(data);
    }

    @Override 
    public void onLoaderReset(Loader<ArrayList<GardenItem>> loader) {
        // Clear the data in the adapter.
    	mItemsAdapter.setData(null);
    }

    public void onItemClick(ListView l, View v, int position, long id) {
        // Insert desired behavior here.
        Log.i("Menhireffect", "Item clicked: " + id);
    }
    
    /**
     * Class GardenItemsGridAdapter
	 * Adapts the List so it's ready for the view.
     */
    public class GardenGridAdapter extends BaseAdapter {
    	
    	private Context mContext;
    	private int resourceId;
    	private ImageThreadLoader imageLoader;
        private List<GardenItem> mThumbIds;

        public GardenGridAdapter(Context c, int resourceId) {
        	mContext = c;
    		this.resourceId = resourceId;
        	this.mThumbIds = new ArrayList<GardenItem>();
        	this.imageLoader = new ImageThreadLoader();
        }

        public GardenGridAdapter(Context c, int resourceId,
    			List<GardenItem> items) {
        	mContext = c;
    		this.resourceId = resourceId;
        	this.mThumbIds = new ArrayList<GardenItem>();
        	this.imageLoader = new ImageThreadLoader();
        }

        public int getCount() {
            return this.mThumbIds.size();
        }

        public GardenItem getItem(int position) {
            return this.mThumbIds.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public void setData(List<GardenItem> data) {
            if (data != null) {
            	//mThumbIds = data;
    			for (GardenItem gardenItem : data) {
    				this.mThumbIds.add(gardenItem);
    			}
    			notifyDataSetChanged();     
            }
        }

        /**
         * Populate new items in the list.
         */
        @Override 
        public View getView(int position, View convertView, ViewGroup parent) {

            final GardenItem item = this.mThumbIds.get(position);
            
    		View listView;
    		if (convertView == null) {
    			LayoutInflater inflater = (LayoutInflater) mContext
    					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    			listView = inflater.inflate(resourceId, parent, false);
    		} else {
    			listView = (View) convertView;
    		}

            // The image
    		final ImageView imageView = (ImageView)listView.findViewById(R.id.bmpGardenItemImage);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(160, 160));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            
            Bitmap cachedImage = null;
            try {
              cachedImage = imageLoader.loadImage(item.getImage(), new ImageLoadedListener() {
                public void imageLoaded(Bitmap imageBitmap) {
                	item.setImageBitmap(imageBitmap);
                	imageView.setImageBitmap(imageBitmap);
                    notifyDataSetChanged();              
                }
              });
            } catch (MalformedURLException e) {
              Log.e("Menhireffect", "Bad remote image URL: " + item.getImage(), e);
            }

            if( cachedImage != null ) {
            	imageView.setImageBitmap(cachedImage);
            }

            // The text.
    		TextView txtName = (TextView) listView
    				.findViewById(R.id.txtGardenItemName);
    		txtName.setText(item.getTitle());
            
            return listView;
        }
        
    }
    
}
