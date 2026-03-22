---
name: token-efficient-collaboration
description: Keep Cursor/agent replies short and reference-based in this KMP repo — paths and symbols instead of long quotes; when to expand.
origin: project-template
---

# Token-efficient collaboration (template)

## When to use

- Any multi-step task with subagents or reviewers
- When the user cares about speed/cost of long chats
- Before pasting large files or repeating `.cursor/rules/core-standards.mdc` content

## Rules (source of truth)

Read and apply **`.cursor/rules/token-efficiency.mdc`** (always-on for this workspace).

## Quick checklist

1. **Verdict line** first (OK / fix needed / blocked).
2. **Paths:** `features/<feature>/<feature>-ui/src/.../Foo.kt` + `Foo.bar()` — not 40 lines of
   context.
3. **Layer rules:** point to `.cursor/rules/<layer>.mdc` instead of copying bullets.
4. **Subagent handoff:** pass changed paths + intent in one short block.
5. **Expand only** when the user asks, or for security/correctness-critical nuance.

## Orchestrators

Skills `feature-edit-review-orchestrator` and `feature-scaffolding-orchestrator` should end with *
*short** summaries: categories run, paths touched, pass/fail — not full reviewer dumps.
