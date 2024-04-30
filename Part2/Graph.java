import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;


/**
 * Graph is the data structure that stores the collection of stops, lines and connections. 
 * The Graph constructor is passed a Map of the stops, indexed by stopId and
 *  a Map of the Lines, indexed by lineId.
 * The Stops in the map have their id, name and GIS location.
 * The Lines in the map have their id, and lists of the stopIDs an times (in order)
 *
 * To build the actual graph structure, it is necessary to
 *  build the list of Edges out of each stop
 * Each pair of adjacent stops in a Line is an edge.
 * We also need to create walking edges between every pair of stops in the whole
 *  network that are closer than walkingDistance.
 */
public class Graph {

    private Collection<Stop> stops;
    private Collection<Line> lines;
    private Collection<Edge> edges = new HashSet<Edge>();      // edges between Stops

    /**
     * Construct a new graph given a collection of stops and a collection of lines.
     * Remove any stops that are not on any lines since they cannot be accessed from anywhere.
     */
    public Graph(Collection<Stop> stps, Collection<Line> lns) {
        stops = new TreeSet<Stop>(stps);
        stops.removeIf((Stop s) -> s.getLines().isEmpty());
        
        lines = lns;

        createAndConnectEdges();

        // printGraphData();   // you could uncomment this to help in debugging your code
    }


    /** Print out the lines and stops in the graph to System.out */
    public void printGraphData(){
        System.out.println("============================\nLines:");
        for (Line line : lines){
            System.out.println(line.getId()+ "("+line.getStops().size()+" stops)");
        }
        System.out.println("\n=============================\nStops:");
        for (Stop stop : stops){
            System.out.println(stop);
            System.out.println("  "+stop.getOutEdges().size()+"  out edges; ");
        }
        System.out.println("===============");
    }


    //============================================
    // Build the graph structure.
    //============================================

    /** 
     * From the loaded Line and Stop information,
     *  identify all the edges that connect stops along a Line.
     * - Construct the collection of all Edges in the graph  and
     * - Construct the forward neighbour edges of each Stop.
     */
    private void createAndConnectEdges() {
        for (Line line : lines) {
            // step through the adjacent pairs of stops in the line
            String transpType =  line.getType();
            for (int i = 0; i < line.getStops().size() - 1; i++) {
                Stop from = line.getStops().get(i);
                Stop to   = line.getStops().get(i+1);
                double distance = from.distanceTo(to);
                Edge edge = new Edge(from, to, transpType, line, distance);
                from.addOutEdge(edge);
                to.addInEdge(edge);
                edges.add(edge);
            }
        }
    }

    //=============================================================================
    //  ***  Recompute Walking edges and add to the graph  ***
    //=============================================================================
    //
    /** 
     * Reconstruct all the current walking edges in the graph,
     * based on the specified walkingDistance:
     * identify all pairs of stops that are at most walkingDistance apart,
     * and construct edges (both ways) between the stops
     * Assumes that all the previous walking edges have been removed
     */
    public void recomputeWalkingEdges(double walkingDistance) {
        removeWalkingEdges();
        for(Stop s1 : stops){
            for(Stop s2: stops){
                if(s1 == s2) continue;
                double distance = s1.distanceTo(s2); 
                if(distance>walkingDistance)continue;
                Edge e1, e2;
                e1 = new Edge(s1, s2, Transport.WALKING, null, distance);
                e2 = new Edge(s2, s1, Transport.WALKING, null, distance);
                //s1.addInEdge(e2);
                s2.addInEdge(e1);
                s1.addOutEdge(e1);
                //s2.addOutEdge(e2);
                edges.add(e1);
                //edges.add(e2);
            }
        }
    }

    /** 
     * Remove all the current walking edges in the graph
     * - from the edges field (the collection of all the edges in the graph)
     * - from the forward neighbours of each Stop.
     */
    public void removeWalkingEdges() {
        List<Edge> toRemove = new ArrayList<>();
        for(var e : edges){
            if(e.transpType().equals(Transport.WALKING)){
                toRemove.add(e);
            }
        }
        edges.removeAll(toRemove);
        for(var s : stops){
            s.deleteInEdgesOfType(Transport.WALKING);
            s.deleteOutEdgesOfType(Transport.WALKING);
        }
    }

    //=============================================================================
    //  Methods to access data from the graph. 
    //=============================================================================

    /**
     * Return a collection of all the lines in the network
     */        
    public Collection<Line> getLines() {
        return Collections.unmodifiableCollection(lines);
    }

    /**
     * Return a collection of all the stops in the network
     */        
    public Collection<Stop> getStops() {
        return Collections.unmodifiableCollection(stops);
    }

    /**
     * Return a collection of all the edges in the network
     */        
    public Collection<Edge> getEdges() {
        return Collections.unmodifiableCollection(edges);
    }

    /**
     * Return the first stop that starts with the specified prefix
     * (first by alphabetic order of name)
     */
    public Stop getFirstMatchingStop(String prefix) {
        prefix = prefix.toLowerCase();
        for (Stop stop : stops) {
            if (stop.getName().toLowerCase().startsWith(prefix)) {
                return stop;
            }
        }
        return null;
    }

    /** 
     * Return all the stops that start with the specified prefix
     * in alphabetic order.
     * This version is not very efficient
     */
    public List<Stop> getAllMatchingStops(String prefix) {
        List<Stop> ans = new ArrayList<Stop>();
        prefix = prefix.toLowerCase();
        for (Stop stop : stops) {
            if (stop.getName().toLowerCase().startsWith(prefix)) {
                ans.add(stop);
            }
        }
        return ans;
    }



}
