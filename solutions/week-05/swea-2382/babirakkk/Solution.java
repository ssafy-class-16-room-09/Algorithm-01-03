package algorithm;

import java.util.*;
import java.io.*;

class Group {
    int r, c, count, dir;
    int maxPrevCount; // 현재 위치에서 합쳐진 군집들 중 이동 전 미생물 수의 최댓값
    Group(int r, int c, int count, int dir) {
        this.r = r;
        this.c = c;
        this.count = count;
        this.dir = dir;
        this.maxPrevCount = count;
    }
}

public class Solution {

    static final int UP = 0;
    static final int DOWN = 1;
    static final int LEFT = 2;
    static final int RIGHT = 3;
    static final int[] dx = { -1, 1, 0, 0 };
    static final int[] dy = { 0, 0, -1, 1 };
    static final int EMPTY = Integer.MAX_VALUE;
    
    public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      final int TEST_CASES = Integer.parseInt(br.readLine());
      StringTokenizer st;
      
      for (int tc = 1; tc <= TEST_CASES; tc++) {
          st = new StringTokenizer(br.readLine());
          final int N = Integer.parseInt(st.nextToken()); // 한 변의 셀의 개수
          int time = Integer.parseInt(st.nextToken()); // 격리 시간 
          final int K = Integer.parseInt(st.nextToken()); // 미생물 군집의 개수 
          
          int[][] map = new int[N][N];
          for (int i = 0; i < N; i++) {
              Arrays.fill(map[i], EMPTY);
          }
          
          ArrayList<Group> groups = new ArrayList<>(); // 미생물 군집의 정보를 저장
          Set<Integer> activeGroups = new TreeSet<>(); // 현재 남아 있는 미생물 군집의 인덱스를 오름차순으로 저장
          for (int i = 0; i < K; i++) {
              st = new StringTokenizer(br.readLine());
              int r = Integer.parseInt(st.nextToken());
              int c = Integer.parseInt(st.nextToken());
              int count = Integer.parseInt(st.nextToken());
              int dir = Integer.parseInt(st.nextToken()) - 1; // 입력 방향 번호(1~4)를 배열 인덱스(0~3)에 맞게 변환
              groups.add(new Group(r, c, count, dir));
              activeGroups.add(i);
          }
          
          Group curr;
          int id;
          Iterator<Integer> it;
          while (time-- > 0) {
        	  it = activeGroups.iterator();
              while (it.hasNext()) {
            	  id = it.next(); // 현재 군집의 인덱스 
            	  curr = groups.get(id); // 현재 군집
            	  
                  curr.maxPrevCount = curr.count; // 새로운 시간 단위가 시작되므로 현재 군집의 이동 전 미생물 수로 초기화
                  
                  if (map[curr.r][curr.c] == id) { // 현재 군집이 이전 시간에 차지했던 위치라면 비움
                      map[curr.r][curr.c] = EMPTY;
                  }
                  
                  curr.r += dx[curr.dir];
                  curr.c += dy[curr.dir];
                  
                  if (curr.r == 0 || curr.c == 0 || curr.r == N - 1 || curr.c == N - 1) { // 약품 칸에 도착한 경우 
                      curr.count /= 2; // 절반 사라짐 
                      if (curr.count == 0) { // 남은 미생물이 없는 경우
                    	  it.remove(); // 해당 군집은 사라짐
                    	  continue;
                      }
                      curr.dir += ((curr.dir % 2 == 0) ? 1 : -1); // 방향 바꾸기: 0/2->1/3, 1/3->0/2
                  } else if (map[curr.r][curr.c] < id) { // 다른 군집과 겹치는 경우 
                      Group target = groups.get(map[curr.r][curr.c]);
                      if (curr.maxPrevCount > target.maxPrevCount) { // 합쳐지는 군집 중 이동 전 미생물 수가 더 큰 군집의 방향을 유지
                    	  target.dir = curr.dir;
                    	  target.maxPrevCount = curr.maxPrevCount;
                      }
                      target.count += curr.count;
                      it.remove();  // curr가 target에 합쳐졌으므로 남은 군집 목록에서 제거 
                  } else { // 아무와도 겹치지 않은 경우 
                      map[curr.r][curr.c] = id; // 지도에 현재 군집의 인덱스 기록 
                  }
              }
          }
          
          int totalCount = 0;
          for (int i : activeGroups) {
              totalCount += groups.get(i).count;
          }
          System.out.println("#" + tc + " " + totalCount);
          
      }
    }
}