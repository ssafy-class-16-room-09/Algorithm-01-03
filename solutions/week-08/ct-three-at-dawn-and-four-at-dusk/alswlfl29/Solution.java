import java.util.*;
import java.io.*;

public class Solution {
    static int[][] jobHard; // 업무 간의 상성 배열
    static boolean[] jobList; // 업무 인덱스(선택 여부 판단)
    static int n; // 일의 양(<= 20)
    static int minGap; // 아침과 저녁의 힘듦의 차이(최솟값)

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

       // 초기화
       n = Integer.parseInt(br.readLine());
       jobHard = new int[n][n];
       jobList = new boolean[n];
       minGap = Integer.MAX_VALUE;

       StringTokenizer st;
       for(int i=0; i<n; i++) {
        st = new StringTokenizer(br.readLine());
        for(int j=0; j<n; j++) {
            jobHard[i][j] = Integer.parseInt(st.nextToken());
        }
       }

       combineJob(0, 0);
       
       System.out.print(minGap);
    }

    private static void combineJob(int index, int selected) {
        // 기저조건
        if(selected == n/2) {
            int morning = 0;
            int evening = 0;

            for(int i=0; i<n; i++) {
                for(int j=i+1; j<n; j++) {
                    if(jobList[i] && jobList[j]) {
                        morning += (jobHard[i][j] + jobHard[j][i]);
                    }else if(!jobList[i] && !jobList[j]) {
                        evening += (jobHard[i][j] + jobHard[j][i]);
                    }
                }
            }
            minGap = Math.min(minGap, Math.abs(morning-evening));
            return;
        }

        for(int i=index; i<n; i++) {
            if(!jobList[i]) {
                jobList[i] = true;
                combineJob(i+1, selected+1);
                jobList[i] = false;
            }
        }
    }
}

/*
return > 아침과 저녁의 업무 강도의 차이의 최솟값 구하기

백트래킹 이용
-> 아침 작업이 n/2개가 선택이 되었을 때, 두 작업의 차이 최솟값 갱신
*/