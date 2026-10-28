import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt(); // 기록 길이(<=5,000)
        sc.nextLine();
        String S = sc.nextLine(); // 기록 문자열

        if (N % 2 != 0 || S.charAt(0) == ')' || S.charAt(N - 1) == '(') {
            System.out.println("No");
            return;
        } // N이 홀수이면 비정상, )로 시작 혹은 (로 종료이면 비정상

        int status = 0;
        int chance = 0;

        for (int i = 0; i < N; i++) {
            if (S.charAt(i) == '(') status++;
            else if (S.charAt(i) == ')') status--;
            else chance++;

            if (status < 0) {
                if (chance > 0) {
                    chance--;
                    status++;
                } else {
                    System.out.println("No");
                    return;
                }
            }
        }

        status = 0;
        chance = 0;
        for (int i=N-1; i>=0; i--) {
            if (S.charAt(i) == ')') status++;
            else if (S.charAt(i) == '(') status--;
            else chance++;

            if (status < 0) {
                if (chance > 0) {
                    chance--;
                    status++;
                } else {
                    System.out.println("No");
                    return;
                }
            }
        }

        System.out.println("Yes");
    }
}