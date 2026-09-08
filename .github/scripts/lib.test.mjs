import assert from 'node:assert/strict';
import fs from 'node:fs';
import os from 'node:os';
import path from 'node:path';
import { test } from 'node:test';
import {
  checklistMarker,
  findProblemDirsByParentIssue,
  parseIssueForm,
  parseProblemLine,
  parseProgrammersTitle,
  parseSolutionPath,
  platformLabel,
  problemDir,
  problemPath,
  problemNumberFromUrl,
  solutionRoot,
  urlPlatformKey,
  weekDir,
} from './lib.mjs';

test('parseIssueForm: "### 필드" 블록을 라벨-값 맵으로 바꾼다', () => {
  const body = ['### 주차', '', '1', '', '### 문제 목록', '', 'line1', 'line2', '', '### 메모', '', '_No response_'].join('\n');
  const fields = parseIssueForm(body);
  assert.equal(fields['주차'], '1');
  assert.equal(fields['문제 목록'], 'line1\nline2');
  assert.equal(fields['메모'], ''); // _No response_ 는 빈 문자열로 취급
});

test('parseProblemLine: 파이프로 구분된 줄을 필드로 나눈다', () => {
  const line = 'https://x.com/1 | 제목 | 42 | Lv.2 | 2026-08-10';
  assert.deepEqual(parseProblemLine(line), {
    url: 'https://x.com/1',
    title: '제목',
    number: '42',
    difficulty: 'Lv.2',
    deadline: '2026-08-10',
  });
});

test('parseProblemLine: 뒤 필드가 생략돼도 빈 문자열로 채운다', () => {
  const result = parseProblemLine('https://x.com/1 | 제목');
  assert.equal(result.number, '');
  assert.equal(result.difficulty, '');
  assert.equal(result.deadline, '');
});

test('urlPlatformKey: 도메인으로 플랫폼을 판별한다', () => {
  assert.equal(urlPlatformKey('https://swexpertacademy.com/x'), 'swea');
  assert.equal(urlPlatformKey('https://school.programmers.co.kr/x'), 'pgs');
  assert.equal(urlPlatformKey('https://www.codetree.ai/training-field/frequent-problems/problems/x'), 'ct');
  assert.equal(urlPlatformKey('https://example.com/x'), null);
});

test('platformLabel: 플랫폼 키를 한글/영문 표기로 바꾼다', () => {
  assert.equal(platformLabel('swea'), 'SWEA');
  assert.equal(platformLabel('pgs'), '프로그래머스');
  assert.equal(platformLabel('ct'), '코드트리');
  assert.equal(platformLabel('unknown'), 'unknown');
});

test('problemNumberFromUrl: 프로그래머스는 숫자 번호, 코드트리는 슬러그를 뽑는다', () => {
  assert.equal(problemNumberFromUrl('https://school.programmers.co.kr/learn/courses/30/lessons/12345'), '12345');
  assert.equal(problemNumberFromUrl('https://swexpertacademy.com/main/code/problem/problemDetail.do?contestProbId=1'), null);
  assert.equal(
    problemNumberFromUrl('https://www.codetree.ai/training-field/frequent-problems/problems/codetree-omakase'),
    'codetree-omakase',
  );
  assert.equal(problemNumberFromUrl('https://www.codetree.ai/frequent-problems/artistry/description'), 'artistry');
  assert.equal(
    problemNumberFromUrl('https://www.codetree.ai/training-field/search/problems/two-people/submissions/1'),
    'two-people',
  );
});

test('parseProgrammersTitle: <title> 태그에서 문제 제목만 뽑는다', () => {
  const html = '<html><head><title>코딩테스트 연습 - 짝수와 홀수 | 프로그래머스 스쿨</title></head></html>';
  assert.equal(parseProgrammersTitle(html), '짝수와 홀수');
});

test('parseProgrammersTitle: 형식이 안 맞으면(사이트 개편 등) null', () => {
  assert.equal(parseProgrammersTitle('<title>어떤 다른 페이지</title>'), null);
  assert.equal(parseProgrammersTitle(''), null);
});

test('weekDir / problemDir / problemPath', () => {
  assert.equal(weekDir(1), 'week-01');
  assert.equal(weekDir(12), 'week-12');
  assert.equal(problemDir('swea', '1859'), 'swea-1859');
  assert.equal(problemPath(1, 'pgs', '42627'), 'solutions/week-01/pgs-42627');
});

