import java.util.*;
import java.io.*;

class Main {
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int testCases = Integer.parseInt(br.readLine());
		
		for (int tc = 0; tc < testCases; tc++) {
			int n = Integer.parseInt(br.readLine());
			long zeros = 0;
			int divide = 5;
			while (n / divide != 0) {
				zeros += n / divide;
				divide *= 5;
			}
			System.out.println(zeros);
		}
	}
}