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

    private static int solution() throws IOException {
    	Counter[] receptionList;
        Counter[] repairList;
        ArrayList<Customer> customerList;
        PriorityQueue<Customer> repairQueue;
        
        // 입력 받기 및 자료 구조 초기
        StringTokenizer st = new StringTokenizer(br.readLine());
        int numReception = Integer.parseInt(st.nextToken());
        int numRepair = Integer.parseInt(st.nextToken());
        int numCustomer = Integer.parseInt(st.nextToken());
        int targetReception = Integer.parseInt(st.nextToken());
        int targetRepair = Integer.parseInt(st.nextToken());
        
        customerList = new ArrayList<>();
        repairQueue = new PriorityQueue<>(
                Comparator.comparingInt((Customer c) -> c.repairArrivalTime)
                .thenComparingInt(c -> c.receptionNo));

        receptionList = new Counter[numReception + 1];
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= numReception; i++) {
            receptionList[i] = new Counter(Integer.parseInt(st.nextToken()));
        }
        repairList = new Counter[numRepair + 1];
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= numRepair; i++) {
            repairList[i] = new Counter(Integer.parseInt(st.nextToken()));
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= numCustomer; i++) {
            customerList.add(new Customer(i, Integer.parseInt(st.nextToken())));
        }
        customerList.sort(Comparator.comparingInt((Customer c) -> c.receptionArrivalTime).thenComparingInt(c -> c.id));
        
        int customerIndex = 0;
        int time = -1;
        int sameCounterCustomers = 0;
        while (customerIndex < numCustomer || !repairQueue.isEmpty()) {
            time++;
        
            for (int i = 1; i <= numReception; i++) {
            	Counter currReception = receptionList[i];
                if (customerIndex < numCustomer && currReception.nextAvailable <= time) { // 빈 카운터가 있다면
                	Customer currCustomer = customerList.get(customerIndex);
                    if (currCustomer.receptionArrivalTime <= time) {
                        currCustomer.receptionNo = i;
                        currReception.startTask(time);
                        currCustomer.repairArrivalTime = currReception.nextAvailable;
                        repairQueue.add(currCustomer);
                        customerIndex++;
                    } else {
                        break;
                    }
                }
            }
            
            for (int i = 1; i <= numRepair; i++) {
                if (repairList[i].nextAvailable <= time) {
                    Counter currRepair = repairList[i];
                    if (!repairQueue.isEmpty() && repairQueue.peek().repairArrivalTime <= time) {
                        Customer currCustomer = repairQueue.poll();
                        currRepair.startTask(time);
                        if (currCustomer.receptionNo == targetReception && i == targetRepair) {
                            sameCounterCustomers += currCustomer.id;
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        return (sameCounterCustomers != 0) ? sameCounterCustomers : -1;
    }
}