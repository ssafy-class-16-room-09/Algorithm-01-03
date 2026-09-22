import java.io.*;
import java.util.*;

public class Main {

    static int tasks;
    static boolean[] isTaskSelected;
    static int[][] taskTime;
    static int totalMorningWorkTime = 0;
    static int totalAfternoonWorkTime = 0;
    static ArrayList<Integer> morningTasks = new ArrayList<>();
    static ArrayList<Integer> afternoonTasks = new ArrayList<>();
    static int minDiffWorkTime = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        tasks = Integer.parseInt(br.readLine());
        isTaskSelected = new boolean[tasks];
        taskTime = new int[tasks][tasks];
        for (int i = 0; i < tasks; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < tasks; j++) {
                taskTime[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        dfs(0, -1);

        System.out.println(minDiffWorkTime);

        br.close();
    }

    private static void dfs(int taskCount, int prevTask) {
        if (prevTask == tasks - 1 && taskCount != tasks / 2) return;

        if (taskCount == tasks / 2) {
            
            afternoonTasks.clear();
            totalAfternoonWorkTime = 0;
            
            for (int i = 0; i < tasks; i++) {
                if (!isTaskSelected[i]) {
                    afternoonTasks.add(i);
                }
            }

            for (int i = 0; i < afternoonTasks.size() - 1; i++) {
                for (int j = i + 1; j < afternoonTasks.size(); j++) {
                    totalAfternoonWorkTime += taskTime[afternoonTasks.get(i)][afternoonTasks.get(j)];
                    totalAfternoonWorkTime += taskTime[afternoonTasks.get(j)][afternoonTasks.get(i)];
                }
            }

            minDiffWorkTime = Math.min(minDiffWorkTime, Math.abs(totalMorningWorkTime - totalAfternoonWorkTime));

            return;
        }

        for (int i = prevTask + 1; i < tasks; i++) {
            int addedTime = 0;
            for (int j = 0; j < morningTasks.size(); j++) {
                addedTime += taskTime[i][morningTasks.get(j)];
                addedTime += taskTime[morningTasks.get(j)][i];
            }
            totalMorningWorkTime += addedTime;
            morningTasks.add(i);
            isTaskSelected[i] = true;

            dfs(taskCount + 1, i);

            morningTasks.remove(morningTasks.size() - 1);
            isTaskSelected[i] = false;
            totalMorningWorkTime -= addedTime;
        }
    }
}