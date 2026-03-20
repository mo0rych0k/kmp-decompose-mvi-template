---
name: feature-scaffolding-orchestrator
description: Orchestrate feature scaffolding using the template’s subagents, then run layer-specific reviewers/enforcers for correctness (UI/Navigation, MVI wiring, Domain use-cases, Data repository mappers, Data-network Ktor dispatchers).
origin: project-template
---

# Feature Scaffolding Orchestrator

## Trigger

Use this skill when the user asks to:

- “create a new feature”
- “scaffold a feature module”
- “add a feature screen / layer” and the work should follow the repository’s KMP + Decompose + MVI +
  layer separation rules.

## Goal

Coordinate multiple specialized subagents so the resulting feature is:

- scaffolded according to `.cursor/rules/feature-module-structure.mdc`
- integrated into global app navigation via `AppFeature` and `AppRootConfig`
- validated per layer (UI navigation+MVI DI, domain use-case/resource flow, data-network dispatcher
  usage, repository mapping boundaries)

## Workflow

### 1) Intake (ask only what’s missing)

Ask the user for:

- `featureName`
- whether to add an optional `*-ui-components` module
- what the feature needs to show (e.g., screens, network endpoints, persistence needs)

Derive:

- module paths: `features/<featureName>/<featureName>-{ui,domain,data,data-network}`
- package namespaces: `io.pylyp.<featureName>.<layer>`

### 2) Scaffold the feature modules

Launch subagent:

- `subagent_type="feature-module-generator"`

Rules for this step:

- Ensure `*-domain`, `*-data`, and `*-data-network` are scaffolded (create or reuse existing modules
  as required by the template rules).
- Do NOT leave a feature only with `*-ui` unless the other modules already exist.

### 3) UI + Navigation + DI validation gate

After scaffolding and/or UI implementation:
Launch reviewer subagents (in order):

1. `subagent_type="ui-layer-decompose-mvi-navigation-di-reviewer"` (Validate Decompose root,
   `AppFeature` navigation contract, isolated DI)
2. `subagent_type="ui-domain-mvi-state-methods-reviewer"` (Validate State/Intent/Label and store->
   component->screen wiring)

If issues are found:

- iterate by applying the minimal fixes and re-running the same reviewers

### 4) Domain layer validation gate

Launch:

- `subagent_type="domain-layer-usecases-resource-reviewer"`

Focus:

- resource/loading/error mapping consistency
- domain doesn’t depend on UI/Data/Network

### 5) Data + Data-network validation gate

Launch:

1. `subagent_type="data-network-layer-ktor-dispatchers-reviewer"` (Check dispatcher usage in Ktor
   remote sources)
2. `subagent_type="data-layer-repository-mappers-reviewer"` (Check mapping boundaries:
   network/DTO -> domain entities)

### 6) Optional end-to-end MVI quality loop

If the feature includes non-trivial MVI UI wiring:
Launch:

- `subagent_type="mvi-edit-review-loop"`

Use it to:

- ensure Compose/Decompose UI is generated and wired in a single consistent loop

## Output requirements

When finished, summarize:

- feature modules created/reused
- where global navigation integration was updated (`AppFeature`, `AppRootComponent`,
  `AppRootMapper`, `app di modules`)
- which reviewers/enforcers were run and whether they passed (or what was fixed)

