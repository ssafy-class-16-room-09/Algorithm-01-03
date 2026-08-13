# 길 찾기 게임 풀이 정리

## 핵심 아이디어

노드를 다음 기준으로 정렬한다.

1. `y` 좌표 내림차순
2. `y`가 같다면 `x` 좌표 오름차순

부모 노드는 자식보다 항상 `y` 좌표가 크므로, 높은 위치의 노드부터 처리하면 부모가 먼저 트리에 들어간다.

이후 각 노드를 `x` 좌표를 기준으로 이진 탐색 트리에 삽입한다.

- 현재 노드보다 `x`가 작으면 왼쪽
- 현재 노드보다 `x`가 크면 오른쪽

트리를 완성한 뒤 전위 순회와 후위 순회를 수행한다.

---

## 시간 복잡도

노드 수를 `N`, 트리 높이를 `H`라고 하자.

### 1. 정렬

```text
O(N log N)
```

### 2. 트리 생성

노드 하나를 삽입할 때 최대 트리 높이만큼 내려가므로:

```text
O(H)
```

전체 노드를 삽입하면:

```text
O(NH)
```

트리가 균형에 가까우면 `H = log N`이므로:

```text
O(N log N)
```

하지만 트리가 한쪽으로 치우치면 `H = N`이므로:

```text
O(N²)
```

실제로 삽입 비용은 다음과 같이 누적된다.

```text
1 + 2 + 3 + ... + N
= N(N + 1) / 2
= O(N²)
```

### 3. 순회

전위 순회와 후위 순회는 각각 모든 노드를 한 번씩 방문한다.

```text
O(N)
```

### 전체 시간 복잡도

```text
O(N log N + NH)
```

일반적인 최악의 경우:

```text
O(N²)
```

단, 이 문제에서는 트리 깊이가 최대 `1,000`으로 제한되어 있으므로 실제 입력에서는 다음 범위로 제한된다.

```text
O(N log N + N × 1000)
```

---

## 풀이 코드

```java
import java.util.*;

class Solution {
    public int[][] solution(int[][] nodeinfo) {
        int[][] answer = new int[2][];

        Node root = makeTree(nodeinfo);

        ArrayList<Integer> preorder = new ArrayList<>();
        ArrayList<Integer> postorder = new ArrayList<>();

        preOrder(root, preorder);
        postOrder(root, postorder);

        answer[0] = preorder.stream()
                .mapToInt(Integer::intValue)
                .toArray();

        answer[1] = postorder.stream()
                .mapToInt(Integer::intValue)
                .toArray();

        return answer;
    }

    private static class Node {
        int number;
        int x;
        int y;

        Node left;
        Node right;

        Node(int number, int x, int y) {
            this.number = number;
            this.x = x;
            this.y = y;
        }
    }

    private Node makeTree(int[][] nodeinfo) {
        List<Node> nodes = new ArrayList<>();

        for (int i = 0; i < nodeinfo.length; i++) {
            nodes.add(new Node(
                    i + 1,
                    nodeinfo[i][0],
                    nodeinfo[i][1]
            ));
        }

        nodes.sort((a, b) -> {
            if (a.y != b.y) {
                return Integer.compare(b.y, a.y);
            }

            return Integer.compare(a.x, b.x);
        });

        Node root = nodes.get(0);

        for (int i = 1; i < nodes.size(); i++) {
            insert(root, nodes.get(i));
        }

        return root;
    }

    private void insert(Node current, Node newNode) {
        if (newNode.x < current.x) {
            if (current.left == null) {
                current.left = newNode;
            } else {
                insert(current.left, newNode);
            }
        } else {
            if (current.right == null) {
                current.right = newNode;
            } else {
                insert(current.right, newNode);
            }
        }
    }

    private void preOrder(Node node, List<Integer> result) {
        if (node == null) {
            return;
        }

        result.add(node.number);
        preOrder(node.left, result);
        preOrder(node.right, result);
    }

    private void postOrder(Node node, List<Integer> result) {
        if (node == null) {
            return;
        }

        postOrder(node.left, result);
        postOrder(node.right, result);
        result.add(node.number);
    }
}
```

---

## 정리

- 문제의 좌표 규칙을 이용해 이진 탐색 트리로 구성할 수 있다.
- `y` 내림차순 정렬로 부모를 먼저 처리한다.
- `x` 좌표를 기준으로 왼쪽과 오른쪽 자식을 결정한다.
- 현재 삽입 방식은 트리가 한쪽으로 치우치면 최악의 경우 `O(N²)`이다.
- 문제에서는 트리 깊이가 최대 `1,000`으로 제한되어 있어 충분히 통과할 수 있다.
