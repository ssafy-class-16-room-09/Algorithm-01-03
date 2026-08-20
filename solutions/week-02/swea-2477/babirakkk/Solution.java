import java.util.*;
import java.io.*;

class Customer {
    final int id;
    final int receptionArrivalTime;
    int repairArrivalTime;
    int receptionNo;
    Customer(int id, int receptionArrivalTime) {
        this.id = id;
        this.receptionArrivalTime = receptionArrivalTime;
    }
}

class Counter {
    int nextAvailable = 0;
    final int duration;
    Counter(int duration) {
        this.duration = duration;
    }

    void startTask(int time) {
        this.nextAvailable = time + duration;
    }
}

class Solution {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

	public static void main(String args[]) throws Exception {
		
		int testCases = Integer.parseInt(br.readLine());
		for(int t = 1; t <= testCases; t++) {
            bw.write("#" + t + " " + solution() + "\n");
		}
        bw.flush();
        bw.close();
        br.close();
	}

	
	/**
	 * 차량 정비소 시뮬레이션을 수행하고 지정된 접수/정비 창구를 모두 이용한 고객들의 ID 합을 반환 
	 * @return 지정된 접수/정비 창구를 이용한 고객들의 ID 합 (해당 고객이 없으면 -1)
	 * @throws IOException 입출력 예외 발생 시 
	 */
    private static int solution() throws IOException {        
        // 입력 받기 및 자료 구조 초기화 
        StringTokenizer st = new StringTokenizer(br.readLine());
        int numReception = Integer.parseInt(st.nextToken());
        int numRepair = Integer.parseInt(st.nextToken());
        int numCustomer = Integer.parseInt(st.nextToken());
        int targetReception = Integer.parseInt(st.nextToken());
        int targetRepair = Integer.parseInt(st.nextToken());

        Counter[] receptionCounters = readCounters(numReception);
        Counter[] repairCounters = readCounters(numRepair);
        
        Customer[] receptionQueue = readCustomers(numCustomer);
        ArrayList<Customer> repairQueue = new ArrayList<>();

        
        // 접수 창구 처리 
        int receptionIndex = 0;
        int time = receptionQueue[0].receptionArrivalTime;
        while (receptionIndex < numCustomer) { // reception counter 먼저 처리
        	int nextTime = Integer.MAX_VALUE;
        	for (int i = 1; i <= numReception; i++) {
        		Counter currReception = receptionCounters[i];
        		Customer currCustomer = receptionQueue[receptionIndex];
                
        		// 빈 창구가 있고 현재 도착한 고객이 있는 경우
        		if (currReception.nextAvailable <= time && currCustomer.receptionArrivalTime <= time) { // 빈 카운터가 있다면
                    currReception.startTask(time);                	
                    currCustomer.receptionNo = i;
                    currCustomer.repairArrivalTime = currReception.nextAvailable;
                    repairQueue.add(currCustomer);
                    
                    // 배치 완료 시 다음 고객으로 이동 
                    if (++receptionIndex >= numCustomer) {
                    	break;
                    }
                }
                nextTime = Math.min(nextTime, currReception.nextAvailable);
        	}
        	// 다음 시점으로 jump: 가장 일찍 비는 창구 시각 vs 다음 고객 도착 시각 
        	time = (nextTime > time || receptionIndex >= numCustomer) ? nextTime : receptionQueue[receptionIndex].receptionArrivalTime;
        }
        
        // 정비 큐를 정비 창구 도착 시간, 접수 창구 번호 오름차순으로 정렬 
        repairQueue.sort(Comparator.comparingInt((Customer c) -> c.repairArrivalTime)
                .thenComparingInt(c -> c.receptionNo));
        
        // 정비 창구 처리 
        int targetCustomerIdSum = 0;
        int repairIndex = 0;
        time = repairQueue.get(0).repairArrivalTime;
        while (repairIndex < numCustomer) {
        	int nextTime = Integer.MAX_VALUE;
            for (int i = 1; i <= numRepair; i++) {
                Counter currRepair = repairCounters[i];
                Customer currCustomer = repairQueue.get(repairIndex);
                
                // 빈 정비 창구가 있고, 손님이 접수를 마치고 정비 창구에 도착한 경우 
                if (currRepair.nextAvailable <= time && currCustomer.repairArrivalTime <= time) {
                	currRepair.startTask(time);
                    
                	// target 창구를 모두 이용한 손님의 ID 합산 
                	if (currCustomer.receptionNo == targetReception && i == targetRepair) {
                    	targetCustomerIdSum += currCustomer.id;
                    }
                    
                    if (++repairIndex >= numCustomer) {
                    	break;
                    }
                }
                nextTime = Math.min(nextTime, currRepair.nextAvailable);
            }
        	// 다음 시점으로 jump: 가장 일찍 비는 창구 시각 vs 다음 고객 도착 시각 
        	time = (nextTime > time || repairIndex >= numCustomer) ? nextTime : repairQueue.get(repairIndex).repairArrivalTime;
        }

        return (targetCustomerIdSum != 0) ? targetCustomerIdSum : -1;
    }
    
    
    /**
     * 입력 스트림에서 고객 정보를 읽어 도착 시간 및 ID 오름차순으로 정렬된 배열을 반환 
     * @param size 총 고객 
     * @return 도착 시간 및 ID 순으로 정렬된 Customer 객체 배열 
     * @throws IOException 입출력 예외 발생 시 
     */
    private static Customer[] readCustomers(int size) throws IOException {
    	Customer[] customerList = new Customer[size];
    	StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= size; i++) {
            customerList[i - 1] = new Customer(i, Integer.parseInt(st.nextToken()));
        }
        Arrays.sort(customerList, Comparator.comparingInt((Customer c) -> c.receptionArrivalTime).thenComparingInt(c -> c.id));
        return customerList;
    }
    
    
    /**
     * 입력 스트림에서 창구별 처리 시간을 읽어 index가 1부터 시작하는 Counter 배열을 생성해 반환 
     * @param size 읽어올 총 창구 
     * @return 창구 번호(1 ~ size)를 인덱스로 사용하는 Counter 객체 배열 
     * @throws IOException 입출력 예외 발생 시 
     */
    private static Counter[] readCounters(int size) throws IOException {
    	Counter[] counterList = new Counter[size + 1];
    	StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= size; i++) {
            counterList[i] = new Counter(Integer.parseInt(st.nextToken()));
        }
        return counterList;
    }
}