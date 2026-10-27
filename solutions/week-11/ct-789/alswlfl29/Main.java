import java.util.*;
import java.io.*;

public class Main {

    static int N; // 퍼즐 판 크기 (<=2,000)
    static int[] shape = new int[]{1, 2, 3}; // 오목, 직선, 볼록
    static Map<Integer, Integer> puzzles; // 퍼즐 정보
    static Set<Integer> holesPos; // 구멍 위치
    static Map<Integer, Long> holes; // 구멍 정보
    static Map<Integer, Long> spares; // 스페어 정보

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine()); // 명령 수(<= 100,000)

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = null;
        int line = 1;
        for(int q=0; q<Q; q++) {
            st = new StringTokenizer(br.readLine());
            int order = Integer.parseInt(st.nextToken()); // 명령
            if(order == 100) { // 퍼즐 판 준비
                N = Integer.parseInt(st.nextToken());

                puzzles = new HashMap<>();
                holesPos = new HashSet<>();
                holes = new HashMap<>();
                spares = new HashMap<>();
            } else if(order == 200) { // 퍼즐 배치
                int y = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());
                int u = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                int d = Integer.parseInt(st.nextToken());
                int l = Integer.parseInt(st.nextToken());

                setPuzzle(y-1, x-1, shape[u], shape[r], shape[d], shape[l]);
            } else if(order == 300) { // 퍼즐 제거
                int y = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());

                removePuzzle(y-1, x-1);
            } else if(order == 400) { // 스페어 퍼즐 추가
                int u = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                int d = Integer.parseInt(st.nextToken());
                int l = Integer.parseInt(st.nextToken());

                addSparePuzzle(shape[u], shape[r], shape[d], shape[l]);
            } else if(order == 500) { // 퍼즐 맞추기
                long count = checkPuzzle();
                sb.append(count).append("\n");
            }
        }
        System.out.print(sb);
        br.close();
    }

    private static void setPuzzle(int y, int x, int u, int r, int d, int l) { // 퍼즐 배치
        int p = puzzleInfo(u, r, d, l);
        puzzles.put(y*N+x, p); // 새로운 퍼즐 추가

        if(holesPos.contains(y*N+x)) { // 추가한 퍼즐이 원래 구멍인 경우, 구멍에서 제외
          holesPos.remove(y*N+x);
          holes.put(p, holes.getOrDefault(p, 0L)-1);
          if(holes.get(p) == 0) holes.remove(p);
        }

        if(puzzles.size() == 1) return;
        findHoles(y, x, true); // 현재 추가한 퍼즐로 구멍 생성
    }

    private static void removePuzzle(int y, int x) { // 퍼즐 제거
        findHoles(y, x, false); // 제거할 퍼즐로 인해 없어지는 구멍 찾기
        
        puzzles.remove(y*N+x); // 퍼즐 제거

        int status = isBlock(y, x); // 제거한 퍼즐이 구멍이 되는지 확인
        if(status != 0) {
          holesPos.add(y*N+x);
          holes.put(status, holes.getOrDefault(status, 0L)+1);
        }
    }

    private static void addSparePuzzle(int u, int r, int d, int l) { // 스페어 퍼즐 추가
        int s = puzzleInfo(u, r, d, l);
        Set<Integer> sparePuzzle = new HashSet<>(); // 나올 수 있는 스페어 모양
        for(int i=0; i<4; i++) {
          sparePuzzle.add(s);
          s = s%10*1000 + s/10;
        }
        for(int puzzle : sparePuzzle) {
          spares.put(puzzle, spares.getOrDefault(puzzle, 0L) + 1); // 스페어 퍼즐 추가
        }
    }

    private static long checkPuzzle() { // 퍼즐 맞추기
        long count = 0; // 스페어가 들어갈 수 있는 개수
        for(int hole : holes.keySet()) {
          if(spares.containsKey(hole)) { // 구멍에 들어갈 스페어 존재하는지
              count += spares.get(hole) * holes.get(hole);
          }
        }
        return count;
    }

    private static void findHoles(int y, int x, boolean isAdd) { // 현재 위치 기준 구멍이 존재하는지 확인
        
        if(y > 0 && !puzzles.containsKey((y-1)*N+x)) { // 위쪽 탐색
            proceedHoles(y-1, x, isAdd);
        }
        if(x > 0 && !puzzles.containsKey(y*N+(x-1))) { // 왼쪽 탐색
            proceedHoles(y, x-1, isAdd);
        }
        if(y < N-1 && !puzzles.containsKey((y+1)*N+x)) { // 아래쪽 탐색
            proceedHoles(y+1, x, isAdd);
        }
        if(x < N-1 && !puzzles.containsKey(y*N+(x+1))) { // 오른쪽 탐색
            proceedHoles(y, x+1, isAdd);
        }
    }

    private static void proceedHoles(int y, int x, boolean isAdd) {
        int status = isBlock(y, x);
        if(status != 0) {
            if(isAdd) {
              holesPos.add(y*N+x);
              holes.put(status, holes.getOrDefault(status, 0L)+1);
            } else {
              holes.put(status, holes.get(status)-1);
              holesPos.remove(y*N+x);
              if(holes.get(status)==0) holes.remove(status);
            }
        }
    }

    private static int isBlock(int y, int x) { // 현재 위치 기준 사방이 막혀있는지 확인
        int u=-1, r=-1, d=-1, l=-1;
        // 위쪽이 벽이거나 퍼즐이 존재하는지 확인
        if(y==0) u = shape[1];
        else if(puzzles.containsKey((y-1)*N+x)) u = 4 - (puzzles.get((y-1)*N+x)%100/10); // 2
        // 왼쪽이 벽이거나 퍼즐이 존재하는지 확인
        if(x==0) l = shape[1];
        else if(puzzles.containsKey(y*N+(x-1))) l = 4 - (puzzles.get(y*N+(x-1))%1000/100); // 1
        // 아래쪽이 벽이거나 퍼즐이 존재하는지 확인
        if(y==N-1) d = shape[1];
        else if(puzzles.containsKey((y+1)*N+x)) d = 4 - (puzzles.get((y+1)*N+x)/1000); // 0
        // 오른쪽이 벽이거나 퍼즐이 존재하는지 확인
        if(x==N-1) r = shape[1];
        else if(puzzles.containsKey(y*N+(x+1))) r = 4 - (puzzles.get(y*N+(x+1))%10); // 3

        if(u == -1 || r == -1 || d == -1 || l == -1) return 0; // 구멍이 아닌 경우

        return puzzleInfo(u, r, d, l);
    }

    private static int puzzleInfo(int u, int r, int d, int l) { // 퍼즐 모양 정수로 변환
        return u*1000+r*100+d*10+l;
    }
}

