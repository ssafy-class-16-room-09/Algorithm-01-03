import assert from 'node:assert/strict';
import fs from 'node:fs';
import os from 'node:os';
import path from 'node:path';
import { afterEach, beforeEach, test } from 'node:test';
import { createMockGithub, makeCore, prContext } from './mock-github.mjs';
import { run as checkRun } from './check-submission.mjs';

const OWNER = 'owner';
const REPO = 'repo';

let workspace;
let github;
let originalCwd;

beforeEach(async () => {
  workspace = fs.mkdtempSync(path.join(os.tmpdir(), 'algo-check-test-'));
  github = createMockGithub();
  originalCwd = process.cwd();
  process.chdir(workspace);
  // PR도 이슈 번호를 공유한다 — 라벨을 붙일 수 있도록 #1로 만들어 둔다.
  await github.rest.issues.create({ title: 'PR #1' });
});

afterEach(() => {
  process.chdir(originalCwd);
  fs.rmSync(workspace, { recursive: true, force: true });
});

const files = (...names) => names.map((filename) => ({ filename, status: 'added' }));

test('하위 폴더로 나눈 풀이는 폴더마다 따로 컴파일 대상이 된다', async () => {
  github._state.prFiles = files(
    'solutions/week-01/pgs-1/alice/v1/Solution.java',
    'solutions/week-01/pgs-1/alice/v2/Solution.java',
  );
  const core = makeCore();
  await checkRun({ github, context: prContext(OWNER, REPO, 1, 'alice'), core });

  assert.equal(core._outputs.has_errors, 'false');
  assert.equal(core._outputs.java_count, '2');
  assert.deepEqual(core._outputs.dirs.split(' ').sort(), [
    'solutions/week-01/pgs-1/alice/v1',
    'solutions/week-01/pgs-1/alice/v2',
  ]);
});

test('하위 폴더 없이 낸 여러 파일은 본인 폴더 하나로 묶여 컴파일된다', async () => {
  github._state.prFiles = files(
    'solutions/week-01/pgs-1/alice/Solution.java',
    'solutions/week-01/pgs-1/alice/Truck.java',
  );
  const core = makeCore();
  await checkRun({ github, context: prContext(OWNER, REPO, 1, 'alice'), core });

  assert.equal(core._outputs.has_errors, 'false');
  assert.equal(core._outputs.java_count, '2');
  assert.equal(core._outputs.dirs, 'solutions/week-01/pgs-1/alice');
});

test('풀이 하위 폴더 이름에 공백이 있으면 규칙 위반으로 막는다', async () => {
  github._state.prFiles = files('solutions/week-01/pgs-1/alice/v 1/Solution.java');
  const core = makeCore();
  await checkRun({ github, context: prContext(OWNER, REPO, 1, 'alice'), core });

  assert.equal(core._outputs.has_errors, 'true');
  assert.match(core._outputs.__failed, /경로 규칙 위반/);
});

test('폴더에 바로 둔 풀이와 하위 폴더 풀이를 섞으면 막는다', async () => {
  github._state.prFiles = files(
    'solutions/week-01/pgs-1/alice/Solution.java',
    'solutions/week-01/pgs-1/alice/v2/Solution.java',
  );
  const core = makeCore();
  await checkRun({ github, context: prContext(OWNER, REPO, 1, 'alice'), core });

  assert.equal(core._outputs.has_errors, 'true');
  assert.match(core._outputs.__failed, /경로 규칙 위반/);
});

test('코드트리 슬러그 폴더에 낸 여러 풀이도 정상 처리된다', async () => {
  github._state.prFiles = files(
    'solutions/week-05/ct-codetree-omakase/alice/greedy/Solution.java',
    'solutions/week-05/ct-codetree-omakase/alice/dp/Solution.java',
  );
  const core = makeCore();
  await checkRun({ github, context: prContext(OWNER, REPO, 1, 'alice'), core });

  assert.equal(core._outputs.has_errors, 'false');
  assert.deepEqual(core._outputs.dirs.split(' ').sort(), [
    'solutions/week-05/ct-codetree-omakase/alice/dp',
    'solutions/week-05/ct-codetree-omakase/alice/greedy',
  ]);
});
