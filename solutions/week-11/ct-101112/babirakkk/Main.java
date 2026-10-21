import java.util.*;
import java.io.*;

public class Main {

    static class Participant {
        int id, score;
        Participant(int id, int score) {
            this.id = id;
            this.score = score;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int numParticipants = Integer.parseInt(br.readLine());
        int[][] rank = new int[numParticipants][3];
        int[] totalScore = new int[numParticipants];

        StringTokenizer st;
        PriorityQueue<Participant> pq = new PriorityQueue<>(
                Comparator.comparingInt((Participant p) -> p.score).reversed());
        
        for (int contestId = 0; contestId < 3; contestId++) {
            st = new StringTokenizer(br.readLine());
            for (int participantId = 0; participantId < numParticipants; participantId++) {
                int score = Integer.parseInt(st.nextToken());
                totalScore[participantId] += score;
                pq.add(new Participant(participantId, score));
            }

            int nextRank = 0;
            int prevScore = 10000;
            int numSameScore = 1;
            while (!pq.isEmpty()) {
                Participant p = pq.poll();
                if (p.score == prevScore) {
                    rank[p.id][contestId] = nextRank;
                    numSameScore++;
                } else {
                    nextRank += numSameScore;
                    rank[p.id][contestId] = nextRank;
                    prevScore = p.score;
                    numSameScore = 1; // 같은 점수인 사람 수 초기화
                }
            }
        }

        for (int participantId = 0; participantId < numParticipants; participantId++) {
            pq.add(new Participant(participantId, totalScore[participantId]));
        }

        int[] finalRank = new int[numParticipants];
        int nextRank = 0;
        int prevScore = 0;
        int numSameScore = 1;
        while (!pq.isEmpty()) {
            Participant p = pq.poll();
            if (p.score == prevScore) {
                finalRank[p.id] = nextRank;
                numSameScore++;
            } else {
                nextRank += numSameScore;
                finalRank[p.id] = nextRank;
                prevScore = p.score;
                numSameScore = 1; // 같은 점수인 사람 수 초기화
            }
        }

        for (int contestId = 0; contestId < 3; contestId++) {
            for (int participantId = 0; participantId < numParticipants; participantId++) {
                System.out.print(rank[participantId][contestId] + " ");
            }
            System.out.println();
        }
        for (int participantId = 0; participantId < numParticipants; participantId++) {
            System.out.print(finalRank[participantId] + " ");
        }
    }
}
