import java.util.*;
import java.io.*;

public class Solution {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int TC = 10;
		
		for (int tc=1; tc<=TC; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int V = Integer.parseInt(st.nextToken());
			int E = Integer.parseInt(st.nextToken());
			
			ArrayList<Integer>[] edges = new ArrayList[V+1];
			for (int i=1; i<=V; i++) {
				edges[i] = new ArrayList<>();
			}
			int[] indegree = new int[V+1];
			
			st = new StringTokenizer(br.readLine());
			
			for (int i=0; i<E; i++) {
				int u = Integer.parseInt(st.nextToken());
				int v = Integer.parseInt(st.nextToken());
				indegree[v]++;
				edges[u].add(v);
			}
			
			ArrayDeque<Integer> queue = new ArrayDeque<>();
			for (int i=1; i<=V; i++) {
				if (indegree[i] == 0) queue.offer(i);
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(tc).append(" ");
			while (!queue.isEmpty()) {
				int curr = queue.poll();
				sb.append(curr).append(" ");
				for (int nxt: edges[curr]) {
					indegree[nxt]--;
					if (indegree[nxt] == 0) queue.offer(nxt);
				}
			}
			System.out.println(sb);
			
		}
	}
}