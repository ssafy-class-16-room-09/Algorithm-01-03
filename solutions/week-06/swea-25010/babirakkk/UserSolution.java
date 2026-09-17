import java.util.*;

class WordGroup {
    int totalCount = 0;
    int bestWordCount;
    String bestWord;
    WordGroup(int totalCount, int bestWordCount, String bestWord) {
        this.totalCount = totalCount;
        this.bestWordCount = bestWordCount;
        this.bestWord = bestWord;
    }
}

class UserSolution {

    int recentSearchCount;
    ArrayList<String> wordList;
    int[] groupId;
    WordGroup[] groupInfo;
    ArrayList<HashMap<String, Integer>> groups;
    
    void init(int N) {
        recentSearchCount = N;
        wordList = new ArrayList<>();
        groupId = new int[recentSearchCount];
        groups = new ArrayList<>();
        groupInfo = new WordGroup[recentSearchCount];
        for (int i = 0; i < recentSearchCount; i++) {
        	groups.add(new HashMap<>());
        }
    }
    
    void addKeyword(String mKeyword) {
        wordList.add(mKeyword);
    }
    
    int top5Keyword(String mRet[]) {
        clearGroup();
        
        int startOffset = (wordList.size() < recentSearchCount) ? 0 : (wordList.size() - recentSearchCount);
        for (int i = 0; i < recentSearchCount; i++) {
        	if (startOffset + i >= wordList.size()) break;
        	
            if (groupId[i] == i) {
                groups.get(i).put(wordList.get(startOffset + i), 1);
                groupInfo[i] = new WordGroup(1, 1, wordList.get(startOffset + i));
                findSimilarWord(startOffset, i + 1, groupId, i);
            }
        }
        
        PriorityQueue<WordGroup> pq = new PriorityQueue<>(Comparator.comparingInt((WordGroup wg) -> wg.totalCount).reversed().thenComparing(wg -> wg.bestWord));
        for (int i = 0; i < ((wordList.size() < recentSearchCount) ? wordList.size() : recentSearchCount); i++) {
        	if (find(i) == i) {
        		pq.add(groupInfo[i]);
        	}
        }

        int popularWordCount = (pq.size() > 5) ? 5 : pq.size();
        //System.out.print(popularWordCount + " ");
        for (int i = 0; i < popularWordCount; i++) {
        	mRet[i] = pq.poll().bestWord;
        	//System.out.print(mRet[i] + " ");
        }
        //System.out.println();
        return popularWordCount;
    }
    
    
    void findSimilarWord(int startOffset, int curr, int[] groupId, int currGroupId) {
        if (curr >= recentSearchCount || startOffset + curr >= wordList.size()) return;
        
        String currWord = wordList.get(startOffset + curr);
        if (groups.get(currGroupId).containsKey(currWord)) { // 같은 단어일 경우
			if (find(curr) != find(currGroupId)) {
				groupId[curr] = currGroupId;
				int currWordCount = groups.get(currGroupId).get(currWord) + 1;
				if (currWordCount > groupInfo[currGroupId].bestWordCount  ||
						(currWordCount == groupInfo[currGroupId].bestWordCount && groupInfo[currGroupId].bestWord.compareTo(currWord) > 0)) {
					groupInfo[currGroupId].bestWordCount = currWordCount;
					groupInfo[currGroupId].bestWord = currWord;
				}
				groups.get(currGroupId).put(currWord, currWordCount);
				groupInfo[currGroupId].totalCount++;
			}
		} else {
	        for (String s : groups.get(currGroupId).keySet()) {
	    		if (isSimilar(s, currWord)) {
	            	if (groupId[curr] == curr) { // 현재 단어가 어느 그룹에도 속하지 않은 경우
	            		groupId[curr] = currGroupId;
	                    groups.get(currGroupId).put(currWord, 1); // 현재 그룹 단어장에 추가 
	                    if (groupInfo[currGroupId].bestWordCount == 1 && groupInfo[currGroupId].bestWord.compareTo(currWord) > 0) {
	                    	groupInfo[currGroupId].bestWord = currWord;
	                    }
	                    groupInfo[currGroupId].totalCount++;
	            	} else { // 이미 다른 그룹에 속해있을 경우 해당 그룹을 합침
	            		int targetGroupId = groupId[curr];
	            		union(currGroupId, targetGroupId);
	            	}
	                break;
	            }
	        }
		}
        findSimilarWord(startOffset, curr + 1, groupId, currGroupId);
    }
    
    boolean isSimilar(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        
        boolean hasDiff = false;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                if (hasDiff) return false; // 이미 다른 글자가 있는 상황에서 또 다른 글자가 있는 경우 
                hasDiff = true;
            }
        }
        return true;
    }
    
    void union(int curr, int target) {
    	int rootC = find(curr);
    	int rootT = find(target);
    	
    	if (rootC != rootT) { // C <- T
    		
    		for (String s : groups.get(rootT).keySet()) {
    			if (groups.get(rootC).containsKey(s)) {
    				int wordCount = groups.get(rootC).get(s) + groups.get(rootT).get(s);
    				groups.get(rootC).put(s, wordCount);
    				
    				if (wordCount > groupInfo[rootC].bestWordCount ||
            				(wordCount == groupInfo[rootC].bestWordCount && groupInfo[rootC].bestWord.compareTo(s) > 0)) {
            			groupInfo[rootC].bestWordCount = wordCount;
            			groupInfo[rootC].bestWord = s;
            		}
    			} else {
    				groups.get(rootC).put(s, groups.get(rootT).get(s));
    			}
    		}
    		groupInfo[rootC].totalCount += groupInfo[rootT].totalCount;
    		if (groupInfo[rootT].bestWordCount > groupInfo[rootC].bestWordCount ||
    				(groupInfo[rootT].bestWordCount == groupInfo[rootC].bestWordCount && groupInfo[rootC].bestWord.compareTo(groupInfo[rootT].bestWord) > 0)) {
    			groupInfo[rootC].bestWordCount = groupInfo[rootT].bestWordCount;
    			groupInfo[rootC].bestWord = groupInfo[rootT].bestWord;
    		}
    		groupId[rootT] = rootC;
    	}
    }
    
    int find(int a) {
    	if (groupId[a] == a) return a;  
    	return groupId[a] = find(groupId[a]);
    }
    
    void clearGroup() {
    	for (int i = 0; i < recentSearchCount; i++) {
    		groups.get(i).clear();
    		groupId[i] = i;
    	}
    	Arrays.fill(groupInfo, null);
    } 
    
}