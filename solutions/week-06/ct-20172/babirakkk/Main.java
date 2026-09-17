import java.util.*;
import java.io.*;

class Task {
    int startDay;
    int endDay;
    int income;
    Task(int startDay, int endDay, int income) {
        this.startDay = startDay;
        this.endDay = endDay;
        this.income = income;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int MAX_DAYS = Integer.parseInt(br.readLine()); // 일을 할 수 있는 최대 날짜
        PriorityQueue<Task> tasks = new PriorityQueue<>((Task t1, Task t2) -> {
            if (t1.endDay == t2.endDay) return Integer.compare(t2.income, t1.income);
            return Integer.compare(t1.endDay, t2.endDay);
        });

        StringTokenizer st;
        for (int i = 1; i <= MAX_DAYS; i++) {
            st = new StringTokenizer(br.readLine());
            int days = Integer.parseInt(st.nextToken());
            int income = Integer.parseInt(st.nextToken());
            tasks.add(new Task(i, i + days - 1, income));
        }

        int[] maxIncome = new int[MAX_DAYS + 1]; // i일에 얻을 수 있는 최대 수익 저장
        for (int i = 1; i <= MAX_DAYS; i++) {
            maxIncome[i] = maxIncome[i - 1];

            Task curr;
            while ((curr = tasks.peek()) != null && curr.endDay == i) {
                // 현재 날짜에 끝낼 수 있는 일이 있다면 일을 했을 때와 안 했을 때를 비교해서 큰 값을 저장
                maxIncome[i] = Math.max(maxIncome[i], maxIncome[curr.startDay - 1] + curr.income);
                tasks.poll();
            }
        }

        System.out.println(maxIncome[MAX_DAYS]);
    }
}
