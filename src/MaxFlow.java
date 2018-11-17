import java.util.*; 
import java.lang.*; 
import java.io.*; 
import java.util.LinkedList; 

class MaxFlow
{
    private int V;
    public int rGraph[][];
    public MaxFlow(int nodes) {
        this.V = nodes;
    }

    boolean bfs(int rGraph[][], int s, int t, int parent[])
    {
        boolean visited[] = new boolean[V];
        for(int i=0; i<V; ++i)
            visited[i]=false;

        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        visited[s] = true;
        parent[s]=-1;

        // Standard BFS Loop 
        while (queue.size()!=0)
        {
            int u = queue.poll();
  
            for (int v=0; v<V; v++) 
            { 
                if (visited[v]==false && rGraph[u][v] > 0) 
                {
                    queue.add(v); 
                    parent[v] = u; 
                    visited[v] = true; 
                }
            }
        }
        return (visited[t] == true); 
    } 
  

    int fordFulkerson(int graph[][], int s, int t) 
    {
//        for(int i=0;i<V;i++) {
//        	for(int j=0;j<V;j++) {
//        		System.out.print(graph[i][j]);
//        	}
//        	System.out.println();
//        }
        int u, v;
        int rGraph[][] = new int[V][V]; 
  
        for (u = 0; u < V; u++) 
            for (v = 0; v < V; v++) 
                rGraph[u][v] = graph[u][v]; 
 
        int parent[] = new int[V]; 
  
        int max_flow = 0; 

        while (bfs(rGraph, s, t, parent)) 
        {
            int path_flow = Integer.MAX_VALUE; 
            for (v=t; v!=s; v=parent[v]) 
            { 
                u = parent[v]; 
                path_flow = Math.min(path_flow, rGraph[u][v]); 
            }
            for (v=t; v != s; v=parent[v]) 
            { 
                u = parent[v]; 
                rGraph[u][v] -= path_flow; 
                rGraph[v][u] += path_flow; 
            }
            max_flow += path_flow; 
        }
        this.rGraph = rGraph;

        return max_flow;
    }
}


