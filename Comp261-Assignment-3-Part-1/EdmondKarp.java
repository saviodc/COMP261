
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Map;
import javafx.util.Pair;
import java.util.List;

/** Edmond karp algorithm to find augmentation paths and network flow.
 * 
 * This would include building the supporting data structures:
 * 
 * a) Building the residual graph(that includes original and backward (reverse) edges.)
 *     - maintain a map of Edges where for every edge in the original graph we add a reverse edge in the residual graph.
 *     - The map of edges are set to include original edges at even indices and reverse edges at odd indices (this helps accessing the corresponding backward edge easily)
 *     
 *     
 * b) Using this residual graph, for each city maintain a list of edges out of the city (this helps accessing the neighbours of a node (both original and reverse))

 * The class finds : augmentation paths, their corresponing flows and the total flow
 * 
 * 
 */

public class EdmondKarp {
    // class members

    //data structure to maintain a list of forward and reverse edges - forward edges stored at even indices and reverse edges stored at odd indices
    private static Map<Integer,Edge> edges; 

    // Augmentation path and the corresponding flow
    private static ArrayList<Pair<ArrayList<Integer>, Integer>> augmentationPaths =null;

    
    //TODO:Build the residual graph that includes original and reverse edges 
    public static void computeResidualGraph(Graph graph){
        // TODO
        //initialise map
        edges = new HashMap<>();
        //iterate through graph's edges
        List<Edge> originalEdges = new ArrayList<>(graph.getOriginalEdges());
        for(int i = 0; i < originalEdges.size(); i += 2){
            //for every edge get to and from and make egde with 0,0
            Edge forw = originalEdges.get(i);
            City from = forw.fromCity();
            City to  = forw.toCity();
            Edge backw = new Edge(to, from, 0, 0);
            //insert in index and index +1
            edges.put(i, forw);
            edges.put(i+1, backw);
            from.addEdgeId(i);
            to.addEdgeId(i+1); 
        }
        
        //printResidualGraphData(graph);  //may help in debugging
        // END TODO
    }

    // Method to print Residual Graph 
    public static void printResidualGraphData(Graph graph){
        System.out.println("\nResidual Graph");
        System.out.println("\n=============================\nCities:");
        for (City city : graph.getCities().values()){
            System.out.print(city.toString());

            // for each city display the out edges 
            for(Integer eId: city.getEdgeIds()){
                System.out.print("["+eId+"] ");
            }
            System.out.println();
        }
        System.out.println("\n=============================\nEdges(Original(with even Id) and Reverse(with odd Id):");
        edges.forEach((eId, edge)->
                System.out.println("["+eId+"] " +edge.toString()));

        System.out.println("===============");
    }

    //=============================================================================
    //  Methods to access data from the graph. 
    //=============================================================================
    /**
     * Return the corresonding edge for a given key
     */

    public static Edge getEdge(int id){
        return edges.get(id);
    }

    /** find maximum flow
     * 
     */
    // TODO: Find augmentation paths and their corresponding flows
    public static ArrayList<Pair<ArrayList<Integer>, Integer>> calcMaxflows(Graph graph, City from, City to) {
        //TODO
        int maxFlow = 0;
        augmentationPaths = new ArrayList<Pair<ArrayList<Integer>, Integer>>();
        computeResidualGraph(graph);
        //while able to find path
        //if path found 
        // add pair to augmentation paths update maxFlow += value of the pair
        while(true){
            Pair<ArrayList<Integer>, Integer> augPath = bfs(graph, from, to);
            if(augPath == null)break;
            List<Integer> listAt = augPath.getKey();
            maxFlow += augPath.getValue();
            augmentationPaths.add(augPath);
        }
        System.out.println(maxFlow);
        // END TODO
        return augmentationPaths;
    }

    // TODO:Use BFS to find a path from s to t along with the correponding bottleneck flow
    public static Pair<ArrayList<Integer>, Integer>  bfs(Graph graph, City s, City t) {

        ArrayList<Integer> augmentationPath = new ArrayList<Integer>();
        Map<City, Integer> backPointer = new HashMap<City, Integer>();
        Queue<City> q  = new ArrayDeque<>();
        q.offer(s);
        while(!q.isEmpty()){
            City cur = q.poll();
            for(int i : cur.getEdgeIds()){
                Edge e = edges.get(i);
                if(e.toCity()!= s && !backPointer.containsKey(i) && e.capacity() > 0){
                    backPointer.put(e.toCity(), i);
                    if(backPointer.get(t) != null){
                        
                    }
                    q.offer(e.toCity());
                }
            }
        }
        if(!backPointer.containsKey(t))return null;
        List<Integer> listAt = fromBackPointers(backPointer, s, t);
        //move to parent method
        int flowThrough = findBottleNeck(listAt);
            updatePath(listAt, flowThrough);
        // TODO
        //get bottle neck, reverse list of edges, add as a pair to augmentation paths, return
        
        // END TODO
        return new Pair(listAt,flowThrough);
    }
    
    public static List<Integer> fromBackPointers(Map<City, Integer> backPointer, City s, City t){
        List<Integer> toRet = new ArrayList<>();
        City at = t;
        while(at != s){
            if(!backPointer.containsKey(at))throw new IllegalArgumentException("Not FOUND");
            toRet.add(backPointer.get(at));
            at = edges.get(backPointer.get(at)).toCity();
        }
        Collections.reverse(toRet);
        return toRet;
    }
    
    public static void updatePath(List<Integer> toUpdate, int flowThrough){
        //for all in list
        //flow += fT
        //cap -= fT
        for(int i : toUpdate){
            Edge forw, backw;
            if(i%2 ==0){
                 forw = edges.get(i);
                 backw = edges.get(i+1);
            }else{
                 forw = edges.get(i);
                 backw = edges.get(i-1);
            }
            forw.setCapacity(forw.capacity()-flowThrough);
            forw.setFlow(forw.flow() + flowThrough);
            backw.setCapacity(backw.capacity() + flowThrough);
            backw.setFlow(backw.flow() + flowThrough);
        }
    }
    
    public static int findBottleNeck(List<Integer> nodes){
        int bottle = Integer.MAX_VALUE;
        for(int i : nodes){
            int val = edges.get(i).capacity();
            bottle = Math.min(bottle, val);
        }
        if(bottle == Integer.MAX_VALUE)return 0;
        return bottle;
    }
}


