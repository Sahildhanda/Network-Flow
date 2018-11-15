/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmondkarp;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 *
 * @author pshre
 */
public class EdmondKarpMaxNetworkFlow {

    /* This stores maximum flow capacity of edges
     * also indicates which nodes are connected since
     * edges with 0 capacity are disconnected
     */
    private long maxFlow[][];

    /* This stores minimum flow capacity of edges */
    private long minFlow[][];

    /* Indicates current flow for this edge and
     * should be always less than or equal to maxFlow of the edge
     */
    private long currentFlow[][];

    /* To keep track of visited nodes in BFS traversal */
    private boolean visited[];

    /* To keep track of backward path to source in order to find min capacity of the path */
    private int parents[];

    /* Number of nodes in the graph ranges from 0 to n-1 */
    private int nodes;

    public EdmondKarpMaxNetworkFlow(int nodes) {
        this.nodes = nodes;
        this.maxFlow = new long[nodes][nodes];
        this.minFlow = new long[nodes][nodes];
        this.currentFlow = new long[nodes][nodes];
        this.parents = new int[nodes];
        this.visited = new boolean[nodes];
    }

    /*
     * This method adds an edge between nodes
     * Edges are added between nodes if flow capacity is greater than 0
     * else it throws an exception
     */
    public void addEdge(int src, int dest, long minFlow, long maxFlow) throws Exception {
        if (maxFlow < 0 || src < 0 || dest < 0) {
            throw new Exception("Invalid Input: node and maxFlow must be non negative");
        }
        this.maxFlow[src][dest] = maxFlow;
        this.minFlow[src][dest] = minFlow;
    }

    /*
     * This is a generic method do BFS traversal of given graph to reach from source to destination
     * Edge is chosen only if current flow is less than the max flow for the edge
     * Also populates the parent list for all nodes in the path
     */
    private boolean bfsTraversal(int src, int dest) {
        Queue<Integer> queue = new ArrayDeque<Integer>();
	boolean pathFound = false;
        int node;

	Arrays.fill(visited, false);
        visited[src] = true;
        queue.add(src);

        while (!queue.isEmpty()) {
            node = queue.remove();
            if (node == dest) {
                pathFound = true;
                break;
            }
            for (int adjNode = 0; adjNode < nodes; adjNode++) {
                if (!visited[adjNode] && maxFlow[node][adjNode] > currentFlow[node][adjNode]) {
                    visited[adjNode] = true;
                    queue.add(adjNode);
                    parents[adjNode] = node;
                }
            }
        }
        return pathFound;
    }

    /*
     * This is a specific method finds a maximum network flow in a given graph
     * If no such flow exists then it return 0 else maximum flow
     */
    public long maxNetworkFlowOne(int src, int dest) {
        int maxNetworkFlow = 0;
        for(int i = 0; i < maxFlow.length; i++) {
	    for(int j = 0; j < maxFlow[i].length; j++) {
            	System.out.print(maxFlow[i][j]+" ");
            }
            System.out.println("");
        }
        while (true) {
            boolean pathExists = bfsTraversal(src, dest);
            if (!pathExists) {
		/* If no path exists then stop traversal and find max flow from source */
                break;
            }

	    /* Find the minimum flow in choose BFS path */
            int node = dest;
            long minFlowInPath = maxFlow[parents[dest]][dest] - currentFlow[parents[dest]][dest];
            node = parents[dest];

	    while (node != src) {
                minFlowInPath = Math.min(minFlowInPath, (maxFlow[parents[node]][node]
                        - currentFlow[parents[node]][node]));
                node = parents[node];
            }

	    /* Update current flow by adding minimum flow in the current path, also add augmented path */
            node = dest;
            while (node != src) {
                currentFlow[parents[node]][node] += minFlowInPath;
                currentFlow[node][parents[node]] -= minFlowInPath;
                node = parents[node];
            }

	    /* Reset parents array for new BFS path */
            Arrays.fill(parents,0);
        }

	/* Find maximum flow from source since all paths are covered */
        for (int node = 0; node < nodes; node++) {
            if (maxFlow[src][node] != 0) {
		/* Add max flow only if there was any edge between node and src originally */
                maxNetworkFlow += currentFlow[src][node];
            }
        }
        System.out.println("Max flow is " + maxNetworkFlow);
        return maxNetworkFlow;
    }

    /*
     * This is a generic method is to print worker-task assignment
     * It also checks whether solution is possible for given input
     */
    public void printSolution(int workerStartIndex, int taskStartIndex, int totalWorkers, int totalTasks) {
	StringBuilder response = new StringBuilder();
	boolean flag = false;
        for (int i = taskStartIndex; i < taskStartIndex + totalTasks; i++) {
	    /* Check if task has its constraints matched */
	    if (currentFlow[i][nodes-1] > maxFlow[i][nodes-1] || currentFlow[i][nodes-1] < minFlow[i][nodes-1]) {
	    	    /* This task didn't meet min max constraints so no solution is possible */
		    flag = true;
		    break;

	    }
            for (int j = workerStartIndex; j < workerStartIndex + totalWorkers; j++) {
                if (currentFlow[j][i] == 1) {
		    /* If task is assigned to worker then current flow from worker to task will be 1 */
                    response.append("Task " + (i - taskStartIndex) + " is assigned to worker " + (j - workerStartIndex) + "\n");
                }
		/* Check if worker has its constraints matched */
	    	if (currentFlow[0][j] > maxFlow[0][j] || currentFlow[0][j] < minFlow[0][j]) {
	    	    /* This task didn't meet min max constraints so no solution is possible */
		    flag = true;
		    break;

	    	}
            }
        }
	if (flag) {
	    System.out.println("Solution is not possible for this input");
	} else {
	    System.out.println(response.toString());
	}
    }
}
