import java.util.*;
import java.io.*;

class Student {
  int from; // 현재 방 번호
  int to; // 돌아가야 할 방 번호
  boolean finishReturn = false; // 돌아가야 할 방으로 이동했는지

  Student(int from, int to) {
    this.from = from;
    this.to = to;
  }
}

public class Solution {
    public static void main(String[] args) throws IOException {
       BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

       int testCases = Integer.parseInt(br.readLine());
       for (int t = 1; t <= testCases; t++) {
        int n = Integer.parseInt(br.readLine());

        ArrayList<Student> students = new ArrayList<>();
        StringTokenizer st;
        for (int i = 0; i < n; i++) {
          st = new StringTokenizer(br.readLine());
          int from = Integer.parseInt(st.nextToken());
          int to = Integer.parseInt(st.nextToken());
          if (from > to) { // from < to 가 되도록 조정
            int temp = from;
            from = to;
            to = temp;
          }
          students.add(new Student(from, to));
        }
        students.sort(Comparator.comparingInt((Student s) -> s.from)); // 원래 방 번호를 기준으로 정렬

        boolean hasLeftStudent = true; // 아직 돌아가지 못한 학생이 남았는지 확인
        int unitTime = 0;
        while (hasLeftStudent) {
          hasLeftStudent = false;
          int prevStudent = 0;
          for (Student currStudent : students) {
            if (!currStudent.finishReturn) {
              hasLeftStudent = true; // 한 명이라도 돌아가지 못한 학생이 있으면 true
              if ((prevStudent + 1) / 2 < (currStudent.from + 1) / 2) {
                prevStudent = currStudent.to;
                currStudent.finishReturn = true;
              }
            }
          }
          if (hasLeftStudent) {
            unitTime++;
          }
        }

        System.out.println("#" + t + " " + unitTime);
       }
       br.close();
    }
}