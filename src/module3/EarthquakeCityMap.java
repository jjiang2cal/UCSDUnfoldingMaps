package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Jingjing
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;
	// Define constant colors of yellow, blue and red
	public final int YELLOW = color(255, 255, 0);
    public final int BLUE = color(0, 0, 255);
    public final int RED = color(255, 0, 0);
    public final int BLACK = color(0, 0, 0);

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
	    int blue = color(0, 0, 255);
	    int red = color(255, 0, 0);
	    
	    //TODO: Add code here as appropriate
	    //From the list of earthquakes, create the list of markers
	    createMarkersFromPointFeature(markers, earthquakes);
	    //add markers to map
	    map.addMarkers(markers);
	    
	}
	
	// A helper method that takes a list of earthquake features and
	// generates makers for each earthquake
	private void createMarkersFromPointFeature(List<Marker> markers, List<PointFeature> earthquakes) {
		for (PointFeature f: earthquakes) {
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	SimplePointMarker ptMarker = this.createMarker(f);
	    	// if earthquake is minor, set the marker to blue and radius to 5
	    	// if earthquake is moderate, set the marker to yellow and radius to 10
	    	// if earthquake is big, set the marker to red and radius to 15
	    	if (mag < THRESHOLD_LIGHT) {
	    		ptMarker.setColor(BLUE);
	    		ptMarker.setRadius(5);
	    	}  else if (mag < THRESHOLD_MODERATE) {
	    		ptMarker.setColor(YELLOW);
	    		ptMarker.setRadius(10);
	    	}  else {
	    		ptMarker.setColor(RED);
	    		ptMarker.setRadius(15);
	    	}
	    	markers.add(ptMarker);
	    }
		return;
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// finish implementing and use this method, if it helps.
		return new SimplePointMarker(feature.getLocation());
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		fill(255, 229, 204);
		rect(25, 50, 150, 250);
		fill(BLACK);
		textSize(14);
		text("Earthquake key", 50, 80);
		fill(RED);
		ellipse(50, 120, 15, 15);
		fill(YELLOW);
		ellipse(50, 160, 10, 10);
		fill(BLUE);
		ellipse(50, 200, 5, 5);
		fill(BLACK);
		textSize(12);
		text("5.0+ Magnitude", 70, 125);
		text("4.0+ Magnitude", 70, 165);
		text("Below 4.0", 70, 205);
	
	}
}
