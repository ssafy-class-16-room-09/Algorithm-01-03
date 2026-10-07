import java.util.*;

class Solution {
    public int[] solution(String[] gems) {
        int totalType = new HashSet<>(Arrays.asList(gems)).size();

        Map<String, Integer> map = new HashMap<>();

        int left = 0;
        int right = 0;

        int minLength = Integer.MAX_VALUE;
        int answerLeft = 0;
        int answerRight = 0;

        while (right < gems.length) {

            map.put(gems[right], map.getOrDefault(gems[right], 0) + 1);

            while (map.size() == totalType) {

                int length = right - left + 1;

                if (length < minLength) {
                    minLength = length;
                    answerLeft = left;
                    answerRight = right;
                }

                String gem = gems[left];

                map.put(gem, map.get(gem) - 1);

                if (map.get(gem) == 0) {
                    map.remove(gem);
                }

                left++;
            }

            right++;
        }

        return new int[]{answerLeft + 1, answerRight + 1};
    }
}