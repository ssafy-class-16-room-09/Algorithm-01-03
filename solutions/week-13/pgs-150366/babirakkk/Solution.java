import java.util.*;

class Cell {
    int r, c;
    String value;
    ArrayList<Cell> merged = new ArrayList<>();
    Cell(int r, int c) {
        this.r = r;
        this.c = c;
    }
}

class Solution {
    
    Cell[][] table = new Cell[51][51];
    Cell[][] parent = new Cell[51][51];
    
    public String[] solution(String[] commands) {
        init();
        
        ArrayList<String> answer = new ArrayList<>();
        for (String command : commands) {
            StringTokenizer st = new StringTokenizer(command);
            String cmd = st.nextToken();
            
            switch (cmd) {
                case "UPDATE" -> {
                    if (st.countTokens() == 3) {
                        int r = Integer.parseInt(st.nextToken());
                        int c = Integer.parseInt(st.nextToken());
                        String value = st.nextToken();
                        update(r, c, value);
                    } else {
                        String value1 = st.nextToken();
                        String value2 = st.nextToken();
                        update(value1, value2);
                    }
                }
                case "MERGE" -> {
                    int r1 = Integer.parseInt(st.nextToken());
                    int c1 = Integer.parseInt(st.nextToken());
                    int r2 = Integer.parseInt(st.nextToken());
                    int c2 = Integer.parseInt(st.nextToken());
                    
                    merge(r1, c1, r2, c2);
                }
                case "UNMERGE" -> {
                    int r = Integer.parseInt(st.nextToken());
                    int c = Integer.parseInt(st.nextToken());
                    Cell p = find(table[r][c]);
                    if (p != null) table[r][c].value = unmerge(p.r, p.c);
                }
                case "PRINT" -> {
                    int r = Integer.parseInt(st.nextToken());
                    int c = Integer.parseInt(st.nextToken());
                    Cell p = find(table[r][c]);
                    answer.add(print(p.r, p.c));
                }
            }
        }
        return answer.toArray(new String[0]);
    }
    
    private void update(int r, int c, String value) {
        find(table[r][c]).value = value;
    }
    
    private void update(String target, String change) {
        for (int i = 1; i <= 50; i++) {
            for (int j = 1; j <= 50; j++) {
                Cell curr = table[i][j];
                if (curr.value == null) continue;
                if (curr.value.equals(target)) {
                    curr.value = change;
                }
            }
        }
    }
    
    private void merge(int r1, int c1, int r2, int c2) {
        Cell cell1 = table[r1][c1];
        Cell cell2 = table[r2][c2];
        
        if (find(cell1) == find(cell2)) return; // 같은 셀일 경우 무시
        
        union(cell1, cell2);
    }
    
    private String unmerge(int r, int c) {
        for (Cell cell : table[r][c].merged) {
            cell.value = null;
            parent[cell.r][cell.c] = table[cell.r][cell.c];
        }
        
        String savedValue = table[r][c].value; // value가 있을 경우 (r, c)에 저장하기 위해 리턴
        table[r][c].value = null;
        table[r][c].merged.clear();
        return savedValue;
    }
    
    private String print(int r, int c) {
        if (table[r][c].value == null) return "EMPTY";
        return table[r][c].value;
    }
    
    private void union(Cell c1, Cell c2) {
        Cell rootC1 = find(c1);
        Cell rootC2 = find(c2);
        if (rootC1 == rootC2) return;
        
        String value = null;
        if (rootC1.value != null) value = rootC1.value;
        else if (rootC2.value != null) value = rootC2.value;
        
        parent[rootC2.r][rootC2.c] = rootC1;
        table[rootC1.r][rootC1.c].value = value;
        table[rootC2.r][rootC2.c].value = null;
        
        rootC1.merged.addAll(rootC2.merged);
        rootC1.merged.add(rootC2);
        rootC2.merged.clear();
    }
    
    private Cell find(Cell c) {
        if (parent[c.r][c.c] == table[c.r][c.c]) return c;
        return parent[c.r][c.c] = find(parent[c.r][c.c]);
    }
    
    private void init() {
        for (int i = 1; i <= 50; i++) {
            for (int j = 1; j <= 50; j++) {
                table[i][j] = new Cell(i, j);
                parent[i][j] = table[i][j]; // 초기에는 자기 자신을 부모로 설정
            }
        }
    }
}