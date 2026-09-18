import java.util.*;
import java.io.*;

class Node {
    final int value;
    ArrayList<Integer> children = new ArrayList<>();
    Node(int info) {
        this.value = (info == 0) ? 1 : -1;
    }
}

class Solution {
    
    int maxSheep = 0;
    
    public int solution(int[] info, int[][] edges) {
        Node[] grassland = new Node[info.length];
        for (int i = 0; i < info.length; i++) {
            grassland[i] = new Node(info[i]);
        }
        
        for (int i = 0; i < edges.length; i++) {
            int parent = edges[i][0];
            int child = edges[i][1];
            grassland[parent].children.add(child);
        }
        
        dfs(0, grassland, 1, 0, grassland[0].children);
        
        return maxSheep;
    }
    
    private void dfs(int curr, Node[] grassland, int sheep, int wolf, ArrayList<Integer> nextCandidate) {
        maxSheep = Math.max(sheep, maxSheep);
        
        for (int next : nextCandidate) {
            if (sheep + wolf + grassland[next].value > 0) {
                ArrayList<Integer> nextnextCandidate = new ArrayList<>();
                nextnextCandidate.addAll(grassland[next].children);
                for (int candidate : nextCandidate) {
                    if (candidate == next) continue;
                    nextnextCandidate.add(candidate);
                }
                if (grassland[next].value > 0) {
                    dfs(next, grassland, sheep + 1, wolf, nextnextCandidate);
                } else {
                    dfs(next, grassland, sheep, wolf - 1, nextnextCandidate);
                }
            }
        }
        
        
    }
}