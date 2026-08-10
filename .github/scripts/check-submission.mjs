import fs from 'node:fs';
import path from 'node:path';
import {
  REVIEW_MARKER,
  ensureLabels,
  parseSolutionPath,
  platformLabel,
  readProblemMeta,
  upsertComment,
} from './lib.mjs';

/**
 * PR에 담긴 파일들이 스터디 규칙을 지키는지 검사한다.
 *  - solutions/week-XX/{platform}-{번호}/{본인아이디}/*.java 경로인지
 *  - 남의 폴더를 건드리지는 않았는지
 *  - .java 파일이 실제로 들어 있는지
 * 컴파일할 디렉터리 목록을 출력으로 넘겨 다음 스텝에서 javac를 돌린다.
 */
export async function run({ github, context, core }) {
  const workspace = process.env.GITHUB_WORKSPACE || process.cwd();
  const { owner, repo } = context.repo;
  const pr = context.payload.pull_request;
  const author = pr.user.login;

  const files = await github.paginate(github.rest.pulls.listFiles, {
    owner,
    repo,
    pull_number: pr.number,
    per_page: 100,
  });

  const errors = [];
  const warnings = [];
  const dirs = new Set();
  const problems = new Map();
  let javaCount = 0;

  for (const file of files) {
    const p = file.filename;
    if (file.status === 'removed') continue;

    // 문제 폴더 자체의 메타 파일은 워크플로가 만든 것이라 그대로 통과시킨다.
    if (/^solutions\/week-\d{2}\/[a-z]+-[A-Za-z0-9_]+\/(README\.md|\.problem\.json)$/.test(p)) {
      warnings.push(`\`${p}\` — 문제 폴더 공용 파일을 수정했습니다. 의도한 변경인지 확인해 주세요.`);
      continue;
    }

    if (!p.startsWith('solutions/')) {
      warnings.push(`\`${p}\` — 풀이 폴더 밖의 파일입니다. 설정 변경이 아니라면 되돌려 주세요.`);
      continue;
    }

    const parsed = parseSolutionPath(p);
    if (!parsed) {
      errors.push(
        `\`${p}\` — 경로 규칙에 맞지 않습니다. \`solutions/week-01/swea-1859/${author}/Solution.java\` 형태여야 합니다.`,
      );
      continue;
    }

    if (parsed.author.toLowerCase() !== author.toLowerCase()) {
      errors.push(
        `\`${p}\` — 본인(\`${author}\`) 폴더가 아니라 \`${parsed.author}\` 폴더입니다. 다른 사람 풀이는 수정할 수 없습니다.`,
      );
      continue;
    }

    if (!p.endsWith('.java')) {
      warnings.push(`\`${p}\` — \`.java\`가 아닌 파일입니다. 메모라면 괜찮습니다.`);
      continue;
    }

    javaCount += 1;
    dirs.add(parsed.dir);
    if (!problems.has(parsed.problemDir)) {
      problems.set(parsed.problemDir, readProblemMeta(workspace, parsed.problemDir));
    }
  }

  if (javaCount === 0 && errors.length === 0) {
    warnings.push('제출된 `.java` 파일이 없습니다. 풀이 PR이 맞는지 확인해 주세요.');
  }

  // 라벨 정리: 이 PR이 다루는 주차 / 플랫폼 / 문제를 붙여 둔다.
  const labels = new Set();
  const problemLabels = new Set();
  for (const [dirPath, meta] of problems) {
    const parsed = parseSolutionPath(`${dirPath}/${author}/Solution.java`);
    if (parsed) labels.add(parsed.weekDir);
    if (meta?.platformLabel) labels.add(meta.platformLabel);
    else if (parsed) labels.add(platformLabel(parsed.platform));
    // Projects의 Slice by 는 필드 값만 쪼갠다. 제목 텍스트로는 안 되므로
    // 문제별로 PR을 모아 보려면 PR 자신이 문제 라벨을 들고 있어야 한다.
    // 경로에서 유도하니 사람이 제목을 어떻게 쓰든 어긋나지 않는다.
    if (parsed) {
      const problemLabel = `${parsed.platform}-${parsed.number}`;
      labels.add(problemLabel);
      problemLabels.add(problemLabel);
    }
  }
  if (labels.size) {
    const labelStyle = (name) => {
      if (name.startsWith('week-')) return { color: '5319e7', description: '스터디 주차' };
      if (problemLabels.has(name)) return { color: 'fbca04', description: '문제' };
      return { color: 'c2e0c6', description: '문제 출처' };
    };
    await ensureLabels({
      github,
      context,
      labels: [...labels].map((name) => ({ name, ...labelStyle(name) })),
    });
    await github.rest.issues.addLabels({
      owner,
      repo,
      issue_number: pr.number,
      labels: [...labels],
    });
  }

  core.setOutput('dirs', [...dirs].join(' '));
  core.setOutput('has_errors', errors.length > 0 ? 'true' : 'false');
  core.setOutput('java_count', String(javaCount));

  // 검사 결과를 PR에 고정 댓글로 남긴다. 컴파일 결과는 다음 스텝에서 덧붙인다.
  const linked = [...problems.entries()]
    .map(([dirPath, meta]) =>
      meta
        ? `- ${meta.platformLabel} ${meta.number} · ${meta.title} → 이슈 #${meta.issue}`
        : `- \`${dirPath}\` (등록 이슈를 찾지 못했습니다)`,
    )
    .join('\n');

  const state = {
    errors,
    warnings,
    linked,
    javaCount,
    dirs: [...dirs],
    issues: [...problems.values()].filter(Boolean).map((m) => m.issue),
  };
  fs.writeFileSync(path.join(workspace, 'check-state.json'), JSON.stringify(state, null, 2));

  if (errors.length) {
    await upsertComment({
      github,
      context,
      issue_number: pr.number,
      marker: REVIEW_MARKER,
      body: [
        '## ❌ 제출 규칙 검사 실패',
        '',
        ...errors.map((e) => `- ${e}`),
        warnings.length ? `\n<details><summary>참고 사항 ${warnings.length}건</summary>\n\n${warnings.map((w) => `- ${w}`).join('\n')}\n\n</details>` : '',
      ].join('\n'),
    });
    core.setFailed('제출 경로 규칙 위반');
  }
}

