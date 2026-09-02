import java.util.*;
import java.io.*;

class Solution
{
	static int[] parent; // 유니온-파인드 부모 배열
	public static void main(String args[]) throws Exception {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());
		
		StringBuilder sb = new StringBuilder();
		for(int test_case = 1; test_case <= T; test_case++) {
			sb.append("#").append(test_case).append(" "); // 테스트 케이스 번호
			
			StringTokenizer st = new StringTokenizer(br.readLine());
			int n = Integer.parseInt(st.nextToken()); // 원소 개수
			int m = Integer.parseInt(st.nextToken()); // 연산 개수
			
			parent = new int[n+1];
			for(int p=1; p<=n; p++) {
				parent[p] = p; // 본인 자신으로 초기화
			}
			
			for(int o=0; o<m; o++) {
				st = new StringTokenizer(br.readLine());
				int order = Integer.parseInt(st.nextToken()); // 0 혹은 1
				int a = Integer.parseInt(st.nextToken());
				int b = Integer.parseInt(st.nextToken());
				
				// a가 포함되어 있는 집합과 b가 포함되어 있는 집합 합치기
				if(order == 0) {
					union(a, b);
				} else {
					int fa = find(a);
					int fb = find(b);
					
					if(fa != fb) sb.append("0"); // 같은 집합에 속하지 않은 경우, 0
					else sb.append("1"); // 같은 집합에 속한 경우, 1
				}
			}
			sb.append("\n");
		}
		System.out.print(sb);
	}
	
	// find 메서드
	private static int find(int v) {
		if(parent[v] == v) return v;
		return parent[v] = find(parent[v]);
	}
	
	// union 메서드
	private static void union(int a, int b) {
		int fa = find(a);
		int fb = find(b);
		
		// 더 작은 수로 합치기
		if(fa <= fb) {
			parent[fb] = fa;
		} else {
			parent[fa] = fb;
		}
	}
}

/*
 * 0: a가 포함되어 있는 집합 + b가 포함되어 있는 집합 => union
 * 1: a와 b가 같은 집합에 포함되어 있는지 확인 => find
 * 
 * union-find 알고리즘 이용
 * 
 * */
