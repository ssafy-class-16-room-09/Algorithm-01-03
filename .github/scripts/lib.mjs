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

const PLATFORM_LABELS = {
  swea: 'SWEA',
  pgs: '프로그래머스',
  ct: '코드트리',
  boj: '백준',
  jungol: '정올',
};

export function platformLabel(key) {
  return PLATFORM_LABELS[key] ?? key;
}

/** 링크의 호스트가 domain 자신이거나 그 하위 도메인인지. */
function hostIs(host, domain) {
  return host === domain || host.endsWith(`.${domain}`);
}

/** 문제 링크 도메인으로부터 플랫폼을 추정한다. 알 수 없는 도메인이면 null. */
export function urlPlatformKey(url = '') {
  const v = url.toLowerCase();
  if (v.includes('swexpertacademy.com')) return 'swea';
  if (v.includes('programmers.co.kr')) return 'pgs';
  if (v.includes('codetree.ai')) return 'ct';
  // 백준/정올은 includes 대신 호스트를 정확히 본다. (예: "notacmicpc.net" 같은 오판 방지)
  let host = '';
  try {
    host = new URL(url).hostname.toLowerCase();
  } catch {
    return null;
  }
  // 백준: 2026-11-20 재오픈 후 정식 주소가 acmicpc.net 으로 바뀌었다 (www.acmicpc.net → 301).
  //       단축 주소 boj.kr/{번호} 도 그대로 받는다.
  if (hostIs(host, 'acmicpc.net') || host === 'boj.kr') return 'boj';
  if (hostIs(host, 'jungol.co.kr')) return 'jungol';
  return null;
}

/**
 * 백준·정올 링크를 정식 주소로 바꾼다. 정올 목록에서 들어오면 붙는 `?cursor=...`,
 * 백준의 www/단축 주소(boj.kr)를 걷어내서 README·현황판 링크가 항상 같은 모양이 되게 한다.
 * 번호를 못 뽑으면 원래 링크를 그대로 돌려준다. 다른 플랫폼은 건드리지 않는다.
 */
export function canonicalProblemUrl(pKey, url, number) {
  if (!/^\d+$/.test(number || '')) return url;
  if (pKey === 'boj') return `https://acmicpc.net/problem/${number}`;
  if (pKey === 'jungol') return `https://jungol.co.kr/problem/${number}`;
  return url;
}

/**
 * 링크에서 문제 식별자를 뽑아낸다.
 *  - 프로그래머스: `lessons/{번호}` 의 숫자
 *  - 코드트리: 경로에 들어 있는 문제 슬러그 (하이픈 포함)
 *      .../frequent-problems/artistry/description                     → artistry
 *      .../training-field/frequent-problems/problems/codetree-omakase → codetree-omakase
 *  - 백준: `acmicpc.net/problem/{번호}` 또는 단축 주소 `boj.kr/{번호}`
 *      대회 전용 링크(`/contest/problem/{대회}/{순번}`)는 전체 문제 번호가 아니라서 null (번호 직접 입력)
 *  - 정올: `jungol.co.kr/problem/{번호}` (뒤에 ?cursor=... 가 붙어도 됨)
 *  - SWEA: 링크가 해시라 불가능 → null (번호 직접 입력)
 */
export function problemNumberFromUrl(url = '') {
  const boj = url.match(/^https?:\/\/(?:[\w-]+\.)*acmicpc\.net\/problem\/(\d+)(?:[/?#]|$)/i)
    || url.match(/^https?:\/\/boj\.kr\/(\d+)(?:[/?#]|$)/i);
  if (boj) return boj[1];
  const jungol = url.match(/^https?:\/\/(?:[\w-]+\.)*jungol\.co\.kr\/problem\/(\d+)(?:[/?#]|$)/i);
  if (jungol) return jungol[1];
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

/** <title> 안의 흔한 HTML 엔티티만 풀어 준다. (문제 제목에 &, <, " 등이 들어가는 경우) */
function decodeEntities(s) {
  return s
    .replace(/&#x([0-9a-f]+);/gi, (_, h) => String.fromCodePoint(parseInt(h, 16)))
    .replace(/&#(\d+);/g, (_, d) => String.fromCodePoint(Number(d)))
    .replace(/&quot;/g, '"')
    .replace(/&#39;|&apos;/g, "'")
    .replace(/&lt;/g, '<')
    .replace(/&gt;/g, '>')
    .replace(/&nbsp;/g, ' ')
    .replace(/&amp;/g, '&');
}

/**
 * 백준 문제 페이지의 <title>은 로그인 없이 "{번호}번: {제목}" 형태로 내려온다.
 * 없는 문제면 "Baekjoon Online Judge"로 떨어지므로 null.
 * number를 주면 그 번호의 페이지인지까지 확인한다.
 */
export function parseBojTitle(html = '', number) {
  const m = html.match(/<title>\s*(\d+)번:\s*([\s\S]+?)\s*<\/title>/);
  if (!m || (number && m[1] !== String(number))) return null;
  return decodeEntities(m[2]).trim() || null;
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
