import java.util.*;

class Solution {

    static class Page {
        String url;
        int basicScore;
        List<String> links;

        Page(String url, int basicScore, List<String> links) {
            this.url = url;
            this.basicScore = basicScore;
            this.links = links;
        }
    }

    public int solution(String word, String[] pages) {
        int n = pages.length;

        Page[] pageInfo = new Page[n];

        Map<String, Integer> urlToIdx = new HashMap<>();

        // 1. 각 페이지 파싱
        for (int i = 0; i < n; i++) {
            pageInfo[i] = parsePage(pages[i], word);
            urlToIdx.put(pageInfo[i].url, i);
        }

        // 2. 기본 점수로 매칭 점수 초기화
        double[] matchingScore = new double[n];

        for (int i = 0; i < n; i++) {
            matchingScore[i] = pageInfo[i].basicScore;
        }

        // 3. 링크 점수 분배
        for (int i = 0; i < n; i++) {
            Page page = pageInfo[i];

            int outDegree = page.links.size();

            if (outDegree == 0) {
                continue;
            }

            double linkScore =
                    (double) page.basicScore / outDegree;

            for (String link : page.links) {

                // pages에 존재하는 URL인 경우에만 점수 추가
                if (urlToIdx.containsKey(link)) {
                    int targetIdx = urlToIdx.get(link);

                    matchingScore[targetIdx] += linkScore;
                }
            }
        }

        // 4. 최대 매칭 점수 찾기
        int answer = 0;
        double maxScore = matchingScore[0];

        for (int i = 1; i < n; i++) {
            if (matchingScore[i] > maxScore) {
                maxScore = matchingScore[i];
                answer = i;
            }
        }

        return answer;
    }

    private Page parsePage(String html, String word) {

        // 1. 현재 페이지 URL 추출
        int metaIdx = html.indexOf("<meta property=\"og:url\"");

        int urlStart = html.indexOf("https://", metaIdx);
        int urlEnd = html.indexOf("\"", urlStart);

        String url = html.substring(urlStart, urlEnd);

        // 2. 외부 링크 추출
        List<String> links = new ArrayList<>();

        int idx = 0;

        while (true) {
            int linkIdx = html.indexOf("<a href=\"", idx);

            if (linkIdx == -1) {
                break;
            }

            int linkStart =
                    linkIdx + "<a href=\"".length();

            int linkEnd =
                    html.indexOf("\"", linkStart);

            String link =
                    html.substring(linkStart, linkEnd);

            links.add(link);

            idx = linkEnd + 1;
        }

        // 3. 기본 점수 계산
        int basicScore =
                getBasicScore(html, word);

        return new Page(url, basicScore, links);
    }

    private int getBasicScore(String html, String word) {

        String target = word.toLowerCase();

        int count = 0;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < html.length(); i++) {

            char ch =
                    Character.toLowerCase(html.charAt(i));

            // 알파벳이면 현재 단어에 추가
            if ('a' <= ch && ch <= 'z') {
                sb.append(ch);
            }

            // 알파벳이 아니면 단어 하나가 끝난 것
            else {
                if (sb.toString().equals(target)) {
                    count++;
                }

                sb.setLength(0);
            }
        }

        // 문자열 마지막이 알파벳으로 끝나는 경우
        if (sb.toString().equals(target)) {
            count++;
        }

        return count;
    }
}