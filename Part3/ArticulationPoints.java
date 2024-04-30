import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

//=============================================================================
//   Finding Articulation Points
//   Finds and returns a collection of all the articulation points in the undirected
//   graph.
//   Uses the algorithm from the lectures, but modified to cope with a not completely
//   connected graph. For a not fully connected graph, an articulation point is one
//   that would break a currently connected component into two or more components
//=============================================================================

public class ArticulationPoints{

    /**
     * Return a collection of all the Stops in the graph that are articulation points.
     */
    public static Collection<Stop> findArticulationPoints(Graph graph) {
        for(Stop s : graph.getStops()){
            s.depth = -1;
        }
        Set<Stop> artPoints = new HashSet<>();
        
        for(Stop s : graph.getStops()){
            if(s.depth != -1)continue;
            int numSubtrees = 0;
            s.depth = 0;
            for(Stop ns :s.neighbours()){
                if(ns.depth == -1){
                    recArtPts(ns, 1, s, artPoints);
                    numSubtrees++;
                }
            }
            if(numSubtrees > 1)artPoints.add(s);
        }
        return artPoints;
    }
    
    private static int recArtPts(Stop s, int depth, Stop from, Set<Stop> artPoints){
        s.depth = depth;
        int reachBack = depth;
        for(Stop ns : s.neighbours()){
            if(ns == from)continue;
            else if (ns.depth != -1)reachBack = Math.min(ns.depth, reachBack);
            else{
                int neighReachBack = recArtPts(ns, depth +1, s, artPoints);
                if(neighReachBack >= depth)artPoints.add(s);
                reachBack = Math.min(neighReachBack,reachBack); 
            }
        }
        return reachBack;
    }



}
