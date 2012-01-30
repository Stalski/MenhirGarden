package be.menhireffect.util;

import android.graphics.Bitmap;

public class GardenItem {
    
	int id;
	String title;
	String description;
	String uri;
	String img;
	String categories = "categories";
	Bitmap imageBitmap;
	
	public GardenItem(String title, String uri, String img) {
		this.title = title;
		this.uri = uri;
		this.img = img;
	}
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getImage() {
		return this.img;
	}
	
	public String getTeaserImage() {
		return this.img;
	}
	
	public void setImage(String img) {
		this.img = img;
	}
	
	public String getUri() {
		return this.uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getCategories() {
		return this.categories;
	}
	
	public void setCategories(String categories) {
		this.categories = categories;
	}
	
	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}
	
	public Bitmap getImageBitmap() {
		return this.imageBitmap;
	}
	
}