test('parseSolutionPath: 규칙에 맞는 경로를 해석한다', () => {
  const parsed = parseSolutionPath('solutions/week-01/swea-1859/JooeonLee/Solution.java');
  assert.deepEqual(parsed, {
    week: 1,
    weekDir: 'week-01',
    platform: 'swea',
    number: '1859',
    author: 'JooeonLee',
    file: 'Solution.java',
    dir: 'solutions/week-01/swea-1859/JooeonLee',
    problemDir: 'solutions/week-01/swea-1859',
  });
});

test('parseSolutionPath: 코드트리 슬러그(하이픈 포함)도 해석한다', () => {
  const parsed = parseSolutionPath('solutions/week-05/ct-codetree-omakase/JooeonLee/Solution.java');
  assert.deepEqual(parsed, {
    week: 5,
    weekDir: 'week-05',
    platform: 'ct',
    number: 'codetree-omakase',
    author: 'JooeonLee',
    file: 'Solution.java',
    dir: 'solutions/week-05/ct-codetree-omakase/JooeonLee',
    problemDir: 'solutions/week-05/ct-codetree-omakase',
  });
});

test('solutionRoot: 본인 폴더에 바로 둔 파일은 본인 폴더가, 하위 폴더로 나눈 풀이는 그 폴더가 루트', () => {
  const flat = parseSolutionPath('solutions/week-01/pgs-1/alice/Solution.java');
  assert.equal(solutionRoot(flat), 'solutions/week-01/pgs-1/alice');

  const v1 = parseSolutionPath('solutions/week-01/pgs-1/alice/v1/Solution.java');
  assert.equal(solutionRoot(v1), 'solutions/week-01/pgs-1/alice/v1');

  const nested = parseSolutionPath('solutions/week-01/pgs-1/alice/v2/pkg/Main.java');
  assert.equal(solutionRoot(nested), 'solutions/week-01/pgs-1/alice/v2');
});

test('parseSolutionPath: 규칙에 안 맞으면 null', () => {
  assert.equal(parseSolutionPath('solutions/week-1/swea-1859/JooeonLee/Solution.java'), null); // 주차가 2자리 아님
  assert.equal(parseSolutionPath('solutions/week-01/swea-1859/Solution.java'), null); // 작성자 폴더 없음
  assert.equal(parseSolutionPath('README.md'), null);
});

test('checklistMarker: 폴더 경로별로 다른 마커를 만든다', () => {
  assert.notEqual(checklistMarker('solutions/week-01/pgs-1'), checklistMarker('solutions/week-01/pgs-2'));
  assert.ok(checklistMarker('solutions/week-01/pgs-1').includes('solutions/week-01/pgs-1'));
});

test('findProblemDirsByParentIssue: .problem.json의 parentIssue로 폴더를 찾는다', () => {
  const workspace = fs.mkdtempSync(path.join(os.tmpdir(), 'algo-lib-test-'));
  try {
    const dirA = path.join(workspace, 'solutions/week-01/pgs-1');
    const dirB = path.join(workspace, 'solutions/week-01/pgs-2');
    const dirC = path.join(workspace, 'solutions/week-02/pgs-3');
    for (const d of [dirA, dirB, dirC]) fs.mkdirSync(d, { recursive: true });
    fs.writeFileSync(path.join(dirA, '.problem.json'), JSON.stringify({ issue: 11, parentIssue: 10 }));
    fs.writeFileSync(path.join(dirB, '.problem.json'), JSON.stringify({ issue: 12, parentIssue: 10 }));
    fs.writeFileSync(path.join(dirC, '.problem.json'), JSON.stringify({ issue: 21, parentIssue: 20 }));

    const found = findProblemDirsByParentIssue(workspace, 10).sort();
    assert.deepEqual(found, ['solutions/week-01/pgs-1', 'solutions/week-01/pgs-2']);
    assert.deepEqual(findProblemDirsByParentIssue(workspace, 20), ['solutions/week-02/pgs-3']);
    assert.deepEqual(findProblemDirsByParentIssue(workspace, 999), []);
  } finally {
    fs.rmSync(workspace, { recursive: true, force: true });
  }
});
