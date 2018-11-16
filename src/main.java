import java.io.*;
public class main {

	public static void main(String[] args) throws IOException {
		File file = new File("./src/task");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		String tot_task = br.readLine();
		int total_task = Integer.parseInt(tot_task);

		File file1 = new File("./src/worker");
		BufferedReader br1 = new BufferedReader(new FileReader(file1));
		String tot_worker = br1.readLine();
		int total_worker = Integer.parseInt(tot_worker);
		int total_nodes = total_worker+total_task+2;


		int[][] graph = new int[total_nodes][total_nodes];
		int[][] worker_bounds = new int[3][total_worker];
		int[][] task_bounds = new int[3][total_task];

		int source_pos = 0;
		int sink_pos = total_worker+total_task+1;
		int worker_start = source_pos+1;
		int worker_stop = worker_start+total_worker;
		int task_start = worker_stop;
		int task_stop = task_start+total_task;
		int MAX_FLOW_DONE = 0;

		int[] task_nodes = new int[total_task];
		for(int i = 1;i<total_task+1;i++) {
			st = br.readLine();
			String[] st1 = st.split(",");
			//Task_id
			task_bounds[0][i-1] = Integer.parseInt(st1[0]);
			//Task lower bound
			task_bounds[1][i-1] = Integer.parseInt(st1[1]);
			//Task upper bound
			task_bounds[2][i-1] = Integer.parseInt(st1[2]);

			for(int j = 3;j<st1.length;j++) {
				int worker = Integer.parseInt(st1[j]);
				int task_pos = total_worker+1;
				graph[worker][task_pos+i-1] = 1;
			}
		}

		for(int i = 1;i<total_worker+1;i++) {
			st = br1.readLine();
			String[] st1 = st.split(",");
			//Worker_id
			worker_bounds[0][i-1] = Integer.parseInt(st1[0]);
			//Worker lower bound
			worker_bounds[1][i-1] = Integer.parseInt(st1[1]);
			//Worker upper bound
			worker_bounds[2][i-1] = Integer.parseInt(st1[2]);
		}

		//Connect all source to workers with lower bounds on edges
		for(int i=1;i<total_worker+1;i++) {
			graph[source_pos][i] = worker_bounds[1][i-1];
		}
		//Connect all tasks to sink with lower bounds on edges
		for(int i=1+total_worker;i<total_task+total_worker+1;i++) {
			graph[i][sink_pos] = task_bounds[1][i-1-total_worker];
		}
		
		MaxFlow m = new MaxFlow(total_nodes);
		MAX_FLOW_DONE+= m.fordFulkerson(graph, source_pos, sink_pos);


//--------------------------------------------------------------------------------------------------------------
		int lowerLimitsOfWorkers = 0;
		for(int i=0;i<worker_bounds[1].length;i++) {
			lowerLimitsOfWorkers+=worker_bounds[1][i];
		}

		
		int lowerLimitsOfTasks = 0;
		for(int i=0;i<task_bounds[1].length;i++) {
			lowerLimitsOfTasks+=task_bounds[1][i];
		}
		//System.out.println(lowerLimitsOfTasks);
		
	
		int totalTaskBoundsCompleted = 0;
		for(int i =task_start;i<task_stop;i++) {
			totalTaskBoundsCompleted+=m.rGraph[sink_pos][i];
		}

		int totalWorkerBoundsCompleted = 0;
		for(int i =worker_start;i<worker_stop;i++) {
			totalWorkerBoundsCompleted+=m.rGraph[i][source_pos];
		}




//------------------------------------------Task Not Satisfied----------------------------------------------------
		if(lowerLimitsOfWorkers == totalWorkerBoundsCompleted && lowerLimitsOfTasks > totalTaskBoundsCompleted) {
			System.out.println("Task's Lower Bounds not satisfied");
			for(int i=task_start;i<task_stop;i++) {
				if(m.rGraph[i][sink_pos]!=0) {
					int[] ass_worker = new int[total_worker];
					int k = 0;
					for(int j=worker_start;j<worker_stop;j++) {
						if(m.rGraph[j][i] == 1) {
							ass_worker[k]=j;
							k++;
						}
					}
					
					for(int l=0;l<k;l++) {
						int t_pos = ass_worker[l];
						int remaining_bound = worker_bounds[2][ass_worker[l]-1] - m.rGraph[t_pos][source_pos];
						m.rGraph[source_pos][t_pos] = remaining_bound;
					}

				}
			}
			
			MAX_FLOW_DONE+= m.fordFulkerson(m.rGraph, source_pos, sink_pos);
			int totalTaskBoundsCompleted_1 = 0;
			for(int i =task_start;i<task_stop;i++) {
				totalTaskBoundsCompleted_1+=m.rGraph[sink_pos][i];
			}

			if(lowerLimitsOfTasks == totalTaskBoundsCompleted_1) {
				System.out.println("Task's Lower Bounds satisfied.");
			}
			
		}
//--------------------------------------------------------------------------------------------------------------------
		
//----------------------------------------Worker Not Satisfied---------------------------------------------------------
		else if(lowerLimitsOfTasks == totalTaskBoundsCompleted && lowerLimitsOfWorkers > totalWorkerBoundsCompleted) {
			System.out.println("Worker's Lower Bounds not satisfied");
			
			for(int i=worker_start;i<worker_stop;i++) {
				if(m.rGraph[source_pos][i]!=0) {

					int[] ass_task = new int[total_task];
					int k = 0;
					for(int j=task_start;j<task_stop;j++) {
						if(m.rGraph[i][j] == 1) {
							ass_task[k]=(j-total_worker);
							k++;
						}
					}
					
					for(int l=0;l<k;l++) {
						int t_pos = ass_task[l]+total_worker;
						int remaining_bound = task_bounds[2][ass_task[l]-1] - m.rGraph[sink_pos][t_pos];
						m.rGraph[t_pos][sink_pos] = remaining_bound;
					}

				}
			}
			MAX_FLOW_DONE+= m.fordFulkerson(m.rGraph, source_pos, sink_pos);
			int totalWorkerBoundsCompleted_1 = 0;
			for(int i =worker_start;i<worker_stop;i++) {
				totalWorkerBoundsCompleted_1+=m.rGraph[i][source_pos];
			}
			if(lowerLimitsOfWorkers == totalWorkerBoundsCompleted_1) {
				System.out.println("Worker's Lower Bounds satisfied.");
			}
		}
//-------------------------------------------------------------------------------------------------------------

//-----------------------------Both not satisfied--------------------------------------------------------------
		else if(lowerLimitsOfTasks > totalTaskBoundsCompleted && lowerLimitsOfWorkers > totalWorkerBoundsCompleted) {
			System.out.println("Both Tasks and Workers not satisfied");
		}
		
		else {
			System.out.println("Lower Bounds Satisfied");
		}
//------------------------------------------------------------------------------------------------------------------
		
		for(int i=worker_start;i<worker_stop;i++) {
			int remaining_bounds = worker_bounds[2][i-1] - m.rGraph[i][source_pos];
			m.rGraph[source_pos][i] = remaining_bounds;
		}
		
		for(int i=task_start;i<task_stop;i++) {
			int remaining_bounds = task_bounds[2][i-total_worker-1] - m.rGraph[sink_pos][i];
			m.rGraph[i][sink_pos] = remaining_bounds;
		}
		
		
		MAX_FLOW_DONE+= m.fordFulkerson(m.rGraph, source_pos, sink_pos);
		System.out.println("\nMAX FLOW:"+MAX_FLOW_DONE);

//-----------------------------------Print Tasks assigned to Workers---------------------------------------------------------
		for(int i =worker_start;i<worker_stop;i++) {
			for(int j=task_start;j<task_stop;j++) {
				if(m.rGraph[j][i]==1) {
					System.out.println("Task: "+(j-total_worker)+" is assigned to Worker:"+i);
				}
			}
		}
		
	}
}
