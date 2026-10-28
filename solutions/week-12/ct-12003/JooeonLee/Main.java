import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());
        String str = br.readLine();

        boolean isValid = N % 2 == 0;

        ArrayDeque<Integer> os = new ArrayDeque<>();
        ArrayDeque<Integer> qs = new ArrayDeque<>();

        if (isValid) {
            for (int i = 0; i < N; i++) {
                char c = str.charAt(i);

                if(c == '(') {
                    os.push(i);
                }
                else if(c == '?') {
                    qs.push(i);
                }
                else {
                    if(!os.isEmpty())
                        os.pop();
                    else if(!qs.isEmpty())
                        qs.pop();
                    else {
                        isValid = false;
                        break;
                    }
                }
            }
        }

        while(isValid && !os.isEmpty()) {
            int oIdx = os.pop();

            if(qs.isEmpty() || qs.peek() < oIdx) {
                isValid = false;
                break;
            }

            qs.pop();
        }

        if(qs.size() % 2 != 0)
            isValid = false;

        System.out.println(isValid ? "Yes" : "No");
    }
}