import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int TC = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
        
        for (int tc=1; tc<=TC; tc++){
        	int N = Integer.parseInt(br.readLine());
        	int[] corridors = new int[200];
        	Arrays.fill(corridors, 0);
        	
        	for (int i=0;i<N;i++) {
        		StringTokenizer st = new StringTokenizer(br.readLine());
        		int s = Integer.parseInt(st.nextToken());
        		int e = Integer.parseInt(st.nextToken());
        		if (s>e) {
        			int tmp = s;
        			s = e;
        			e = tmp;
        		}
        		for (int cell=(s-1)/2;cell<(e+1)/2;cell++) {
        			corridors[cell]++;
        		}
        	}
        	int res = 0;
        	for (int overrwapped:corridors) {
        		res = Math.max(overrwapped, res);
        	}
        	System.out.printf("#%d %d\n",tc,res);
        }
        
    }

}


/*
 * 1~2, 3~4...를 묶어서 복도의 칸 수를 기록 
 * 겹치는 최대값
 * */
 