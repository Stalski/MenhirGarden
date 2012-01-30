package be.menhireffect.garden.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.menhireffect.garden.R;
import be.menhireffect.garden.fragments.GardenListFragment.GardenListAdapter;
import be.menhireffect.util.GardenItem;

/**
 * GardenDetailFragment
 * Fragment to display the detail of a GardenItem.
 */
public class GardenDetailFragment extends Fragment {

    int mNum;
    int id;
    GardenItem gardenItem;
    GardenListAdapter listAdapter;

    /**
     * Create a new instance of CountingFragment, providing "number"
     * as an argument.
     */
    public static GardenDetailFragment newInstance(int num) {
    	
    	GardenDetailFragment f = new GardenDetailFragment();
    	Log.i("Menhireffect", "GardenDetailFragment initiated");

        // Supply number input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }
    
    /**
     * Set the identifier.
     */
    public void setId(int id) {
    	this.id = id;
    }
    
    /**
     * Set the Garden Item.
     */
    public void setGardenItem(GardenItem item) {
    	this.gardenItem = item;
    }
    
    /**
     * Set the Garden Item.
     */
    public void setGardenItemsAdapter(GardenListAdapter adapter) {
    	this.listAdapter = adapter;
    }

    /**
     * Process the arguments into this object while creating the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i("Menhireffect", "GardenDetailFragment onCreate");
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	
        super.onActivityCreated(savedInstanceState);
        
        // No adapter needed as long as this fragment is started from a list
        // fragment. Since the image will need to be refetched for a better 
        // resolution, I suppose the GardenDetailActivity will take over.
        
		// Options in the scope of this fragment.
        // setHasOptionsMenu(true);
        
    }

    /**
     * The Fragment's UI consists of a layout with several view components.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	Log.i("Menhireffect", "Creating detail fragment with several components. id = " + this.id);
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        
        View itemTitle = view.findViewById(R.id.itemTitle);
        ((TextView)itemTitle).setText(this.gardenItem.getTitle());
        
        View itemCategories = view.findViewById(R.id.itemCategories);
        ((TextView)itemCategories).setText(this.gardenItem.getCategories());
        
        View itemDescription = view.findViewById(R.id.itemDescription);
        ((TextView)itemDescription).setText(this.gardenItem.getDescription());
        
		final ImageView imageView = (ImageView)view.findViewById(R.id.itemImage);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(700, 700));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);
        imageView.setImageBitmap(this.gardenItem.getImageBitmap());
        
        return view;
        
    }

}