/*
(1, 1) ~ (N, N)
위 > 오른쪽 > 아래 > 왼쪽
오목: 0, 직선: 1, 볼록: 2
- 퍼즐 판 테두리에 닿은 변 => 직선
- 이웃 칸과 맞닿은 변은 퍼즐의 변과 맞물려야 함(오목-볼록, 직선-직선)
- 시계방향으로 90도 회전 가능(왼->위, 위->오, 오->아, 아->왼)

구멍: 네 변이 모두 퍼즐 판의 테두리 혹은 퍼즐이 놓인 칸
스페어 퍼즐 -> 0~3까지 돌려서 구멍에 들어가면 넣을 수 있음
- 스페어 퍼즐은 사라지지 않음
- 점수: 모든 구멍에 대해 그 구멍에 넣을 수 있는 스페어 퍼즐의 개수를 더한 값

1. 퍼즐 판 준비(N)

2. 200 퍼즐 배치(y, x) -> y x u r d l
- 맞물림 규칙 항상 만족
- 스페어 퍼즐과 상관 x
-> 현재 위치에 퍼즐 모양 저장

3. 300 퍼즐 제거(y, x)
- 스페어 퍼즐되는거 아님
-> 해당 위치에 퍼즐 제거

4. 400 스페어 퍼즐 추가(모양) -> u r d l
- 주어진 모양의 스페어 퍼즐 하나 새로 추가
-> 스페어 추가 및 스페어 당 가능 위치 출력

5. 500 퍼즐 맞추기
현재 퍼즐 판의 점수 구해 출력

0 1 2
3 4 5
6 7 8
puzzle = {}
puzzle = {0: 1 2 2 1, 2: 1 1 2 0, 4: 0 1 1 1}
hole = {1220, }
- 위쪽 탐색: (r-1, c-1), (r-1, c+1), (r-2, c) -> 벽일 수도 있음
- 왼쪽 탐색: (r-1, c-1), (r+1, c-1), (r, c-2)
- 아래쪽 탐색: (r+1, c-1), (r+1, c+1), (r+2, c)
- 오른쪽 탐색: (r-1, c+1), (r+1, c+1), (r, c+2)

spare = {1: (1220, 0122, 2012, 2201) => 1, (0122, 2012, 2201, 1220) => 1, (1111) => 0}
500 -> 2
puzzle = {0: 1 2 2 1, 2: 1 1 2 0, 4: 0 1 1 1, 6: 2 1 1 1}
hole = {1220, 0101}
spare = {1: (1220, 0122, 2012, 2201) => 1, (0122, 2012, 2201, 1220) => 1, (1111) => 0, (1010, 0101, 1010, 0101) => 1}
500 -> 3
300 2 2 -> puzzle = {0: 1 2 2 1, 2: 1 1 2 0, 6: 2 1 1 1}
hole = {}
500 -> 0
puzzle = {0: 1 2 2 1, 2: 1 1 2 0, 6: 2 1 1 1, 4: 1 1 1 1}
hole = {0101, 1210}
spare = {1: (1220, 0122, 2012, 2201) => 0, (0122, 2012, 2201, 1220) => 0, (1111) => 0, (1010, 0101, 1010, 0101) => 1}

퍼즐 추가 삭제할 때마다 hole 위치 확인 -> 추가 혹은 삭제된 퍼즐 위치 대상으로 위/아/왼/오 방면으로 구멍이 존재하는지
-> 추가의 경우 > hole에 추가
-> 삭제의 경우 > hole에서 삭제

- 스페어 추가(이때 스페어 별 4방향 모두 탐색)
- hole에 있는 것 중 스페어로 채울 수 있는 개수 확인
*/
