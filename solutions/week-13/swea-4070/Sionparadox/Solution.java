import java.util.*;
import java.io.*;

class Longest {
  //한 칸에 8자리 수 배열 : 최대 80자리 수
  int[] numbers;
  static int L = 100_000_000;

  public Longest(){
    numbers = new int[10];

  }

  void update(Longest x, Longest y){
    int carry = 0;
    for(int i=0; i<10; i++) {
      int n = x.numbers[i]+y.numbers[i]*2+carry;
      numbers[i] = n%L;
      carry = n/L;
    }
    
  }

  @Override
  public String toString(){
    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;
    for(int i=9; i>=0; i--){
      int number = numbers[i];
      if(isFirst && number != 0){
        isFirst = false;
        sb.append(number);
      } else if(!isFirst){
        sb.append(String.format("%08d", number));
      }
    }
    return sb.toString();
  }
}

class Solution {
    public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      int TC = Integer.parseInt(br.readLine());

      Longest[] dp = new Longest[251];
      int idx = 1;
      dp[0] = new Longest();
      dp[1] = new Longest();
      dp[0].numbers[0] = 1;
      dp[1].numbers[0] = 1;

      for (int tc=1; tc<=TC; tc++){
        int N = Integer.parseInt(br.readLine());
        if(idx >= N) {
          System.out.println("#"+tc+" "+dp[N]);
          continue;
        }
        for (int i=idx+1; i<=N; i++){
          dp[i] = new Longest();
          dp[i].update(dp[i-1], dp[i-2]);
        }
        idx = N;
        System.out.println("#"+tc+" "+dp[N]);
      }
    }
}


/*


최대 61자리수 * 2^50승
61자리수 + 15자리
76

1 : 1
2 : 3
3 : 5
4 : 

끝에 1*2 하나 붙이기 : dp[i-1]
끝에 2*2 중에 ||빼고 붙이기 : dp[i-2]*2
dp[i] = dp[i-1] + 2*dp[i-2]
*/