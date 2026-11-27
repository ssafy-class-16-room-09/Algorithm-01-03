import java.util.*;
import java.io.*;

class Main {
	
	public static void main(String args[]) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		int testCases = Integer.parseInt(br.readLine());
		
		for (int t = 0; t < testCases; t++) {
			int n = Integer.parseInt(br.readLine());
			
			bw.write((n + 1) / 2 + "\n");
			
			ArrayList<Integer> numList = new ArrayList<>();
			StringTokenizer st = null;
			for (int i = 0; i < n; i++) {
				if (i % 10 == 0) {
					st = new StringTokenizer(br.readLine());
				}
				int num = Integer.parseInt(st.nextToken());
				numList.add(num);
				
				if (i % 2 == 0) {
					Collections.sort(numList);
					bw.write(numList.get(i / 2) + " ");
					if (i % 20 == 18) {
						bw.write("\n");
					}
				}
			}
			bw.write("\n");
		}
		bw.flush();
		bw.close();
		br.close();
	}
}