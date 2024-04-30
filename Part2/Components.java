import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

//=============================================================================
//   Finding Components
//   Finds all the strongly connected subgraphs in the graph
//   Constructs a Map recording the number of the subgraph for each Stop
//=============================================================================

public class Components{

    // Based on Kosaraju's_algorithm
    // https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm
    // Use a visited set to record which stops have been visited

    
    public static Map<Stop,Integer> findComponents(Graph graph) {
        Map<Stop, Integer> components = new HashMap<Stop,Integer>();
        Set<Stop> vis = new HashSet<>();
        List<Stop> list = new ArrayList<>();
        for(Stop s :graph.getStops()){
            if(!vis.contains(s)){
                forwardVisit(s, list, vis);
            }
        }
    
        java.util.Collections.reverse(list);
        int componentNum = 0;
        for(Stop s : list){
            if(!components.keySet().contains(s)){
                backwardVisit(s, componentNum, components);
                componentNum++;
            }
        }

        return components ; 
    }

    static void forwardVisit(Stop s, List<Stop> l, Set<Stop> v){
        if(!v.contains(s)){
            v.add(s);
            for(Edge n : s.getOutEdges()){
                forwardVisit(n.toStop(), l ,v);
            }
            l.add(s);
        }
        }
        
    static void backwardVisit(Stop s, int num, Map<Stop, Integer> m){
        if(!m.keySet().contains(s)){
            m.put(s, num);
            for(Edge n : s.getInEdges()){
                backwardVisit(n.fromStop(), num, m);
            }
        }
    }
}
        
    


