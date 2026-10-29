import java.util.Scanner;

public class Main {

    private static char ENTRY = '(';
    private static char EXIT = ')';
    private static char UNKNOWN = '?';

    static int recordCount;
    static String record;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        recordCount = sc.nextInt();
        sc.nextLine();
        record = sc.nextLine();

        System.out.println(isNormalRecord() ? "Yes" : "No");
    }

    private static boolean isNormalRecord() {
        if (recordCount % 2 == 1) return false; // 기록이 홀수이면 정상적인 기록일 수 없음

        // 앞에서부터 EXIT가 ENTRY보다 많은지 검사
        int personCount = 0;
        int unknownCount = 0;
        for (int i = 0; i < record.length(); i++) {
            int curr = record.charAt(i);
            if (curr == ENTRY) {
                personCount++; 
            } else if (curr == EXIT) {
                if (personCount > 0) {
                    personCount--;
                } else {
                    if (unknownCount == 0) return false;
                    unknownCount--;
                }
            } else if (curr == UNKNOWN) {
                unknownCount++;
            }
            if (personCount + unknownCount < 0) return false;
        }
        
        if (!((unknownCount -= personCount) >= 0 && unknownCount % 2 == 0)) return false;
        

        // 뒤에서부터 ENTRY가 EXIT보다 많은지 검사
        personCount = 0;
        unknownCount = 0;
        for (int i = record.length() - 1; i >= 0; i--) {
            int curr = record.charAt(i);
            if (curr == ENTRY) {
                if (personCount < 0) {
                    personCount++;
                } else {
                    if (unknownCount == 0) return false;
                    unknownCount--;
                }
            } else if (curr == EXIT) {
                personCount--;
            } else if (curr == UNKNOWN) {
                unknownCount++;
            }
            if (personCount > 0) return false;
        }
        return ((unknownCount += personCount) >= 0 && unknownCount % 2 == 0); // 남은 ENTRY 인원보다 UNKNOWN이 더 많고, 처리 후 남은 UNKNOWN이 짝수이면 정상 기록
    }
}
