# CRM Realm - Junior Software Engineer Intern Assessment

Submission by **Mijanur Rahman Oli**.

| Part | What | Where |
|------|------|-------|
| A | Java console app: Simple Lead Tracker | [`java/`](java) |
| B | JavaScript problems + tests | [`javascript/`](javascript) |
| C | SQL queries (PostgreSQL) | [`sql/queries.sql`](sql/queries.sql) |
| D | Approach and testing notes | this README |

```
.
├── java/src/com/crmrealm/leadtracker/
│   ├── Lead.java            record: name, email, score (validates itself)
│   ├── Priority.java        enum: HOT, WARM, COLD
│   ├── LeadTracker.java     in-memory store + calculatePriority(score)
│   ├── Main.java            console menu
│   └── LeadTrackerTest.java dependency-free tests
├── javascript/
│   ├── solutions.js         the three functions
│   ├── solutions.test.js    tests (node:test, no packages)
│   └── package.json
└── sql/queries.sql
```

## How to run

### Java (JDK 17+, no libraries)

Run from the repository root:

```bash
# compile
mkdir -p out && javac -d out java/src/com/crmrealm/leadtracker/*.java

# run the app
java -cp out com.crmrealm.leadtracker.Main

# run the tests (prints "22 passed, 0 failed"; exits non-zero on failure)
java -cp out com.crmrealm.leadtracker.LeadTrackerTest
```

Menu: `1` add a lead, `2` view all leads, `3` exit.

### JavaScript (Node.js 18+, no packages to install)

```bash
cd javascript
npm test          # same as: node --test
```

### SQL

`sql/queries.sql` targets **PostgreSQL 12+** and also runs on MySQL 8+. See the comments in the file.

## Part A – design notes

- `Lead` is an immutable **record**. Validation (non-blank name/email, score 0–100) lives in its constructor, so an invalid lead cannot exist anywhere in the program.
- **Priority is never stored.** `LeadTracker.calculatePriority(int)` derives it from the score each time it is needed, so it cannot become inconsistent with the score.
- Leads are kept in an `ArrayList` (insertion order) and exposed through an unmodifiable view.
- `Main` only handles I/O. All rules are in `Lead` and `LeadTracker`, which makes them easy to test.
- Invalid input (empty name/email, score out of range, non-numeric score) prints a clear message and returns to the menu.

## Part B – the main idea behind each solution

**1. `firstNonRepeatingChar`** – Two passes: count every character in a `Map` using its lower-cased form, then return the first original character whose count is 1. Case is ignored for counting but preserved in the result. Spaces and punctuation are counted like any other character (see decisions below). Iterating by code point keeps emoji intact. O(n).

**2. `findTwoSum`** – One pass with a `Map` of `value -> index`. For each number, check whether `target - number` has already been seen; if so, return both indices. Because we look up *before* storing the current element, an element can never pair with itself. O(n) time instead of O(n²) for the nested-loop approach.

**3. `countLeadsByStatus`** – Walk the array once and increment a counter per status in a `Map`, then convert it with `Object.fromEntries`. Any new status works with no code change, the input array is never modified, and an empty array gives `{}`.

**Including statuses with a count of zero:** pass a list of known statuses (e.g. `['NEW','CONTACTED','QUALIFIED','LOST']`) and start every one at `0` before counting. The function already supports this through an optional second argument.

## Test cases

Java (`LeadTrackerTest`, 22 checks):

| Input | Expected |
|-------|----------|
| score 0 / 49 | COLD |
| score 50 / 79 | WARM |
| score 80 / 100 | HOT |
| score -1 / 101 | rejected (boundary, invalid) |
| empty, blank or null name | rejected |
| empty or blank email | rejected |
| `"  Ada Lovelace "` | stored as `Ada Lovelace` (trimmed) |
| non-numeric score typed in the menu | "must be a whole number" message |
| view with no leads | helpful "No leads yet" message |

JavaScript (`solutions.test.js`, 21 tests):

| Call | Result |
|------|--------|
| `firstNonRepeatingChar("swiss")` | `"w"` |
| `firstNonRepeatingChar("sTreSS")` | `"T"` (original case kept) |
| `firstNonRepeatingChar("aAbB")` / `""` | `null` |
| `firstNonRepeatingChar("aa bb")` | `" "` (space is a character) |
| `firstNonRepeatingChar(123)` | throws `TypeError` |
| `findTwoSum([3, 3], 6)` | `[0, 1]` |
| `findTwoSum([3], 6)` | `null` (no self-pairing) |
| `findTwoSum([], 0)` | `null` |
| `findTwoSum([0, 4, 0], 0)` / negatives | valid pair |
| `countLeadsByStatus([])` | `{}` |
| new status such as `ON_HOLD` | counted |
| input array after the call | unchanged |

## Decisions and assumptions

- **Spaces and punctuation count as characters** in `firstNonRepeatingChar`. The brief does not say to skip them, and silently dropping characters would be a surprising behaviour. The catch: `"a b a"` returns `"b"`, because two spaces repeat. If only letters should count, it is a one-line filter.
- **Email is only checked for being non-empty**, exactly as specified. I did not add format validation because the brief did not ask for it and a hand-written regex tends to reject valid addresses.
- Names and emails are trimmed before storing.
- SQL: PostgreSQL assumed; `score` assumed to be non-NULL. Query 2 uses a `LEFT JOIN` against the three priorities so a group with zero leads still shows `0`.

## What I would improve with more time

- Persist leads (a file or database) so they survive restarts, and add edit/delete/search/sort-by-score.
- Add email format validation and duplicate-email detection.
- Move the Java tests to JUnit with Maven or Gradle, and add CI (GitHub Actions) that runs both test suites.

## AI tools used

I used **Claude (Anthropic)** to draft the project structure, the Java and JavaScript code, the tests and this README. I then compiled and ran everything. Running the tests caught a mistake in one of my own test cases (`"a b a"` has two spaces), which I corrected. The design decisions above are mine to explain in a follow-up discussion.
