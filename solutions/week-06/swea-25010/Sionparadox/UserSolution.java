import java.util.*;

class UserSolution {
	int N;
	ArrayDeque<String> deque;
	HashMap<String, Integer> word_cnt;
	String[] words;
	int[] parent;

	void init(int N) {
		this.N = N;
		deque = new ArrayDeque<>();
	}

	void addKeyword(String mKeyword) {
		deque.offerLast(mKeyword);
		if (deque.size()>N){
			deque.pollFirst();
		}
	}

	int top5Keyword(String mRet[]) {
		int size = 0;
		word_cnt = new HashMap<>();
		words = new String[N];

		for (String word : deque) {
			if (word_cnt.get(word) == null) {
				word_cnt.put(word, 1);
				words[size++] = word;
			} else {
				word_cnt.put(word, word_cnt.get(word)+1);
			}
		}

		parent = new int[size];
		HashMap<String, ArrayList<Integer>> patternMap = new HashMap<>();
		for (int i = 0; i < size; i++) {
			parent[i] = i;

			char[] arr = words[i].toCharArray();
			for (int j = 0; j < arr.length; j++) {
				char tmp = arr[j];
				arr[j] = '*';
				String pattern = new String(arr);
				if (!patternMap.containsKey(pattern)) patternMap.put(pattern, new ArrayList<>());
				patternMap.get(pattern).add(i);
				arr[j] = tmp;
			}
		}

		for (ArrayList<Integer> group : patternMap.values()) {
			for (int i = 1; i < group.size(); i++) {
				union(group.get(0), group.get(i));
			}
		}

		HashMap<Integer, Integer> baseCnt = new HashMap<>();
		for (int i = 0; i < size; i++) {
			int p = find(i);
			baseCnt.put(p, baseCnt.getOrDefault(p, 0) + word_cnt.get(words[i]));
		}

		ArrayList<Integer> base = new ArrayList<>(baseCnt.keySet());
		base.sort((o1, o2) -> {
			int s1 = baseCnt.get(o1);
			int s2 = baseCnt.get(o2);
			if (s1 != s2) return Integer.compare(s2, s1);
			return words[o1].compareTo(words[o2]);
		});

		int ret = Math.min(5, base.size());
		for (int i = 0; i < ret; i++) {
			mRet[i] = words[base.get(i)];
		}
		return ret;
	}

	private int find(int node) {
		if (node != parent[node]) parent[node] = find(parent[node]);
		return parent[node];
	}

	private void union(int u, int v) {
		int ru = find(u), rv = find(v);
		if (ru == rv) return;
		if (better(words[ru], words[rv])) {
			parent[rv] = ru;
		} else {
			parent[ru] = rv;
		}
	}

	private boolean better(String s1, String s2) {
		int c1 = word_cnt.get(s1), c2 = word_cnt.get(s2);
		if (c1 != c2) return c1 > c2;
		return s1.compareTo(s2) < 0;
	}
}

/*
deque로 N개 관리

중복 단어 없는 단어장 생성 및 개수 카운트
해당 단어장의 단어들을 길이만큼 돌며 한자리씩 *로 바꾼 패턴 생성
이후 해당 패턴에 본인 삽입
같은 패턴에 있는 단어 union
조건에 따라 최대 5개 추출

100번 * 단어 500개 * 단어 길이(10)
50만
*/

