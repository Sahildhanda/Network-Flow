/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmondkarp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author pshre
 */
public class SolutionOne {
    public static void main(String[] args) throws IOException, Exception {
        
	String str;
	
	try {
	    /* Note down the start time */
	    long startTime = System.nanoTime();

	    /* Read input files for workers and tasks */
            File taskFile = new File("./src/task");
            BufferedReader taskReader = new BufferedReader(new FileReader(taskFile));
            int totalTask = Integer.parseInt(taskReader.readLine());

	    File workerFile = new File("./src/worker");
            BufferedReader workerReader = new BufferedReader(new FileReader(workerFile));
            int totalWorker = Integer.parseInt(workerReader.readLine());
            int totalNodes = totalWorker + totalTask + 2;

	    final int SOURCE_INDEX = 0;
            final int SINK_INDEX = totalNodes - 1;
            final int taskStartIndex = SOURCE_INDEX + totalWorker + 1;
            final int workerStartIndex = SOURCE_INDEX + 1;

	    /* Validate input params */
	    if (totalWorker == 0 || totalTask == 0) {
	    	System.out.println("Input provided is invalid");
	    }

            EdmondKarpMaxNetworkFlow graph = new EdmondKarpMaxNetworkFlow(totalNodes);

	    /* Read Task details */
            for (int i = 1; i < totalTask + 1; i++) {
                str = taskReader.readLine();
                String[] taskDetail = str.split(",");
                int taskId = taskStartIndex + Integer.parseInt(taskDetail[0]);
                int minWorkersForTask = Integer.parseInt(taskDetail[1]);
                int maxWorkersForTask = Integer.parseInt(taskDetail[2]);

	        /* Read all the workers qualifies to do this task */
                for (int j = 3; j < taskDetail.length; j++) {
                    int worker = workerStartIndex + Integer.parseInt(taskDetail[j]);
		    /* Assuming in file tasks starts from number 0 */
                    graph.addEdge(worker, taskId, 1, 1);
                }

	        /* Connect this task to Sink */
                graph.addEdge(taskId, SINK_INDEX, minWorkersForTask, maxWorkersForTask);
            }

	    /* Read worker details */
            for (int i = 1; i < totalWorker + 1; i++) {
                str = workerReader.readLine();
                String[] workerDetail = str.split(",");
                int workerId = workerStartIndex + Integer.parseInt(workerDetail[0]);
                int minTasksForWorker = Integer.parseInt(workerDetail[1]);
                int maxTasksForWorker = Integer.parseInt(workerDetail[2]);

	        /* Connect this worker to Source */
	        /* Max and Min task is same for Question 1 */
                graph.addEdge(SOURCE_INDEX, workerId, minTasksForWorker, maxTasksForWorker);
            }

	    /* Calculate flow */
            graph.maxNetworkFlowOne(SOURCE_INDEX, SINK_INDEX);

	    /* Print solution */
            graph.printSolution(workerStartIndex, taskStartIndex, totalWorker, totalTask);

	    /* Note down end time and calculate total time */
	    long endTime = System.nanoTime();

	    System.out.println("Total time it took to run this is " + (endTime - startTime)/Math.pow(10, 9) + " seconds"); // nanoseconds to seconds
	} finally {
	    if (taskFile != null)
		taskFile.close();
	    if (workerFile != null)
		workerFile.close();
	    if (taskReader != null)
		taskReader.close();
	    if (workerReader != null)
		workerReader.close();
	}
    }
}
