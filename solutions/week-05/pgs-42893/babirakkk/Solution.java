import java.util.*;

class WebPage {
    int index;
    int baseScore;
    double linkScore = 0.0;
    double matchingScore;
    ArrayList<String> incomingLinks = new ArrayList<>();
    ArrayList<String> outgoingLinks = new ArrayList<>();
    WebPage(int index) {
        this.index = index;
    }
}

class Solution {
    
    final String START_HEAD = "<head>";
    final String END_HEAD = "</head>";
    final String START_BODY = "<body>";
    final String END_BODY = "</body>";
    final String META = "<meta";
    final String PROPERTY = "property=\"og:url\"";
    final String START_ANCHOR = "<a";
    final String CONTENT = "content=\"https://";
    final int URL_START_OFFSET = 9;
    final String HREF = "href=\"";
    final int HREF_START_OFFSET = 6;
    final int HREF_END_OFFSET = 2;
    
    String target;
    HashMap<String, WebPage> pageList = new HashMap<>();
    
    public int solution(String word, String[] pages) {
        target = word.toLowerCase();
        for (int i = 0; i < pages.length; i++) {
            String url = parseWebPage(i, pages[i]);
            pageList.get(url).baseScore = countTargetWordsForWholePage(pages[i]);
        }
        
        for (String url : pageList.keySet()) {
            for (String outgoing : pageList.get(url).outgoingLinks) {
                if (pageList.containsKey(outgoing)) {
                    pageList.get(outgoing).incomingLinks.add(url);
                }
            }
        }
        
        double maxScore = 0.0;
        int maxScorePageIndex = 0;
        for (String url: pageList.keySet()) {
            WebPage currPage = pageList.get(url);
            for (String incoming : pageList.get(url).incomingLinks) {
                currPage.linkScore += (double) pageList.get(incoming).baseScore / pageList.get(incoming).outgoingLinks.size();
            }
            currPage.matchingScore = currPage.baseScore + currPage.linkScore;
            if (currPage.matchingScore > maxScore) {
                maxScore = currPage.matchingScore;
                maxScorePageIndex = currPage.index;
            } else if (currPage.matchingScore == maxScore) {
                maxScorePageIndex = Math.min(maxScorePageIndex, currPage.index);
            }
        }
        
        return maxScorePageIndex;
    }
    
    /**
     * 웹페이지에서 URL과 외부 링크 정보를 파싱하고 페이지 정보를 저장
     * 
     * @param index 페이지의 원래 인덱스
     * @param page 웹페이지의 HTML 문자열
     * @return 현재 페이지의 URL
     */
    private String parseWebPage(int index, String page) {
        StringTokenizer st = new StringTokenizer(page);
        String currToken = "";
        while (!currToken.contains(START_HEAD)) { // <head>가 나올 때까지 넘김 
            currToken = st.nextToken();
        }
        String url = parseHead(st);
        pageList.put(url, new WebPage(index));
        
        parseBody(url, st);
        return url;
    }
    
    /**
     * head 영역에서 현재 페이지의 URL을 추출
     * 
     * @param st 웹페이지를 토큰화한 StringTokenizer
     * @return 현재 페이지의 URL
     */
    private String parseHead(StringTokenizer st) {
        String url = null;
        String currToken = "";
        while (url == null) {
            currToken = st.nextToken();
            if (currToken.contains(META)) {
                currToken = st.nextToken();
                if (currToken.contains(PROPERTY)) {
                    currToken = st.nextToken();
                    while (!currToken.contains(CONTENT) && !currToken.contains(">")) {
                        currToken = st.nextToken();
                    }
                    if (currToken.contains(">") && !currToken.contains(CONTENT)) {
                        continue;
                    }
                    for (int i = URL_START_OFFSET; i < currToken.length(); i++) {
                        if (currToken.charAt(i) == '\"') {
                            url = currToken.substring(URL_START_OFFSET, i);
                            break;
                        }
                    }
                }
            }
        }
        return url;
    }
    
    /**
     * body 영역을 순회하며 외부 링크를 탐색
     * 
     * @param url 현재 페이지의 URL
     * @param st 웹페이지를 토큰화한 StringTokenzier
     */
    private void parseBody(String url, StringTokenizer st) {
        String currToken = "";
        while (st.hasMoreTokens()) {
            currToken = st.nextToken();
            if (currToken.contains(START_ANCHOR)) {
                parseAnchor(url, st);
            }
        }
    }
    
    /**
     * 태그에서 href에 해당하는 외부 URL을 추출하여 저장
     * 
     * @param url 현재 페이지의 URL
     * @param st 웹페이지를 토큰화한 StringTokenizer
     */
    private void parseAnchor(String url, StringTokenizer st) {
        String currToken = st.nextToken();
        if (currToken.startsWith(HREF)) {
            for (int i = 0; i < currToken.length(); i++) { // 외부 url에 해당하는 str만 추출
                if (currToken.charAt(i) == '\"' && currToken.charAt(i + 1) == '>') {
                    pageList.get(url).outgoingLinks.add(currToken.substring(HREF_START_OFFSET, i + 1 - HREF_END_OFFSET + 1));
                    break;
                }
            }
        }
        
        if (currToken.contains(START_ANCHOR)) {
            parseAnchor(url, st);
        }
    }

    /**
     * 전체 페이지에서 목표 단어가 등장하는 횟수를 계산
     * 
     * @param page 검사할 웹페이지의 HTML 문자열
     * @return 목표 단어가 등장한 횟수
     */
    private int countTargetWordsForWholePage(String page) {
        StringTokenizer st = new StringTokenizer(page);
        int baseScore = 0;
        while (st.hasMoreTokens()) {
            baseScore += countTargetWord(st.nextToken());
        }
        return baseScore;
    }
    
    /**
     * 문자열을 알파벳 단위로 분리하여 목표 단어와 일치하는 단어의 개수를 계산
     * 
     * @param currWord 검사할 문자열
     * @return 목표 단어와 일치하는 단어의 개수
     */
    private int countTargetWord(String currWord) {
        int countTargetWords = 0;
        String[] tokens = currWord.split("[^a-zA-Z]+");
        for (String token : tokens) { // 알파벳이 아닌 문자를 기준으로 단어 구분
            if (token.toLowerCase().equals(target)) countTargetWords++;
        }
        return countTargetWords;
    }
}