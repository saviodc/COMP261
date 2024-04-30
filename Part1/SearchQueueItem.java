/**
 * AStar search uses a priority queue of partial paths
 * that the search is building.
 * Each partial path needs several pieces of information
 * to specify the path to that point, its cost so far, and
 * its estimated total cost
 */

public class SearchQueueItem implements Comparable<SearchQueueItem> {
    private Stop s;
    private Edge edge;
    private double heuristic;
    private double lengthTo;
    
    public SearchQueueItem(Stop s, Edge edge, double lengthTo, double heuristic){
        this.s = s;
        this.edge = edge;
        this.lengthTo = lengthTo;
        this.heuristic = heuristic;
    }
    
    public Stop stop(){return s;}

    public Edge edge(){return edge;}
    
    public double heuristic(){return heuristic;}
    
    public double lengthTo(){return lengthTo;}
    
    // stub needed for file to compile.
    public int compareTo(SearchQueueItem other) {
        return Double.compare(this.heuristic, other.heuristic);
    }
}
