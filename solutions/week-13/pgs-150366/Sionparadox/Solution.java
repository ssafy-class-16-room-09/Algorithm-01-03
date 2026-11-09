import java.util.*;

class Solution {
    String[][] table;
    int[] parent;
    final int SIZE = 50;
    ArrayList<String> list = new ArrayList<>();
    
    public String[] solution(String[] commands) {
        String[] answer = {};
        table = new String[SIZE][SIZE];
        parent = new int[SIZE*SIZE];

        for (int r=0; r<SIZE; r++){
            for (int c=0; c<SIZE; c++){
                table[r][c] = "";
                parent[r*SIZE+c] = r*SIZE+c;
            }
        }
        
        for (String command: commands){
            String[] arr = command.split(" ");
            if(arr[0].equals("UPDATE")){
                if(arr.length == 4){
                    int r = Integer.parseInt(arr[1])-1;
                    int c = Integer.parseInt(arr[2])-1;
                    update(r, c, arr[3]);
                } else {
                    replace(arr[1], arr[2]);
                }
            } else if(arr[0].equals("MERGE")){
                int r1 = Integer.parseInt(arr[1])-1;
                int c1 = Integer.parseInt(arr[2])-1;
                int r2 = Integer.parseInt(arr[3])-1;
                int c2 = Integer.parseInt(arr[4])-1;
                merge(r1, c1, r2, c2);
            } else if (arr[0].equals("UNMERGE")){
                int r = Integer.parseInt(arr[1])-1;
                int c = Integer.parseInt(arr[2])-1;
                unmerge(r, c);
            } else {
                int r = Integer.parseInt(arr[1])-1;
                int c = Integer.parseInt(arr[2])-1;
                list.add(print(r, c));
            }
            // System.out.println(Arrays.deepToString(table));
            // System.out.println("CMD : "+command);
            // for (String[] row:table){
            //     System.out.println(Arrays.toString(row));
            // }
        }
        return list.toArray(String[]::new);
    }
    
    private void update(int r, int c, String s){
        int key = find(r*SIZE+c);
        int x = key/SIZE;
        int y = key%SIZE;
        table[x][y] = s;

    }
    
    private void replace(String from, String to){
        for (int r=0; r<SIZE; r++){
            for (int c=0; c<SIZE; c++){
                if(from.equals(table[r][c])) table[r][c] = to;
            }
        }
    }
    
    private void merge(int r1, int c1, int r2, int c2){
        int k1 = find(r1*SIZE+c1);
        int k2 = find(r2*SIZE+c2);
        
        String s1 = table[k1/SIZE][k1%SIZE];
        String s2 = table[k2/SIZE][k2%SIZE];
        
        if(s1.equals("") && !s2.equals("")){
            union(k2, k1);
            table[k1/SIZE][k1%SIZE] = s2;
        } else {
            union(k1, k2);
            table[k2/SIZE][k2%SIZE] = s1;
        }
    }
    
    private int find(int node){
        if(node != parent[node]) parent[node] = find(parent[node]);
        return parent[node];
    }
    
    private void union(int u, int v){
        int ru = find(u), rv = find(v);
        if(ru != rv) parent[rv] = ru;
    }
    
    private void unmerge(int r, int c){
        int key = find(r*SIZE+c);
        String s = table[key/SIZE][key%SIZE];
        ArrayList<Integer> candidates = new ArrayList<>();
        
        for (int i=0; i<SIZE; i++){
            for (int j=0; j<SIZE; j++){
                int k = i*SIZE+j;
                if(find(k) == key){
                    candidates.add(k);
                }
            }
        }
        for (int k:candidates){
            int x = k/SIZE;
            int y = k%SIZE;
            table[x][y] = "";
            parent[x*SIZE+y] = x*SIZE+y;
        }
        table[r][c] = s;
    }
    
    private String print(int r, int c){
        int key = find(r*SIZE+c);
        int x = key/SIZE;
        int y = key%SIZE;
        return table[x][y].equals("") ? "EMPTY" : table[x][y];
    }
}

/*
1-indexed
update 인자 3개 : 좌표에 해당하는 셀에 벨류 삽입
update 인자 2개 : 벨류 하나를 다른 하나로 치환
merge : 논리적 병합(같은 위치를 가리킴)
unmerge : 병합 해제 및 r, c만 값 가짐
print : 출력

명령 1000번
2500 * 1000 = 2,500,000
*/