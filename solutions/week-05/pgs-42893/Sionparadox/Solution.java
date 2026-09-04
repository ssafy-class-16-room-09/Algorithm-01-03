import java.util.*;

class Page {
    int link, base_score;
    double match_score;
    int index;
    ArrayList<Integer> links;
    public Page(){
        links = new ArrayList<>();
    }
    
    public double getLinkScore() {
        if (links.isEmpty()) return 0;
        return (double) base_score / links.size();
    }
}

class Solution {
    HashMap<Integer, Integer> mapper;
    
    public int solution(String word, String[] pages) {
        int L = pages.length;
        Page[] arr = new Page[L];
        mapper = new HashMap<>();
        
        for (int i=0; i<L; i++){
            arr[i] = parseDOM(pages[i], word, i);
            arr[i].match_score += arr[i].base_score;
        }
               
        for (Page p : arr){
            for (int connected: p.links){
                Integer index = mapper.get(connected);
                if (index != null) {
                    arr[index].match_score += p.getLinkScore();
                }
                
            }
        }
        
        int answer = 0;

        for (int i = 1; i < L; i++) {
            if (arr[i].match_score > arr[answer].match_score) {
                answer = i;
            }
        }

    return answer;

    }
    
    //문서를 파싱하는 메서드
    private Page parseDOM(String page, String target, int idx){
        ArrayList<String> words = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        Page ret = new Page();
        ret.index = idx;
        
        // 해당하는 링크 추출
        String[] myLink = getLink(page, "meta", "content");
        String[] links = getLink(page, "a", "href");
        
        ret.link = myLink[0].hashCode();
        mapper.put(ret.link, idx);
        
        for (String link : links){
            ret.links.add(link.hashCode());
        }
        
        // 알파벳이 아닌 문자로 잘라서 단어 생성
        for (char c: page.toCharArray()){
            if (Character.isAlphabetic(c)) sb.append(c);
            else {
                if (sb.length() == 0) continue;
                words.add(sb.toString());
                sb.setLength(0);
            }
        }
        
        // 타겟 단어인지 확인
        for (String word : words){
            if (target.equalsIgnoreCase(word)) {
                ret.base_score++;
            }
        }
        
        return ret;
    }
    
    //태그를 기반으로 해당하는 링크를 반환
    private String[] getLink(String page, String tag, String property){
        //시작태그
        int tagStart = page.indexOf("<"+tag);
        ArrayList<String> ret = new ArrayList<>();
        
        while (tagStart != -1) {
            //종료태그
            int tagEnd = page.indexOf(">", tagStart);

            if (tagEnd == -1) break;

            // 시작태그와 종료 태그 사이의 문자열
            String sub = page.substring(tagStart, tagEnd + 1);

            // 속성값의 시작 위치
            int contentStart = sub.indexOf(property+"=\"");

            if (contentStart != -1) {
                contentStart += (property+"=\"").length();

                // 속성값의 종료 위치
                int contentEnd = sub.indexOf("\"", contentStart);

                if (contentEnd != -1) {
                    // 실제 링크
                        ret.add(sub.substring(contentStart, contentEnd));
                }
            }

            // 다음 태그의 시작 위치
            tagStart = page.indexOf("<"+tag, tagEnd + 1);
        }
        
        return ret.toArray(new String[0]);
    }
}