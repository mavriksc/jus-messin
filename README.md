# jus-messin

Mixed Java and Kotlin repository for learning, discovery, challenge solutions, and one-off experiments.

## What it is

This is a long-running personal sandbox rather than a single polished application. It collects:

- Advent of Code solutions
- HackerRank and LeetCode practice
- small data structure and algorithm experiments
- PDF, image, parsing, and file-format tests
- utility snippets and throwaway prototypes

## Project layout

- `src/main/java/org/mavriksc/messin/advent/`: Advent of Code work
- `src/main/java/org/mavriksc/messin/hackerrank/`: challenge solutions
- `src/main/java/org/mavriksc/messin/random/`: miscellaneous experiments
- `src/main/resources/`: sample inputs, fixtures, and test assets
- `scripts/` and `skills/`: newer local tooling experiments

## Build

The repo still uses an older Gradle setup and targets Java 8:

```powershell
.\gradlew build
```

## Notes

- This repo is primarily for exploration and learning.
- Many files are intentionally independent from each other.
- Dependency choices and structure reflect the age of the repo.
- Some assets and experiments are rough, large, or one-off by design.
