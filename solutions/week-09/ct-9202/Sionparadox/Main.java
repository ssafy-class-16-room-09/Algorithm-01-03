import java.util.*;
import java.io.*;

class Point {
    int x, y, color;
    public Point(int x, int y, int color){
        this.x = x;
        this.y = y;
        this.color = color;
    }
}

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        Point[] points = new Point[N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            points[i] = new Point(x, y, c-1);
        }
        Arrays.sort(points, (o1, o2) -> {
            if (o1.x != o2.x) return Integer.compare(o1.x, o2.x);
            return Integer.compare(o1.y, o2.y);
        });

        Point[] yPoints = points.clone();
        Arrays.sort(yPoints, (o1, o2) -> Integer.compare(o1.y, o2.y));

        int answer = Integer.MAX_VALUE;

        for (int i=0; i<N-1; i++){
            for (int j=i; j<N; j++){
                int left = points[i].x;
                int right = points[j].x;

                ArrayList<Point> innerPoints = new ArrayList<>();
                for (Point p:yPoints){
                    if (left<=p.x && p.x<=right) innerPoints.add(p);
                }

                int[] count = new int[K];
                int cnt = 0;
                int bot = 0;
                for (int top=0; top<innerPoints.size(); top++){
                    Point p = innerPoints.get(top);
                    if (count[p.color] == 0) cnt++;
                    count[p.color]++;

                    while (cnt == K){
                        int height = innerPoints.get(top).y-innerPoints.get(bot).y;
                        int width = right-left;
                        answer = Math.min(answer, height * width);

                        Point prev = innerPoints.get(bot);
                        bot++;
                        count[prev.color]--;
                        if (count[prev.color] == 0) cnt--;
                    }
                }

            }
        }
        System.out.println(answer);
    }
}

/*
점 최대 1000개
범위 -1000 ~ 1000

Point를 x에 대해 정렬

x의 모든 범위에 대해 y기준으로 투포인터
-> O(N^3)


*/
