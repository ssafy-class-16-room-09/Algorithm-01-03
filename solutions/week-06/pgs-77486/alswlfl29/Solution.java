import java.util.*;

class Seller {
    int idx; // 인덱스
    String name; // 이름
    Seller recommender; // 추천인
    
    Seller(int idx, String name, Seller recommender) {
        this.idx = idx;
        this.name = name;
        this.recommender = recommender;
    }
}

class Solution {
    
    static final int COST = 100; // 칫솔 판매 이익의 개당 가격
    static final double PERCENT = 0.1; // 나눠줘야 하는 퍼센트
    
    public int[] solution(String[] enroll, String[] referral, String[] seller, int[] amount) {
        int[] answer = {};
        int N = enroll.length; // 판매원 총 수
        
        Map<String, Seller> sellers = new HashMap<>(); // 판매원 정보
        for(int i=0; i<N; i++) { 
            Seller recommender = sellers.getOrDefault(referral[i], null); // 추천인 정보
            sellers.put(enroll[i], new Seller(i, enroll[i], recommender));
        }
        
        int[] profits = new int[N]; // 총 판매 이익
        for(int i=0; i<seller.length; i++) {
            Seller current = sellers.get(seller[i]); // 현재 이익이 생긴 판매원
            int profit = amount[i]*COST; // 얻게되는 이익

            while(current != null) {
                if(profit < 1) break; // 더 이상 나눠가질 이익이 없을 때
                
                int share = (int) (profit * PERCENT); // 나눠줘야 하는 금액
                profits[current.idx] += profit - share; // 10% 나눠주고 본인이 가질 이익
                if(current.recommender != null) profit = share; // 추천인이 받을 이익
                current = current.recommender;
            }
        }
        
        return profits;
    }
}