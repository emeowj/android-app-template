#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<EOF
Usage: $(basename "$0") [--main <branch>] [--worktree <path>] [<branch>]

Rebases a feature worktree branch onto main, moves to the worktree that hosts
main, rebases main onto the feature branch, then removes the feature worktree
and deletes the local feature branch.

Defaults:
  --main      main
  --worktree current git worktree
  <branch>   current branch in --worktree

Examples:
  $(basename "$0")
  $(basename "$0") codex/my-feature
  $(basename "$0") --worktree ../bound-feature codex/my-feature
EOF
}

main_branch="main"
feature_worktree=""
feature_branch=""

while [[ $# -gt 0 ]]; do
  case "$1" in
    --main)
      if [[ $# -lt 2 || -z "$2" ]]; then
        echo "--main requires a branch name." >&2
        exit 1
      fi
      main_branch="${2:-}"
      shift 2
      ;;
    --worktree)
      if [[ $# -lt 2 || -z "$2" ]]; then
        echo "--worktree requires a path." >&2
        exit 1
      fi
      feature_worktree="${2:-}"
      shift 2
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    -*)
      echo "Unknown option: $1" >&2
      usage
      exit 1
      ;;
    *)
      if [[ -n "$feature_branch" ]]; then
        echo "Multiple branches provided." >&2
        usage
        exit 1
      fi
      feature_branch="$1"
      shift
      ;;
  esac
done

if [[ -z "$main_branch" ]]; then
  echo "Main branch must be non-empty." >&2
  exit 1
fi

if [[ -z "$feature_worktree" ]]; then
  feature_worktree=$(git rev-parse --show-toplevel)
fi

feature_worktree=$(cd "$feature_worktree" && pwd)

if ! git -C "$feature_worktree" rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "Not a git worktree: $feature_worktree" >&2
  exit 1
fi

current_branch=$(git -C "$feature_worktree" branch --show-current)
if [[ -z "$feature_branch" ]]; then
  feature_branch="$current_branch"
fi

if [[ -z "$feature_branch" ]]; then
  echo "Feature branch could not be inferred. Pass it explicitly." >&2
  exit 1
fi

if [[ "$feature_branch" == "$main_branch" ]]; then
  echo "Refusing to clean up the main branch." >&2
  exit 1
fi

if [[ -n "$current_branch" && "$current_branch" != "$feature_branch" ]]; then
  echo "Worktree is on $current_branch, not $feature_branch." >&2
  exit 1
fi

if ! git -C "$feature_worktree" show-ref --verify --quiet "refs/heads/$main_branch"; then
  echo "Main branch not found: $main_branch" >&2
  exit 1
fi

if ! git -C "$feature_worktree" show-ref --verify --quiet "refs/heads/$feature_branch"; then
  echo "Feature branch not found: $feature_branch" >&2
  exit 1
fi

if [[ -n "$(git -C "$feature_worktree" status --porcelain)" ]]; then
  echo "Feature worktree has uncommitted changes: $feature_worktree" >&2
  exit 1
fi

main_worktree=$(
  git -C "$feature_worktree" worktree list --porcelain |
    awk -v branch="refs/heads/$main_branch" '
      /^worktree / { path = substr($0, 10) }
      /^branch / && substr($0, 8) == branch { print path; exit }
    '
)

if [[ -z "$main_worktree" ]]; then
  echo "No worktree is hosting $main_branch." >&2
  exit 1
fi

main_worktree=$(cd "$main_worktree" && pwd)

if [[ "$main_worktree" == "$feature_worktree" ]]; then
  echo "Feature and main worktrees resolve to the same path." >&2
  exit 1
fi

if [[ -n "$(git -C "$main_worktree" status --porcelain)" ]]; then
  echo "Main worktree has uncommitted changes: $main_worktree" >&2
  exit 1
fi

echo "Rebasing $feature_branch onto $main_branch in $feature_worktree"
git -C "$feature_worktree" switch "$feature_branch" >/dev/null
git -C "$feature_worktree" rebase "$main_branch"

echo "Rebasing $main_branch onto $feature_branch in $main_worktree"
git -C "$main_worktree" switch "$main_branch" >/dev/null
git -C "$main_worktree" rebase "$feature_branch"

if ! git -C "$main_worktree" merge-base --is-ancestor "$feature_branch" "$main_branch"; then
  echo "$main_branch does not contain $feature_branch after rebase; aborting cleanup." >&2
  exit 1
fi

echo "Switching to main worktree $main_worktree"
cd "$main_worktree"

echo "Removing worktree $feature_worktree"
git worktree remove "$feature_worktree"

echo "Deleting local branch $feature_branch"
git branch -d "$feature_branch"

echo "Done. Current worktree: $PWD"
