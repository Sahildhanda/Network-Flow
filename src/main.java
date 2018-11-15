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
		int source_pos = 0;
		for(int i=1;i<total_worker+1;i++) {
			graph[source_pos][i] = worker_bounds[1][i-1];
		}
		//Connect all tasks to sink with lower bounds on edges
		int sink_pos = total_worker+total_task+1;
		for(int i=1+total_worker;i<total_task+total_worker+1;i++) {
			graph[i][sink_pos] = task_bounds[1][i-1-total_worker];
		}
		
		System.out.println("Parameters for Edmond Karp:- ");
		System.out.println("Source_Position: "+source_pos);
		System.out.println("Sink_Position: "+sink_pos);
		System.out.println("\n\nGraph(graph[][]):-\n");


		for(int i=0;i<total_nodes;i++) {
			for(int j=0;j<total_nodes;j++) {
				System.out.print(graph[i][j]);
			}
			System.out.print("\n");
		}
		
		
	}
}