/** javac 실행 후 최종 리포트를 남긴다. */
export async function report({ github, context, core }) {
  const workspace = process.env.GITHUB_WORKSPACE || process.cwd();
  const statePath = path.join(workspace, 'check-state.json');
  if (!fs.existsSync(statePath)) return;
  const state = JSON.parse(fs.readFileSync(statePath, 'utf8'));

  const compileOk = process.env.COMPILE_RESULT === 'success';
  const compileLog = (process.env.COMPILE_LOG || '').trim();

  const body = [
    compileOk ? '## ✅ 제출 검사 통과' : '## ❌ 컴파일 실패',
    '',
    `- 경로 규칙: ✅ 통과 (\`.java\` ${state.javaCount}개)`,
    `- 컴파일: ${compileOk ? '✅ 통과' : '❌ 실패'}`,
    '',
    state.linked ? `### 연결된 문제\n\n${state.linked}` : '',
    !compileOk && compileLog
      ? `\n<details open><summary>javac 출력</summary>\n\n\`\`\`\n${compileLog.slice(0, 8000)}\n\`\`\`\n\n</details>`
      : '',
    state.warnings.length
      ? `\n<details><summary>참고 사항 ${state.warnings.length}건</summary>\n\n${state.warnings.map((w) => `- ${w}`).join('\n')}\n\n</details>`
      : '',
    '',
    compileOk ? '리뷰어 승인 후 머지하면 이슈 체크리스트가 자동으로 갱신됩니다.' : '',
  ]
    .filter(Boolean)
    .join('\n');

  await upsertComment({
    github,
    context,
    issue_number: context.payload.pull_request.number,
    marker: REVIEW_MARKER,
    body,
  });

  if (!compileOk) core.setFailed('javac 컴파일 실패');
}
