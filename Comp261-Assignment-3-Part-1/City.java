import java.util.Collection;
import java.util.Collections;
import javafx.geometry.Point2D;
import java.util.HashSet;
import javafx.util.Pair;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;


/**
 * Structure for holding city information
 */

public class City implements Comparable<City> {
    // location of the city
    private Point2D loc;
    private String name;
    private int id;

    // data structure for holding routes between cities to support EdmondKarp 
    //for efficient storage and look-up instead of maintaining a set of forward and backward(reverse) edges we just maintain a list of edge IDs where even ID indicates a forward edge and odd ID represents a backward(reverse) edge
    private Collection<Integer> edgeIds = new HashSet<Integer>();
    
  
    /**
     * Constructor for a city
     * 
     * @param id   4  digit city id
     * @param name Long name for the city
     * @param lat
     * @param lon
     */
    public City(double x, double y, String name, int id) {
        this.loc = new Point2D(x, y);
        this.name = name;
        this.id = id;

    }

    //--------------------------------------------
    //  Getters and basic properties of a City
    //--------------------------------------------

    /**
     * Get the location of the City
     */
    public Point2D getPoint() {
        return this.loc;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    /**
     * Returns distance in meters between this City and a Location
     */
    public double distanceTo(Point2D loc) {
        return this.loc.distance(loc);
    }

    /**
     * Returns distance in meters between this City and another City
     */
    public double distanceTo(City toCity) {
        return this.loc.distance(toCity.loc);
    }

    /**
     * Compare by alphabetic order of name,
     */
    public int compareTo(City other){
        return this.name.compareTo(other.name);
    }

    /** 
     * Display a City
     * 
     * @return string of the city information in the format: XXXX: name at (x,y)
     */
    public String toString() {
        // TODO: decide how you want to print a City
        //return id + ": " + name + " at (" + getPoint().getX() + ", " + getPoint().getY() + ")";
        return id + ": " + name;
    }

    /**
     * @param a Point to check if the city is in an **identical** location
     * @return is this city is at the same location as the given point
     */
    public boolean atLocation(Point2D point) {
        return this.loc.equals(point);
    }

        //set of edge indices out of the city  - these would include actual edges in the graph and the reverse edges that we add to support Edmond-Karp algorithm
    //  
    //--------------------------------------------
    /** Get the collection of edges out of the city - remember this also includes reverse edges added to support Edmond-karp algorithm*/
    public Collection<Integer> getEdgeIds() {
        return Collections.unmodifiableCollection(edgeIds);
    }
   

    /** add a new edge  */
    public void addEdgeId(int edgeId) {
        this.edgeIds.add(edgeId);
    }
    
    
    
}
