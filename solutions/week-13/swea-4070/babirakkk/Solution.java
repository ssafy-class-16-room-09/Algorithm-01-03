import java.util.*;
import java.io.*;

class Solution {
	
	private static String[] dp;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		init();
		int testCases = Integer.parseInt(br.readLine());
		for (int tc = 1; tc <= testCases; tc++) {
			int n = Integer.parseInt(br.readLine());
			bw.write("#" + tc + " " + dp(n) + "\n");
		}
		bw.flush();
		bw.close();
		br.close();
	}
	
	private static String dp(int n) {
		if (dp[n] != null) return dp[n];
		
		return dp[n] = calcString(dp(n - 1), n);
	}
	
	// 마이너스 처리 해야함!!!!!!!!!!!!!!!!!!!!!!!!
	private static String calcString(String num, int n) {
		StringBuilder result = new StringBuilder();
		boolean hasCarry = (n % 2 == 0) ? true : false; // 다음 자리에 더할 값이 있는지 (올림)
		boolean hasBorrow = (n % 2 == 1) ? true : false; // 다음 자리에서 빌려와야 하는지 (내림)
		for (int i = num.length() - 1; i >= 0; i--) {
			int doubleCurr = (num.charAt(i) - '0') * 2;
			
			if (hasCarry) doubleCurr += 1;
			if (hasBorrow) {
				if (doubleCurr > 0) {
					doubleCurr -= 1;
					hasBorrow = false;
				} else {
					doubleCurr = 9;
				}
			}
			
			hasCarry = (doubleCurr >= 10) ? true : false;
			result.insert(0, doubleCurr % 10);
		}
		
		if (hasCarry) result.insert(0, 1);
		return result.toString();
	}
	
	private static void init() {
		dp = new String[251];
		dp[1] = "1";
	}
}