import java.util.*;
import java.io.*;
import java.math.*;

public class Solution {
    public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      StringBuilder sb = new StringBuilder();
      BigInteger[] dp = new BigInteger[251];
      dp[1] = BigInteger.valueOf(1);
      dp[2] = BigInteger.valueOf(3);
      for(int i=3; i<251; i++)
        dp[i] = dp[i-2].shiftLeft(1).add(dp[i-1]);
      int tc = sc.nextInt();
      for(int t=1; t<=tc; t++) {
        int n = sc.nextInt();
        sb.append('#').append(t).append(' ').append(dp[n]).append('\n');
      }
      System.out.print(sb);
    }
}
