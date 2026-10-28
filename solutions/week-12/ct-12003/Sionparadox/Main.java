import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        String S = br.readLine();
        ArrayDeque<Integer> bracket = new ArrayDeque<>();
        ArrayDeque<Integer> question = new ArrayDeque<>();

        boolean possible = true;
        for (int i=0; i<S.length(); i++){
            char c = S.charAt(i);
            if (c == '(') bracket.add(i);
            else if (c == '?') question.add(i);
            else if (!bracket.isEmpty()) bracket.pollLast();
            else if (!question.isEmpty()) question.pollLast();
            else {
                System.out.println("No");
                return;
            }
        }
        
        while (!bracket.isEmpty() && !question.isEmpty()){
            int pos = bracket.pollLast();
            int que = question.pollLast();
            if (pos>que){
                possible = false;
                break;
            }
        }
        
        if (!bracket.isEmpty()) possible = false;
        if (question.size() % 2 == 1) possible = false;

        System.out.println(possible ? "Yes" : "No");

    }
}

/*
괄호 스택, 물음표 스택

*/
