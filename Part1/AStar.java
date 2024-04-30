/**
 * Implements the A* search algorithm to find the shortest path
 *  in a graph between a start node and a goal node.
 * If start or goal are null, it returns null
 * If start and goal are the same, it returns an empty path
 * Otherwise, it returns a Path consisting of a list of Edges that will
 * connect the start node to the goal node.
 */

import java.util.Collections;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;


public class AStar {

    /**
     * Finds the shortest path between two stops
     */
    public static List<Edge> findShortestPath(Stop start, Stop goal) {
        if(start == null || goal == null) return null;
        if(start == null) return List.of();
        PriorityQueue<SearchQueueItem> fringe = new PriorityQueue<>();
        Set<Stop> visited = new HashSet<>();
        Map<Stop, Edge> backPointers = new HashMap<>();
        fringe.add(new SearchQueueItem(start, null, 0, start.distanceTo(goal)));
        while(!fringe.isEmpty()){
            SearchQueueItem process = fringe.poll();
            Stop stop = process.stop();
            if(!visited.contains(stop)){
                visited.add(stop);
                backPointers.put(stop,process.edge());
                if(stop.equals(goal)){return reverseBackPointer(backPointers, goal, start);}
                
                for(Edge e : stop.getEdges()){
                    Stop to = e.toStop();
                    if(!visited.contains(to)){
                        double lengthTo= e.distance() + process.lengthTo();
                        double estimateTotal = lengthTo + to.distanceTo(goal);
                        fringe.add(new SearchQueueItem(to, e, lengthTo, estimateTotal));
                    }
                }
            }
        }
        return null; // to make the template compile!
    }
    
    public static List<Edge> reverseBackPointer(Map<Stop, Edge> backPointers, Stop end, Stop start){
        List<Edge> forwards = new ArrayList<>();
        Stop finish = end;
        do{
            Edge toAdd = backPointers.get(finish);
            forwards.add(toAdd);
            finish = toAdd.fromStop();
        }while(finish != start);
        Collections.reverse(forwards);
        return forwards;
    }
}
