import java.util.*;
import java.io.*;

public class Main {
    static int N, K;
    static int[] positions;
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        positions = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            positions[i] = Integer.parseInt(st.nextToken());
        }
        
        int left = 1, right = positions[N-1]-positions[0]+1;
        int answer = -1;
        while (left<=right){
            int mid = (left+right)/2;
            
            if (check(mid)){
                right = mid-1;
                answer = mid;
            } else {
                left = mid+1;
            }
        }
        System.out.println(answer);
    }

    private static boolean check(int len){
        int cnt = 1;
        int start = positions[0];
        for (int i=1; i<N; i++){
            if (positions[i]-start >= len){
                cnt++;
                start = positions[i];
            }
            if(cnt>K) return false;
        }
        return true;
    }
}

/*
이분탐색
개미집이랑 똑같은거 아인교
check : positions 순회 = O(N)
10만 * log10만 = 200만
*/
