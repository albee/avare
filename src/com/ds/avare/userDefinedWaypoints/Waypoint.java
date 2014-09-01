package com.ds.avare.userDefinedWaypoints;

import com.ds.avare.StorageService;
import com.ds.avare.gps.GpsParams;
import com.ds.avare.position.Origin;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Waypoint {
    final String 	mName;
    final String 	mDescription;
    final float  	mLat;
    final float  	mLon;
    final float  	mAlt;
    final boolean 	mShowDist;
    final int 		mMarkerType;

    public Waypoint(String name, String description, float lat, float lon, float alt, boolean showDist, int markerType) {
    	if(null != name) {
    		this.mName = name;
    	} else {
    		this.mName = "UNDEF";
    	}
    	
    	if(null != description) {
    		this.mDescription = description;
		} else {
			this.mDescription= "UNDEF";
		}

    	this.mLat = lat;
        this.mLon = lon;
        this.mAlt = alt;
        this.mShowDist = showDist;
        this.mMarkerType = markerType;
    }
    
    public String getName() { return mName; }
    public String getDesc() { return mDescription; }
    public float getLat() { return mLat; }
    public float getLon() { return mLon; }
    
    public static final int CYANDOT = 0;
    
    public void draw(Canvas canvas, Origin origin, boolean trackUp, GpsParams gpsParams, Paint paint, StorageService service, String dstBrg, float size ) {
		// Map the lat/lon to the x/y of the current canvas
		float x = (float) origin.getOffsetX(mLon);
		float y = (float) origin.getOffsetY(mLat);
	
		switch(mMarkerType){
			case Waypoint.CYANDOT: {
				// Draw the filled circle, centered on the point
				paint.setStyle(Style.FILL);
				paint.setColor(Color.CYAN);
				paint.setAlpha(0x9F);
		        canvas.drawCircle(x, y, (float) size * 3, paint);
	
		        // A black ring around it to highlight it a bit
		        paint.setStyle(Style.STROKE);
		        paint.setColor(Color.BLACK);
		        paint.setStrokeWidth(size);
		        canvas.drawCircle(x, y, (float) size * 3, paint);
		        break;
			}
		}
		
	    // Set the display text properties
		paint.setStyle(Style.FILL);
	    paint.setColor(Color.WHITE);

	    // If we are in track up mode, then we need to rotate the text so it shows
	    // properly
	    boolean bRotated = false;
        if (trackUp && (gpsParams != null)) {
        	bRotated = true;
            canvas.save();
            canvas.rotate((int) gpsParams.getBearing(), x, y);
        }

	    // Draw the name above
	    service.getShadowedText().draw(canvas, paint, mName, Color.BLACK, x, y - size * 12);
	    
	    // and the distance/brg below IF that piece of metadata is true
	    if(true == mShowDist) {
	        service.getShadowedText().draw(canvas, paint, dstBrg, Color.BLACK, x, y + size * 12);
	    }
	    
	    // Restore canvas if we rotated it
        if (true == bRotated) {
            canvas.restore();
        }

    }
}