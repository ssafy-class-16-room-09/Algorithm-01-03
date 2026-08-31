import java.util.*;
import java.io.*;

class Solution {
	static int[] parent;
	public static void main(String args[]) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int TC = Integer.parseInt(br.readLine());

		for(int tc = 1; tc<= TC; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int N = Integer.parseInt(st.nextToken());
			int M = Integer.parseInt(st.nextToken());
			
			StringBuilder sb = new StringBuilder();
			
			parent = new int[N+1];
			for (int i=1; i<=N; i++) {
				parent[i] = i;
			}
			
			for (int i=0; i<M; i++) {
				st = new StringTokenizer(br.readLine());
				int cmd = Integer.parseInt(st.nextToken());
				int a = Integer.parseInt(st.nextToken());
				int b = Integer.parseInt(st.nextToken());
				if (cmd == 0) {
					union(a, b);
				} else {
					sb.append(find(a) == find(b) ? 1 : 0);
				}
			}
			System.out.printf("#%d %s\n",tc,sb.toString());

		}
	}
	
	private static int find(int node) {
		if (node != parent[node]) parent[node] = find(parent[node]);
		return parent[node];
	}
	
	private static void union(int u, int v) {
		int ru = find(u), rv = find(v);
		if (ru != rv) parent[rv] = ru;
	}
}