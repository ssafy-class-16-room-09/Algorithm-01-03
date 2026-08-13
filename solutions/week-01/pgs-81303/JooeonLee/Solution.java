import java.util.*;

class Solution {
    class Node {
        int idx;
        int prev;
        int next;
        boolean isActive;
        
        Node(int idx, int prev, int next, boolean isActive) {
            this.idx =idx;
            this.prev = prev;
            this.next = next;
            this.isActive = isActive;
        }
    }
    public String solution(int n, int k, String[] cmd) {
        ArrayList<Node> nodeList = new ArrayList<>();
        nodeList.add(new Node(0, -1, 1, true));
        for(int i=1; i<n-1; i++) {
            nodeList.add(new Node(i, i-1, i+1, true));
        }
        nodeList.add(new Node(n-1, n-2, -1, true));
        
        int pointerIdx = k;
        Stack<Node> deleteStack = new Stack<>();
        
        for(String currCmd : cmd) {
            String[] cmdArr = currCmd.split(" ");
            String opt = cmdArr[0];
            
            if(opt.equals("U") || opt.equals("D")) {
                int move = Integer.parseInt(cmdArr[1]);
                if(opt.equals("U")) {
                    int currIdx = pointerIdx;
                    for(int i=0; i<move; i++)
                        currIdx = nodeList.get(currIdx).prev;
                    pointerIdx = currIdx;
                }
                else {
                    int currIdx = pointerIdx;
                    for(int i=0; i<move; i++)
                        currIdx = nodeList.get(currIdx).next;
                    pointerIdx = currIdx;
                }
            }
            
            else if(opt.equals("C")) {
                Node currNode = nodeList.get(pointerIdx);
                
                if(currNode.prev != -1) {
                    Node prevNode = nodeList.get(currNode.prev);
                    prevNode.next = currNode.next;
                }
                if(currNode.next != -1) {
                    Node nextNode = nodeList.get(currNode.next);
                    nextNode.prev = currNode.prev;
                }
                
                currNode.isActive = false;
                deleteStack.push(currNode);
                
                if(currNode.next == -1)
                    pointerIdx = currNode.prev;
                else
                    pointerIdx = currNode.next;
            }
            
            else {
                if(deleteStack.isEmpty())
                    continue;
                Node currNode = deleteStack.pop();
                currNode.isActive = true;
                
                if(currNode.prev != -1) {
                    Node prevNode = nodeList.get(currNode.prev);
                    prevNode.next = currNode.idx;
                }
                if(currNode.next != -1) {
                    Node nextNode = nodeList.get(currNode.next);
                    nextNode.prev = currNode.idx;
                }
            }
        }
        
        StringBuilder sb = new StringBuilder();
        for(Node e : nodeList) {
            if(e.isActive)
                sb.append("O");
            else
                sb.append("X");
        }
        return sb.toString();
    }
}

// 시간초과 풀이
/**
- 모든 opt U, D, C, Z에 대해 O(1) 이 아니다.

class Solution {
    public String solution(int n, int k, String[] cmd) {
        boolean[] isActive = new boolean[n];
        for(int i=0; i<n; i++)
            isActive[i] = true;
        int pointerIdx = k;
        Stack<Integer> deleteStack = new Stack<>();
        
        for(String currCmd : cmd) {
            String[] cmdArr = currCmd.split(" ");
            String opt = cmdArr[0];
            if(opt.equals("U") || opt.equals("D")) {
                int move = Integer.parseInt(cmdArr[1]);
                int cnt = 0;
                if(opt.equals("U")) {
                    for(int i=pointerIdx-1; i>=0; i--) {
                        if(isActive[i]) {
                            cnt++;
                            if(cnt == move) {
                                pointerIdx = i;
                                break;
                            }
                        }
                    }
                }
                else {
                    for(int i=pointerIdx+1; i<n; i++) {
                        if(isActive[i]) {
                            cnt++;
                            if(cnt == move) {
                                pointerIdx = i;
                                break;
                            }
                        }
                    }
                }
            }
            
            else if(opt.equals("C")) {
                isActive[pointerIdx] = false;
                deleteStack.push(pointerIdx);
                boolean flag = false;
                int nextPointerIdx = pointerIdx;
                
                for(int i=pointerIdx+1; i<n; i++) {
                    if(isActive[i] == true) {
                        nextPointerIdx = i;
                        flag = true;
                        break;
                    }
                }
                if(!flag) {
                    for(int i=pointerIdx-1; i>=0; i--) {
                        if(isActive[i] == true) {
                            nextPointerIdx = i;
                            break;
                        }
                    }
                }
                pointerIdx = nextPointerIdx;
            }
            
            else {
                int curr = deleteStack.pop();
                isActive[curr] = true;
            }
        } 
        
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<n; i++) {
            boolean e = isActive[i];
            if(e)
                sb.append("O");
            else
                sb.append("X");
        }
        return sb.toString();
    }
}
*/

/**
n = 10^6
cmd = 2 * 10^5
sum of X <= 10^6
*/