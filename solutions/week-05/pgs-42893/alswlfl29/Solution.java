import java.util.*;

class WebSite {
    int index; // 웹 페이지의 index
    int basicScore; // 기본 점수
    double linkScore; // 링크 점수
    List<String> externalLink; // 외부 링크
    
    WebSite(int index) {
        this.index = index;
        this.basicScore = 0;
        externalLink = new ArrayList<>();
    }
}

class Solution {
    static String Word;
    
    static boolean HTML; // HTML 태그 안에 있는지
    static boolean HEAD; // HEAD 태그 안에 있는지
    static boolean BODY; // BODY 태그 안에 있는지
    static final String START = "https://";
    
    public int solution(String word, String[] pages) {
        int size = pages.length; // 웹 사이트 개수
        
        Word = word.toUpperCase(); // word 대문자로 만들기
        HTML = false;
        HEAD = false;
        BODY = false;

        WebSite[] websites = new WebSite[size]; // 각 웹페이지 정보를 저장하기 위해 배열 선언
        for(int i=0; i<size; i++) websites[i] = new WebSite(i); // 객체 생성
        
        Map<String, Integer> url = new HashMap<>(); // url과 그 url에 해당하는 웹페잊 인덱스 연결

        for(int p=0; p<size; p++) {
            String[] str = pages[p].split("\n"); // 줄바꿈 단위로 배열 생성
            int count = 0; // 단어와 일치하는 총 개수
            for(String s : str) {
                if(s.startsWith("<html")) HTML = true; // HTML 태그 안에 들어온 경우
                else if(s.startsWith("</html>")) HTML = false; // HTML 태그를 빠져나온 경우
                else if(s.startsWith("<head>")) HEAD = true; // HEAD 태그 안에 들어온 경우
                else if(s.startsWith("</head>")) HEAD = false; // HEAD 태그를 빠져나온 경우
                else if(s.startsWith("<body>")) BODY = true; // BODY 태그 안에 들어온 경우
                else if(s.startsWith("</body>")) BODY = false; // BODY 태그를 빠져나온 경우
                
                // HEAD 태그 안에 있는 META 태그 안에 있는 URL인 경우, 현재 웹 페이지의 URL임
                if(HEAD && s.contains("<meta ") && s.contains("content=\"https://")) {
                    StringBuilder sb = new StringBuilder();
                    int idx = s.indexOf(START) + START.length(); // https://를 제외한 순수 URL 뽑기 위한 시작 인덱스 찾기
                    // URL이 끝나는 위치까지 StringBuilder에 넣기
                    while(s.charAt(idx) != '"') {
                        sb.append(s.charAt(idx));
                        idx++;
                    }
                    url.put(sb.toString(), p);
                }
                
                // BODY 태그 안에 있는 A 태그 안에 있는 URL인 경우, 외부 링크 URL임
                if(BODY && s.contains("<a ") && s.contains("href=\"https://")) {
                    String[] ss = s.split(" "); // <a> 태그가 한 라인에 여러 개 존재할 수 있으니까~!
                    for(String st : ss) {
                        if(!st.contains("href=\"https://")) continue; // href가 없는 경우는 패스
                        StringBuilder sb = new StringBuilder();
                        
                        int idx = st.indexOf("href=\"") + START.length()+6; // 순수 URL 뽑기 위한 시작 인덱스 찾기
                        while(idx < st.length()) {
                            if(st.charAt(idx) != '"') sb.append(st.charAt(idx));  // URL이 끝나는 위치까지 StringBuilder에 넣기
                            else {
                                websites[p].externalLink.add(sb.toString());
                                break;
                            }
                            idx++;
                        }
                    }
                }
                
                count += findWord(s.toUpperCase().toCharArray()); // 기본점수 구하기
            }
            websites[p].basicScore = count;
        }

        for(WebSite ws : websites) {
            int externalLinkCnt = ws.externalLink.size(); // 외부링크 수
            for(String ex : ws.externalLink) {
                if(!url.containsKey(ex)) continue; // 주어진 웹페이지 외의 외부링크인 경우
                WebSite externalWebSite = websites[url.get(ex)]; // 외부링크에 해당하는 웹페이지의 인덱스 찾기
                externalWebSite.linkScore += ((double) ws.basicScore / externalLinkCnt); // 점수 계산: 기본점수/외부링크 수
            }
        }
        
        double max = 0.0;
        int findIndex = 0;
        // 매칭점수가 큰 웹페이지의 인덱스 찾기
        for(WebSite ws : websites) {
            double total = ws.basicScore + ws.linkScore;
            if(max < total) {
                max = total;
                findIndex = ws.index;
            }
        }
        
        
        return findIndex;
    }
    
    private int findWord(char[] str) {
        int cnt = 0;

        StringBuilder sb = new StringBuilder();
        
        for(int idx=0; idx<str.length; idx++) {
            // 알파벳인 경우 StringBuilder에 넣기
            if(str[idx] >= 'A' && str[idx] <= 'Z') {
                sb.append(str[idx]);
            } else {
                if(sb.toString().equals(Word)) { // 단어랑 일치한 경우, 카운트 증가
                    cnt++;
                }
                sb = new StringBuilder();
            }
        }
        // StringBuilder에 문자열이 남아있는데, 단어랑 일치하는 경우 카운트 증가(마지막에 등장하는 경우를 위해)
        if(sb.toString().equals(Word)) {
            cnt++;
        }
        return cnt;
    }
}