import assert from 'node:assert/strict';
import { spawnSync } from 'node:child_process';
import fs from 'node:fs';
import os from 'node:os';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { afterEach, beforeEach, test } from 'node:test';

const SCRIPT = fileURLToPath(new URL('./build-leaderboard.mjs', import.meta.url));

let workspace;

beforeEach(() => {
  workspace = fs.mkdtempSync(path.join(os.tmpdir(), 'algo-leaderboard-test-'));
  const dir = path.join(workspace, 'solutions/week-01/pgs-111');
  fs.mkdirSync(dir, { recursive: true });
  fs.writeFileSync(
    path.join(dir, '.problem.json'),
    JSON.stringify({ issue: 17, platformLabel: '프로그래머스', number: '111', title: 'A', url: 'https://x.com' }),
  );
  fs.mkdirSync(path.join(dir, 'alice'), { recursive: true });
  fs.writeFileSync(path.join(dir, 'alice', 'Solution.java'), 'class Solution {}');
  fs.writeFileSync(path.join(workspace, 'README.md'), '# test\n');
});

afterEach(() => {
  fs.rmSync(workspace, { recursive: true, force: true });
});

function runBuild(env) {
  const result = spawnSync(process.execPath, [SCRIPT], {
    env: { ...process.env, GITHUB_WORKSPACE: workspace, ...env },
    encoding: 'utf8',
  });
  assert.equal(result.status, 0, result.stderr);
  return fs.readFileSync(path.join(workspace, 'README.md'), 'utf8');
}

test('GITHUB_REPOSITORY가 있으면 이슈 번호를 클릭 가능한 마크다운 링크로 만든다', () => {
  const readme = runBuild({ GITHUB_REPOSITORY: 'owner/repo' });
  assert.match(readme, /\[#17\]\(https:\/\/github\.com\/owner\/repo\/issues\/17\)/);
});

test('풀이를 하위 폴더(v1, v2)로만 낸 사람도 제출자로 집계된다', () => {
  const base = path.join(workspace, 'solutions/week-01/pgs-111/bob');
  fs.mkdirSync(path.join(base, 'v1'), { recursive: true });
  fs.mkdirSync(path.join(base, 'v2'), { recursive: true });
  fs.writeFileSync(path.join(base, 'v1', 'Solution.java'), 'class Solution {}');
  fs.writeFileSync(path.join(base, 'v2', 'Solution.java'), 'class Solution {}');

  const readme = runBuild({ GITHUB_REPOSITORY: 'owner/repo' });
  assert.match(readme, /\[@bob\]\(https:\/\/github\.com\/bob\)/);
  // alice(직접) + bob(하위 폴더) 두 명이 제출 → 문제 행의 제출 수가 2, 제출자에 둘 다
  assert.match(readme, /\| 2 \| \[@alice\]\(https:\/\/github\.com\/alice\), \[@bob\]\(https:\/\/github\.com\/bob\) \|/);
});

test('GITHUB_REPOSITORY가 없으면(로컬 실행) "#N" 텍스트로만 표시한다', () => {
  const env = { ...process.env, GITHUB_WORKSPACE: workspace };
  delete env.GITHUB_REPOSITORY;
  const result = spawnSync(process.execPath, [SCRIPT], { env, encoding: 'utf8' });
  assert.equal(result.status, 0, result.stderr);
  const readme = fs.readFileSync(path.join(workspace, 'README.md'), 'utf8');
  assert.match(readme, /\|\s*#17\s*\|/);
});
