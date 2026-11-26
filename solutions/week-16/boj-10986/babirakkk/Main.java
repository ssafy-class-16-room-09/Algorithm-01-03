import java.util.*;
import java.io.*;

class Main {
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int n = Integer.parseInt(st.nextToken());
		int m = Integer.parseInt(st.nextToken());

		st = new StringTokenizer(br.readLine());
		
		long count = 0;
		long straightZeros = 0;
		int[] numMods = new int[m];
		int mod = 0;
		for (int i = 0; i < n; i++) {
			mod = (mod + Integer.parseInt(st.nextToken())) % m;
			
			if (mod != 0) {
				count += numMods[mod];
			} else {
				count += numMods[mod] - straightZeros;
			}
			numMods[mod]++;
			

			if (mod == 0) {
				straightZeros++;
			} else {
				count += straightZeros * (straightZeros + 1) / 2;
				straightZeros = 0;
			}
		}
		
		count += straightZeros * (straightZeros + 1) / 2;
		
		System.out.println(count);
		
		br.close();
	}	
}