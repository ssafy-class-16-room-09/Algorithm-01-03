import java.util.*;

public class Main {

    static int N; // 점 개수
    static int K; // 색깔 개수
    static int[][] positions; // 좌표들

    static int minArea; // 최소 넓이

    static int[] colorsIdx; // 색깔 시작 인덱스
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        K = sc.nextInt();
        positions = new int[N][3]; // 0: x죄표, 1: y좌표, 2: color

        for (int i = 0; i < N; i++) {
            positions[i][0] = sc.nextInt();
            positions[i][1] = sc.nextInt();
            positions[i][2] = sc.nextInt();
        }
        // 색깔 기준 오름차순 정렬
        Arrays.sort(positions, (a, b) -> Integer.compare(a[2], b[2]));

        minArea = Integer.MAX_VALUE;

        dfs(-1, 0, 1001, 1001, -1001, -1001);
        System.out.println(minArea);
    }

    // params) index: 인덱스, select: 선택된 색깔, minX: 최소 X, minY: 최소 Y, maxX: 최대 X, maxY: 최대 Y
    private static void dfs(int index, int select, int minX, int minY, int maxX, int maxY) {
      // 아무것도 선택되지 않은 경우 제외하고 나머지는 현재까지 이전 인덱스에 있는 색깔들이 선택되었는지 확인
      if(index != -1) {
        int colors = (1 << positions[index][2]) - 1; // 확인하려는 색 번호
        if((select & colors) != colors) return; // 그 이전 인덱스에 있는 색들 모두 포함해야 함
      }
      
      int width = maxX - minX;
      int heigth = maxY - minY;
      int area = width * heigth;
      if(area >= minArea) return; // 현재 최소 넓이보다 큰 경우 패쓰

      // K개 모두 포함된 경우
      if(select == ((1 << K) - 1)) {
        minArea = Math.min(minArea, area);
        return;
      }

      for(int i=index+1; i<N; i++) {
        // 이미 선택된 색깔인 경우는 패쓰
        if((select & (1<<(positions[i][2]-1))) != 0) continue;
  
        dfs(i, select | (1<<(positions[i][2]-1)), 
              Math.min(minX, positions[i][0]), 
              Math.min(minY, positions[i][1]), 
              Math.max(maxX, positions[i][0]), 
              Math.max(maxY, positions[i][1])); // 해당 좌표 선택
      }
    }
}