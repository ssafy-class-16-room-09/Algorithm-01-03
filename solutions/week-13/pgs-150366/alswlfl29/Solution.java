import java.util.*;

class Solution {
    
    final int N = 50*50+50;
    String[] tables;
    int[] parent;
    
    public List<String> solution(String[] commands) {
        tables = new String[N]; // 배열 초기화
        parent = new int[N];
        for(int i=0; i<N; i++) parent[i] = i;
        
        List<String> answer = new ArrayList<>();
        for(String command : commands) {
            String[] comm = command.split(" ");
            // UPDATE r c value: r, c 셀 위치에 value 넣기
            if(comm[0].equals("UPDATE") && comm.length == 4) {
                int r = Integer.parseInt(comm[1])-1;
                int c = Integer.parseInt(comm[2])-1;
                int pos = find(getPos(r, c));
                if(tables[pos] == null) tables[pos] = comm[3];
                else changeValue(pos, comm[3]);
            }
            // UPDATE value1 value2: value1 값을 value2로 변경
            else if(comm[0].equals("UPDATE")) {
                findValue(comm[1], comm[2]);
            }
            // MERGE r1 c1 r2 c2: r1, c1, r2, c2 병합
            else if(comm[0].equals("MERGE")) {
                int r1 = Integer.parseInt(comm[1])-1;
                int c1 = Integer.parseInt(comm[2])-1;
                int r2 = Integer.parseInt(comm[3])-1;
                int c2 = Integer.parseInt(comm[4])-1;
                merge(getPos(r1, c1), getPos(r2, c2));
            }
            // UNMERGE r c: r c 위치의 셀 병합 해제
            else if(comm[0].equals("UNMERGE")) {
                int r = Integer.parseInt(comm[1])-1;
                int c = Integer.parseInt(comm[2])-1;
                unmerge(getPos(r, c));
            } else if(comm[0].equals("PRINT")) {
                int r = Integer.parseInt(comm[1])-1;
                int c = Integer.parseInt(comm[2])-1;
                int p = find(getPos(r, c));
                if(tables[p] != null) answer.add(tables[p]);
                else answer.add("EMPTY");
            }
        }

        return answer;
    }
    
    private void changeValue(int pos, String content) {
        int p = find(pos);
        tables[p] = content;
    }
    
    private void findValue(String value1, String value2) {
        for(int i=0; i<N; i++) {
            if(tables[i] == null || !tables[i].equals(value1)) continue;
            tables[i] = value2;
        }
    }
    
    private void merge(int pos1, int pos2) {
        String content1 = tables[find(pos1)];
        String content2 = tables[find(pos2)];
        if(union(pos1, pos2)) {
            int find1 = find(pos1);
            int find2 = find(pos2);
            if(content1 == null && content2 != null) {
                tables[find1] = content2;
            }
        }
    }
    
    private void unmerge(int pos) {
        int p = find(pos);
        String content = tables[p];
        List<Integer> children = new ArrayList<>();
        for(int i=0; i<N; i++) {
            if(find(i) == p) {
                children.add(i);
            }
        }
        for(int child : children) {
            parent[child] = child;
            tables[child] = null;
        }
        tables[pos] = content;
    }
    
    private int find(int v) {
        if(parent[v] == v) return v;
        else return parent[v] = find(parent[v]);
    }
    
    private boolean union(int a, int b) {
        int fa = find(a);
        int fb = find(b);
        
        if(fa != fb) {
            parent[fb] = fa;
            return true;
        }
        return false;
    }

    
    private int getPos(int r, int c) {
        return r*50+c;
    }
}