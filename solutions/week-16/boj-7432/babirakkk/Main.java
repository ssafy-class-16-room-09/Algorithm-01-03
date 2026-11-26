import java.util.*;
import java.io.*;

class Main {
	
	static class Trie {
		TreeMap<String, Trie> children = new TreeMap<>();
	}
	
	static Trie root;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int numDirectories = Integer.parseInt(br.readLine());
		
		root = new Trie();
		for (int i = 0; i < numDirectories; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine(), "\\");
			Trie parent = root;
			while (st.hasMoreTokens()) {
				String currDirectory = st.nextToken();
				if (!parent.children.containsKey(currDirectory)) {
					parent.children.put(currDirectory, new Trie());
				}
				parent = parent.children.get(currDirectory);
			}
		}
		
		printDirectoryStructure(root, 0);		
			
		br.close();
	}
	
	private static void printDirectoryStructure(Trie curr, int directoryLevel) {
		for (Map.Entry<String, Trie> child : curr.children.entrySet()) {
			for (int space = 0; space < directoryLevel; space++) {
				System.out.print(" ");
			}
			System.out.println(child.getKey());
			if (child.getValue().children != null) {
				printDirectoryStructure(child.getValue(), directoryLevel + 1);
			}
		}
	}
}

