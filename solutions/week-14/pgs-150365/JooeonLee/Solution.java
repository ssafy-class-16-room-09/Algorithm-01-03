class Solution {
    public String solution(int n, int m, int x, int y, int r, int c, int k) {
        StringBuilder sb = new StringBuilder();
        
        char[] dir =  {'d', 'l', 'r', 'u'};
        int[] dr = {1, 0, 0, -1};
        int[] dc = {0, -1, 1, 0};
        
        int dist = Math.abs(r-x) + Math.abs(c-y);
        if(k < dist || (dist-k)%2!=0)
            return "impossible";
        
        for(int step=0; step<k; step++) {
            int remain = k-step-1;
            
            for(int i=0; i<4; i++) {
                int nx = x + dr[i];
                int ny = y + dc[i];
                
                if(nx<1 || nx>n || ny<1 || ny>m)
                    continue;
                
                int nextDist = Math.abs(r-nx) + Math.abs(c-ny);
                
                if(nextDist > remain || (remain-nextDist)%2!=0)
                    continue;
                
                sb.append(dir[i]);
                
                x = nx;
                y = ny;
                
                break;
            }  
        }
        
        return sb.toString();
    }
}