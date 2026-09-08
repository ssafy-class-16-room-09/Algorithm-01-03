import fs from 'node:fs';
import path from 'node:path';

export const SOLUTIONS_ROOT = 'solutions';
export const REGISTER_MARKER = '<!-- algo-study:registered -->';
export const REVIEW_MARKER = '<!-- algo-study:submission-check -->';

export function checklistMarker(dir) {
  return `<!-- algo-study:checklist:${dir} -->`;
}

/**
 * GitHub 이슈 폼(issue form)이 만들어내는 마크다운 본문을 { 라벨: 값 } 으로 변환한다.
 * 본문은 "### 필드명\n\n값" 블록이 반복되는 형태다.
 */
export function parseIssueForm(body = '') {
  const fields = {};
  const blocks = body.replace(/\r\n/g, '\n').split(/^###[ \t]+/m).slice(1);
  for (const block of blocks) {
    const nl = block.indexOf('\n');
    if (nl === -1) continue;
    const key = block.slice(0, nl).trim();
    const value = block.slice(nl + 1).trim();
    fields[key] = value === '_No response_' ? '' : value;
  }
  return fields;
}

export function platformLabel(key) {
  return key === 'swea' ? 'SWEA' : key === 'pgs' ? '프로그래머스' : key === 'ct' ? '코드트리' : key;
}

/** 문제 링크 도메인으로부터 플랫폼을 추정한다. 알 수 없는 도메인이면 null. */
export function urlPlatformKey(url = '') {
  const v = url.toLowerCase();
  if (v.includes('swexpertacademy.com')) return 'swea';
  if (v.includes('programmers.co.kr')) return 'pgs';
  if (v.includes('codetree.ai')) return 'ct';
  return null;
}

/**
 * 링크에서 문제 식별자를 뽑아낸다.
 *  - 프로그래머스: `lessons/{번호}` 의 숫자
 *  - 코드트리: 경로에 들어 있는 문제 슬러그 (하이픈 포함)
 *      .../frequent-problems/artistry/description                     → artistry
 *      .../training-field/frequent-problems/problems/codetree-omakase → codetree-omakase
 *  - SWEA: 링크가 해시라 불가능 → null (번호 직접 입력)
 */
export function problemNumberFromUrl(url = '') {
  const pgs = url.match(/lessons\/(\d+)/);
  if (pgs) return pgs[1];
  const ct = url.match(/codetree\.ai\/(?:[\w-]+\/)*(?:frequent-problems|problems)\/([a-z0-9][a-z0-9-]*)/i);
  if (ct) return ct[1];
  return null;
}

/**
 * 프로그래머스 문제 페이지의 <title>은 로그인 없이도
 * "코딩테스트 연습 - {문제 제목} | 프로그래머스 스쿨" 형태로 내려온다.
 * 거기서 문제 제목만 뽑아낸다. 형식이 안 맞으면(사이트 개편 등) null.
 */
export function parseProgrammersTitle(html = '') {
  const m = html.match(/<title>\s*코딩테스트 연습 - (.+?)\s*\|\s*프로그래머스 스쿨\s*<\/title>/);
  return m ? m[1].trim() : null;
}

/**
 * "문제 목록" 필드의 한 줄을 해석한다.
 * 형식: `링크 | 제목 | 번호(선택) | 난이도(선택) | 마감일(선택)`
 */
export function parseProblemLine(line) {
  const [url = '', title = '', number = '', difficulty = '', deadline = ''] = line
    .split('|')
    .map((s) => s.trim());
  return { url, title, number, difficulty, deadline };
}

export function weekDir(week) {
  return `week-${String(week).padStart(2, '0')}`;
}

export function problemDir(pKey, number) {
  return `${pKey}-${number}`;
}

/** solutions/week-01/swea-1859 */
export function problemPath(week, pKey, number) {
  return path.posix.join(SOLUTIONS_ROOT, weekDir(week), problemDir(pKey, number));
}

/**
 * solutions/week-01/swea-1859/JooeonLee/Solution.java 형태의 경로를 해석한다.
 * 규칙에 맞지 않으면 null.
 */
export function parseSolutionPath(filePath) {
  const m = filePath
    .replace(/\\/g, '/')
    .match(/^solutions\/week-(\d{2})\/([a-z]+)-([A-Za-z0-9_-]+)\/([^/]+)\/(.+)$/);
  if (!m) return null;
  const [, week, pKey, number, author, rest] = m;
  return {
    week: Number(week),
    weekDir: `week-${week}`,
    platform: pKey,
    number,
    author,
    file: rest,
    dir: `solutions/week-${week}/${pKey}-${number}/${author}`,
    problemDir: `solutions/week-${week}/${pKey}-${number}`,
  };
}

/** 풀이 하위 폴더 이름에 허용하는 문자 (컴파일 스텝이 공백으로 목록을 나누므로 공백 불가). */
export const SOLUTION_VARIANT_RE = /^[A-Za-z0-9._-]+$/;

/**
 * 한 제출 파일이 속한 "솔루션 루트"를 구한다.
 * 한 사람이 같은 문제에 여러 풀이를 낼 수 있도록, 본인 폴더 아래를
 *   {아이디}/Solution.java         → 루트는 {아이디} 폴더 (파일을 바로 둔 경우)
 *   {아이디}/v1/Solution.java      → 루트는 {아이디}/v1 (풀이별 하위 폴더)
 * 로 나눈다. 루트마다 따로 컴파일하므로 v1·v2의 `Solution` 클래스가 충돌하지 않는다.
 * parsed 는 parseSolutionPath()의 결과.
 */
export function solutionRoot(parsed) {
  const slash = parsed.file.indexOf('/');
  return slash === -1 ? parsed.dir : `${parsed.dir}/${parsed.file.slice(0, slash)}`;
}

/** .github/study-members.yml 의 members 목록. 없으면 레포 콜라보레이터로 대체. */
export async function getMembers({ github, context, workspace = process.cwd() }) {
  const file = path.join(workspace, '.github', 'study-members.yml');
  if (fs.existsSync(file)) {
    const raw = fs.readFileSync(file, 'utf8');
    const members = raw
      .split('\n')
      .map((line) => line.match(/^\s*-\s*([A-Za-z0-9][A-Za-z0-9-]*)\s*(?:#.*)?$/))
      .filter(Boolean)
      .map((m) => m[1]);
    if (members.length) return members;
  }
  const { owner, repo } = context.repo;
  const collaborators = await github.paginate(github.rest.repos.listCollaborators, {
    owner,
    repo,
    per_page: 100,
  });
  return collaborators.map((c) => c.login).filter((login) => !login.endsWith('[bot]'));
}

/** 마커가 들어간 댓글이 이미 있으면 수정하고, 없으면 새로 만든다. */
export async function upsertComment({ github, context, issue_number, marker, body }) {
  const { owner, repo } = context.repo;
  const comments = await github.paginate(github.rest.issues.listComments, {
    owner,
    repo,
    issue_number,
    per_page: 100,
  });
  const existing = comments.find((c) => (c.body || '').includes(marker));
  const full = `${marker}\n${body}`;
  if (existing) {
    await github.rest.issues.updateComment({ owner, repo, comment_id: existing.id, body: full });
    return { ...existing, body: full, updated: true };
  }
  const { data } = await github.rest.issues.createComment({ owner, repo, issue_number, body: full });
  return { ...data, updated: false };
}

/** 라벨이 없으면 만들고 붙인다. addLabels 만 쓰면 색이 임의로 정해져서 미리 만든다. */
export async function ensureLabels({ github, context, labels }) {
  const { owner, repo } = context.repo;
  for (const { name, color, description } of labels) {
    try {
      await github.rest.issues.getLabel({ owner, repo, name });
    } catch (err) {
      if (err.status !== 404) throw err;
      await github.rest.issues.createLabel({ owner, repo, name, color, description });
    }
  }
}

export function readProblemMeta(workspace, problemDirPath) {
  const file = path.join(workspace, problemDirPath, '.problem.json');
  if (!fs.existsSync(file)) return null;
  try {
    return JSON.parse(fs.readFileSync(file, 'utf8'));
  } catch {
    return null;
  }
}

function* iterateProblemMetas(workspace) {
  const root = path.join(workspace, SOLUTIONS_ROOT);
  if (!fs.existsSync(root)) return;
  for (const weekEntry of fs.readdirSync(root, { withFileTypes: true })) {
    if (!weekEntry.isDirectory() || !/^week-\d{2}$/.test(weekEntry.name)) continue;
    const weekPath = path.join(root, weekEntry.name);
    for (const problemEntry of fs.readdirSync(weekPath, { withFileTypes: true })) {
      if (!problemEntry.isDirectory()) continue;
      const dir = path.posix.join(SOLUTIONS_ROOT, weekEntry.name, problemEntry.name);
      const meta = readProblemMeta(workspace, dir);
      if (meta) yield { dir, meta };
    }
  }
}

/**
 * 특정 부모(주차) 이슈가 만든 문제 폴더를 solutions/ 전체에서 찾는다.
 * 부모 이슈 본문을 수정해 문제 목록이 바뀌면 예전 폴더가 남을 수 있어,
 * 그 폴더를 찾아 정리하는 데 쓴다.
 */
export function findProblemDirsByParentIssue(workspace, parentIssueNumber) {
  const results = [];
  for (const { dir, meta } of iterateProblemMetas(workspace)) {
    if (meta.parentIssue === parentIssueNumber) results.push(dir);
  }
  return results;
}
