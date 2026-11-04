import java.util.ArrayList;
import java.util.List;

class Solution {
    private static int size = 50;
    private static int cnt = size * size;

    public String[] solution(String[] commands) {
        int[] group = new int[cnt];
        String[] value = new String[cnt];
        List<String> answer = new ArrayList<>();

        for(int i = 0; i < cnt; i++) {
            group[i] = i;
        }

        for(String command : commands) {
            String[] parts = command.split(" ");

            switch(parts[0]) {
                case "UPDATE": {
                    if(parts.length == 4) {
                        int cell = index(parts[1], parts[2]);
                        value[group[cell]] = parts[3];
                    } 
                    else {
                        String before = parts[1];
                        String after = parts[2];

                        for(int i = 0; i < cnt; i++) {
                            if(before.equals(value[i]))
                                value[i] = after;
                        }
                    }
                    break;
                }

                case "MERGE": {
                    int cell1 = index(parts[1], parts[2]);
                    int cell2 = index(parts[3], parts[4]);

                    int group1 = group[cell1];
                    int group2 = group[cell2];

                    if(group1 == group2)
                        break;

                    if(value[group1] == null)
                        value[group1] = value[group2];

                    for(int i = 0; i < cnt; i++) {
                        if (group[i] == group2)
                            group[i] = group1;
                    }

                    value[group2] = null;
                    break;
                }

                case "UNMERGE": {
                    int cell = index(parts[1], parts[2]);
                    int targetGroup = group[cell];
                    String savedValue = value[targetGroup];

                    for(int i = 0; i < cnt; i++) {
                        if(group[i] == targetGroup) {
                            group[i] = i;
                            value[i] = null;
                        }
                    }

                    value[cell] = savedValue;
                    break;
                }

                case "PRINT": {
                    int cell = index(parts[1], parts[2]);
                    String result = value[group[cell]];

                    answer.add(result == null ? "EMPTY" : result);
                    break;
                }
            }
        }

        return answer.toArray(new String[0]);
    }

    private int index(String row, String col) {
        int r = Integer.parseInt(row);
        int c = Integer.parseInt(col);

        return (r - 1) * size + (c - 1);
    }
}