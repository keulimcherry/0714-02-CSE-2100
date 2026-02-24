<div align="center">

# Pac-Man — Architecture & Technical Report

```
╔════════════════════════════════════════════════════════════════════════════╗
║  C (C11)  │  Raylib 5.0+  │  February 2026                                 ║
╠════════════════════════════════════════════════════════════════════════════╣
║  8 Modules  │  60% Complexity ↓  │  320→35 Ghost AI Lines                  ║
╚════════════════════════════════════════════════════════════════════════════╝
```

**A Pac-Man clone in C + Raylib 5.0, refactored from a 500-line monolith**  
**into a clean, modular architecture.**

This document demonstrates professional software engineering through complete  
architectural documentation, transparent AI collaboration, and systematic refactoring.

*Created by Sneha Shah (240242) & Prerana Gupta (240241)*

---

</div>

## Project Overview

<table>
<tr>
<td><b>Language</b></td>
<td>C (C11)</td>
<td><b>Engine</b></td>
<td>Raylib 5.0+</td>
</tr>
<tr>
<td><b>Platform</b></td>
<td>Windows (Linux/macOS portable)</td>
<td><b>Architecture</b></td>
<td>Lightweight Entity-Component</td>
</tr>
<tr>
<td><b>Frame Rate</b></td>
<td>Fixed 60 FPS</td>
<td><b>Codebase</b></td>
<td>~500 lines → 8 modules</td>
</tr>
</table>

### Refactoring Impact

| Metric | Before | After | Improvement |
|--------|--------|-------|-------------|
| **Modules** | 1 monolithic file | 8 focused files | Clear separation |
| **Complexity** | ~25 avg | ~8 avg | 60% reduction |
| **Ghost AI Code** | 320 lines (duplicated) | 35 lines (reusable) | 90% reduction |
| **Function Length** | ~80 lines avg | ~30 lines avg | 62% shorter |

---

## Game Features

<table>
<tr>
<th width="25%">Feature</th>
<th>Detail</th>
</tr>
<tr>
<td><b>Maze</b></td>
<td>Classic 28×31 tile grid with tunnel wrapping</td>
</tr>
<tr>
<td><b>Ghosts</b></td>
<td>4 personalities: Blinky (Red), Pinky (Pink), Inky (Cyan), Clyde (Orange)</td>
</tr>
<tr>
<td><b>Ghost Modes</b></td>
<td>Scatter, Chase, Frightened, Eaten, Respawning</td>
</tr>
<tr>
<td><b>Power Pellets</b></td>
<td>Make ghosts vulnerable; combo scoring 200→400→800→1600 pts</td>
</tr>
<tr>
<td><b>Fruit System</b></td>
<td>Spawns at 50% completion; level-scaled points (100-5000)</td>
</tr>
<tr>
<td><b>Progression</b></td>
<td>Color-changing mazes with increasing difficulty</td>
</tr>
<tr>
<td><b>Audio</b></td>
<td>9 sound effects + background siren loop</td>
</tr>
<tr>
<td><b>High Score</b></td>
<td>Memory persistence with rainbow celebration mode</td>
</tr>
<tr>
<td><b>Lives System</b></td>
<td>3 lives with complete death/game-over state machine</td>
</tr>
</table>

---

## Table of Contents

<table>
<tr>
<td width="50%">

**Planning & Setup**
1. [Project Objective](#project-objective)
2. [System Requirements](#system-requirements)
3. [Build Instructions](#build-instructions)

**Architecture**
4. [Module Architecture](#module-architecture)
5. [Per-Frame Pipeline](#per-frame-pipeline-60-hz)
6. [Module Documentation](#module-documentation)

</td>
<td width="50%">

**Refactoring Process**
7. [Before & After](#before--after-the-refactoring)
8. [Design Decisions](#design-decisions)
9. [Human vs AI](#human-vs-ai--who-built-what)

**Reference**
10. [Code Style](#code-style-conventions)
11. [Limitations](#limitations-and-known-issues)
12. [Future Work](#future-enhancements)
13. [Conclusion](#conclusion)

</td>
</tr>
</table>

---

## Project Objective

The primary objective of this project was to design and implement a **functionally complete Pac-Man clone** using the C programming language and the Raylib game library. The project was undertaken as an exercise in applied software engineering, with the intention of moving beyond isolated programming exercises toward a cohesive, interactive system built from multiple cooperating components.

### Problem Statement

> **The Challenge:** A working prototype that has grown organically into a difficult-to-maintain monolithic codebase.

The problem this project addresses is common in early-to-intermediate software development: the initial implementation achieved functional correctness but at the cost of readability, testability, and extensibility. The core challenge was therefore not only to build a game, but to **refactor it into a structure that reflects professional software design principles**.

### Learning Goals

<table>
<tr>
<th width="30%">Goal</th>
<th>Description</th>
</tr>
<tr>
<td><b>C Programming</b></td>
<td>Manual memory layout, struct composition, pointer usage, working within a systems-level language with no built-in object model</td>
</tr>
<tr>
<td><b>Modular Design</b></td>
<td>Separating concerns across .h/.c file pairs, managing shared global state, designing clean public APIs per module</td>
</tr>
<tr>
<td><b>Game Loop</b></td>
<td>Implementing a fixed-timestep 60 Hz loop with distinct input, update, and render phases</td>
</tr>
<tr>
<td><b>AI Systems</b></td>
<td>Reproducing ghost personalities, mode cycling, and the historical overflow bug accurately</td>
</tr>
<tr>
<td><b>AI Collaboration</b></td>
<td>Critically evaluating AI-generated code, integrating it responsibly, maintaining clear attribution</td>
</tr>
</table>

---

## System Requirements

**Note:** No internet connection required. No external dependencies beyond Raylib.

<table>
<tr>
<th width="30%">Component</th>
<th>Requirement</th>
</tr>
<tr>
<td><b>Operating System</b></td>
<td>Windows 10+ (primary); Linux / macOS portable</td>
</tr>
<tr>
<td><b>Compiler</b></td>
<td>GCC 11.0+ — MinGW-w64 recommended on Windows</td>
</tr>
<tr>
<td><b>Raylib Version</b></td>
<td>5.0 or later</td>
</tr>
<tr>
<td><b>C Standard</b></td>
<td>C11 (<code>-std=c11</code>)</td>
</tr>
<tr>
<td><b>RAM</b></td>
<td>256 MB minimum — game uses &lt;10 MB at runtime</td>
</tr>
<tr>
<td><b>Display</b></td>
<td>672 × 864 px minimum window</td>
</tr>
<tr>
<td><b>Audio</b></td>
<td>Any system audio device — optional, game runs without it</td>
</tr>
<tr>
<td><b>Storage</b></td>
<td>&lt; 50 MB including headers, binary, and audio assets</td>
</tr>
</table>

---

## Build Instructions

The following assumes Windows with MinGW-w64 and Raylib 5.0 installed. Adjust paths to match your local setup.

### Step 1: Clone or Download

```bash
git clone https://github.com/yourname/raylib-pacman.git
cd raylib-pacman
```

### Step 2: Compile

```bash
gcc -std=c11 -o pacman.exe \
    main.c map.c pacman.c ghost.c render.c resources.c \
    -I"C:/raylib/include" \
    -L"C:/raylib/lib" \
    -lraylib -lopengl32 -lgdi32 -lwinmm \
    -Wall -Wextra
```

> **Note:** Replace `C:/raylib/include` and `C:/raylib/lib` with your actual Raylib paths.

### Step 3: Assets

The binary expects an `assets/` folder in the same directory containing all `.wav` sound files and `spritesheet.png`. Expected filenames are defined in `resources.c`.

```
raylib-pacman/
├── pacman.exe
├── assets/
│   ├── chomp.wav
│   ├── death.wav
│   ├── eat_fruit.wav
│   └── ...
```

### Step 4: Run

```bash
./pacman.exe
```

#### Linux Note

Replace `-lopengl32 -lgdi32 -lwinmm` with:
```bash
-lGL -lm -lpthread -ldl -lrt -lX11
```

If Raylib is installed system-wide, the `-I` and `-L` flags may not be needed.

> **Future Improvement:** A Makefile is planned for easier cross-platform compilation.

---

## Module Architecture

### File Structure

```
raylib-pacman/
├── main.c          → Entry point & game loop (~25 lines)
├── types.h         → Shared structs, enums, constants
├── map.h / map.c   → Maze layout, collision detection
├── pacman.h / .c   → Player input, movement, state machine
├── ghost.h / .c    → AI pathfinding, personality targeting
├── render.h / .c   → Drawing & visual effects pipeline
├── resources.h/.c  → Asset loading & lifecycle management
└── raylib.dll      → Raylib runtime
```

### Dependency Graph

```
main.c
  │
  ├─► types.h          ◄─── all modules depend on this
  │
  ├─► map.h
  │
  ├─► pacman.h  ─────► map.h
  │
  ├─► ghost.h   ─────► map.h, types.h
  │
  ├─► render.h  ─────► map.h, types.h, resources.h
  │
  └─► resources.h
```

---

## Per-Frame Pipeline (60 Hz)

Each frame executes the following stages in strict order. No drawing function is called before all game state has been resolved for that frame.

```
┌──────────────────────────────────────────────────────────────┐
│  FRAME N (16.67ms @ 60 FPS)                                  │
└──────────────────────────────────────────────────────────────┘

[1] INPUT POLLING
   ├─ IsKeyDown() reads arrow keys
   └─ Stores next direction in pacman.nextDir

[2] STATE UPDATE
   ├─ UpdateGame() drives the state machine
   └─ Timers for state transitions

[3] ENTITY UPDATES
   ├─ UpdatePacman()
   │  ├─ Movement & tile alignment
   │  ├─ Tunnel wrapping
   │  └─ Collectible detection
   │
   └─ UpdateGhosts()
      ├─ Target tile calculation per personality
      ├─ Pathfinding execution
      └─ Mode management (Scatter/Chase/Frightened)

[4] COLLISION DETECTION
   ├─ Pac-Man vs Walls (CanPacmanMove)
   ├─ Pac-Man vs Ghosts (distance checks)
   ├─ Pac-Man vs Collectibles (tile matching)
   └─ Ghost vs Walls (CanMove)

[5] LOGIC RESOLUTION
   ├─ Score updates
   ├─ Power pellet activation
   ├─ Ghost mode changes
   ├─ Death handling
   └─ Level completion

[6] AUDIO UPDATES
   ├─ Play/stop sounds based on events
   ├─ Manage background siren loop
   └─ Handle sound priorities

[7] RENDERING
   └─ DrawGame()
      ├─ DrawMaze() → walls, dots, power pellets
      ├─ Draw fruit (if spawned)
      ├─ Draw ghosts (color, animation, eyes)
      ├─ Draw Pac-Man (mouth animation)
      ├─ Draw HUD (score, lives, level)
      └─ Draw state overlays (READY!, GAME OVER)
```

---

## Module Documentation

### `types.h` — Core Data Structures

> Single source of truth for all shared types. Every module includes this header.

#### Key Enums

```c
// Game States
typedef enum { 
    STATE_INTRO, STATE_READY, STATE_PLAYING,
    STATE_DEATH, STATE_LEVEL_COMPLETE, STATE_GAME_OVER 
} GameState;

// Ghost Personalities
typedef enum { 
    GHOST_BLINKY = 0,  // Red - Direct chaser
    GHOST_PINKY  = 1,  // Pink - Ambusher
    GHOST_INKY   = 2,  // Cyan - Flanker
    GHOST_CLYDE  = 3   // Orange - Shy
} GhostType;

// Ghost Behavior Modes
typedef enum { 
    MODE_SCATTER,     // Return to corner
    MODE_CHASE,       // Hunt Pac-Man
    MODE_FRIGHTENED,  // Run away (blue)
    MODE_EATEN,       // Return to spawn
    MODE_RESPAWNING   // Waiting in house
} GhostMode;

// Movement Directions
typedef enum { 
    DIR_NONE = 0, DIR_UP = 1, DIR_DOWN = 2,
    DIR_LEFT = 3, DIR_RIGHT = 4 
} Direction;
```

#### Central Game Struct

```c
typedef struct {
    Entity  pacman;
    Ghost   ghosts[GHOST_COUNT];
    int     maze[MAZE_HEIGHT][MAZE_WIDTH]; // 0=empty 1=wall 2=dot 3=pellet

    // Scoring
    int   score, highScore, lives, level, dotsEaten, totalDots;

    // Power-Up System
    int   powerPelletActive;
    float powerPelletTimer;
    int   ghostsEaten;          // Combo counter: 200/400/800/1600

    // State Management
    GameState state;
    float     stateTimer;

    // Fruit System
    int   fruitSpawned, fruitEaten;
    float fruitX, fruitY, fruitTimer;

    // Visual Effects
    bool  rainbowMode;          // High score celebration
} Game;

extern Game game;  // Global singleton
```

> **Design Note:** Using a single global `Game` struct is idiomatic in C game development. Passing `Game*` through every call adds boilerplate without benefit at this scale.

---

### `main.c` — Entry Point

> Intentionally minimal. Owns the window lifecycle and delegates everything else.

```c
int main(void) {
    InitWindow(SCREEN_WIDTH, SCREEN_HEIGHT, "PAC-MAN");
    InitAudioDevice();
    SetTargetFPS(60);

    LoadResources();  // Load all audio assets
    InitGame();       // Initialize game state

    while (!WindowShouldClose()) {
        UpdateGame();  // Update all game logic
        
        BeginDrawing();
        ClearBackground(BLACK);
        DrawGame();    // Render everything
        EndDrawing();
    }

    UnloadResources();
    CloseAudioDevice();
    CloseWindow();
    return 0;
}
```

**Key Points:**
- Only ~25 lines including includes
- No game logic — purely initialization and delegation
- Clear lifecycle: Init → Loop → Cleanup
- Fixed 60 FPS timestep for consistent physics

---

### `map` — Maze Management

> Owns the 28×31 tile grid and all collision queries.

#### Tile Codes

| Code | Meaning |
|------|---------|
| `0` | Empty (dot eaten, or open path) |
| `1` | Wall (collision boundary) |
| `2` | Dot (10 points) |
| `3` | Power Pellet (50 points) |
| `4` | Ghost House interior (ghosts only) |

#### Public API

| Function | Responsibility |
|----------|---------------|
| `InitMaze()` | Copy template into `game.maze`; count total dots |
| `ResetLevel()` | Clear collectibles; keep score and lives |
| `ResetPositions()` | Move Pac-Man and ghosts to spawn |
| `IsWall(x, y)` | Returns true if tile is type 1 |
| `CanMove(x, y, dir)` | True if moving `dir` from `(x,y)` is valid; handles tunnel edge |

---

### `pacman` — Player Logic

> Handles input, tile-aligned movement, collectibles, ghost collision, and the game state machine.

#### Turn Queueing — The Key Detail

```c
// Turns are queued and only applied when sprite is grid-aligned
// This prevents corner-cutting and matches the arcade feel

bool aligned = ((int)game.pacman.x % TILE_SIZE == 0) &&
               ((int)game.pacman.y % TILE_SIZE == 0);

if (aligned && CanPacmanMove(tileX, tileY, nextDir))
    game.pacman.direction = nextDir;
```

#### Ghost Collision & Combo Scoring

```c
int points[] = { 200, 400, 800, 1600 };  // Ghosts 1-4 per power pellet

if (dist < TILE_SIZE * 0.7f) {
    if (ghost->mode == MODE_FRIGHTENED)
        game.score += points[game.ghostsEaten++];  // Combo scoring
    else if (ghost->mode != MODE_EATEN && ghost->mode != MODE_RESPAWNING)
        game.state = STATE_DEATH;  // Death
}
```

#### State Machine

| State | Behavior |
|-------|----------|
| `INTRO` | Show title; wait for intro music or SPACE key |
| `READY` | 2-second countdown; display "READY!" overlay |
| `PLAYING` | Run `UpdatePacman()` + `UpdateGhosts()`; check win/death/fruit |
| `DEATH` | 2-second animation; decrement lives or → `GAME_OVER` |
| `LEVEL_COMPLETE` | 2-second flash; increment level; reset maze |
| `GAME_OVER` | Show final score; SPACE restarts via `InitGame()` |

---

### `ghost` — AI System

> Each ghost runs the same movement engine but selects different target tiles based on personality.

#### Pathfinding (Greedy Best-First)

```c
Direction GetBestDirection(int cx, int cy, int tx, int ty, Direction cur) {
    // [1] Evaluate UP, LEFT, DOWN, RIGHT (classic arcade priority order)
    // [2] Skip reverse direction — ghosts cannot 180° turn mid-corridor
    // [3] Skip walls via CanMove()
    // [4] Return direction of minimum Euclidean distance to target (tx, ty)
}
```

#### Ghost Personalities

<table>
<tr>
<th width="15%">Ghost</th>
<th width="15%">Color</th>
<th>Targeting Strategy</th>
</tr>
<tr>
<td><b>Blinky</b></td>
<td>Red</td>
<td><b>Direct chase</b> — targets Pac-Man's current tile</td>
</tr>
<tr>
<td><b>Pinky</b></td>
<td>Pink</td>
<td><b>Ambush</b> — targets 4 tiles ahead of Pac-Man's direction<br/><i>(includes overflow bug recreation)</i></td>
</tr>
<tr>
<td><b>Inky</b></td>
<td>Cyan</td>
<td><b>Flanker</b> — mirrors a vector from Blinky through a pivot<br/>2 tiles ahead of Pac-Man</td>
</tr>
<tr>
<td><b>Clyde</b></td>
<td>Orange</td>
<td><b>Shy</b> — chases when >4 tiles away;<br/>retreats to scatter corner when close</td>
</tr>
</table>

#### Mode Cycle

```
SCATTER (7s) → CHASE (20s) → SCATTER (7s) → CHASE ...
                      ↓
              Power Pellet
                      ↓
              FRIGHTENED (6s) → resume prior mode
                      ↓
              EATEN → flies home at 4× speed
                      ↓
              RESPAWNING (3s) → SCATTER
```

---

### `render` — Graphics Pipeline

> All draw calls are isolated here. Game logic never calls Raylib drawing functions directly.

<table>
<tr>
<th width="30%">Element</th>
<th>What Is Drawn</th>
</tr>
<tr>
<td><code>DrawMaze()</code></td>
<td>Walls with double-border effect; dots with glow; pulsing power pellets</td>
</tr>
<tr>
<td><b>Fruit</b></td>
<td>Circle at (13, 17) when active</td>
</tr>
<tr>
<td><b>Ghosts</b></td>
<td>Normal color / dark-blue frightened / flashing warning (&lt;2s) / eyes-only when eaten</td>
</tr>
<tr>
<td><b>Pac-Man</b></td>
<td><code>DrawCircleSector()</code> with animated mouth angle per direction</td>
</tr>
<tr>
<td><b>HUD</b></td>
<td>Score, high score, level number, life icons</td>
</tr>
<tr>
<td><b>Overlays</b></td>
<td>READY! / GAME OVER / title text based on current state</td>
</tr>
</table>

> **Implementation Tip:** The frightened flashing warning uses `(int)(frightenedTimer * 8) % 2` to alternate between white and dark blue — no extra timer variable needed!

---

### `resources` — Asset Management

> Loads all assets once at startup; unloads all on exit. No streaming needed.

<table>
<tr>
<th width="30%">Asset</th>
<th>Usage</th>
</tr>
<tr>
<td><code>soundChomp</code></td>
<td>Dot eating</td>
</tr>
<tr>
<td><code>soundDeath</code></td>
<td>Pac-Man death sequence</td>
</tr>
<tr>
<td><code>soundEatGhost</code></td>
<td>Ghost eaten — plays during combo</td>
</tr>
<tr>
<td><code>soundEatFruit</code></td>
<td>Fruit collected</td>
</tr>
<tr>
<td><code>soundIntro</code></td>
<td>Title screen music</td>
</tr>
<tr>
<td><code>soundPowerSiren</code></td>
<td>Background chase loop</td>
</tr>
<tr>
<td><code>soundGhostTurnBlue</code></td>
<td>Power pellet activation</td>
</tr>
<tr>
<td><code>soundHighScore</code></td>
<td>New high score celebration</td>
</tr>
</table>

---

## Before & After: The Refactoring

### Ghost AI — Biggest Win

#### Before: 320 Lines of Duplicated Code

```c
// Repeated almost identically for ghost0, ghost1, ghost2, ghost3
if (ghost0.mode == MODE_CHASE) {
    float bestDist = 999999;
    Direction bestDir = ghost0.direction;
    // check UP    (12 lines) ⎤
    // check LEFT  (12 lines) ⎥ Duplicated
    // check DOWN  (12 lines) ⎥ 4 times!
    // check RIGHT (12 lines) ⎦
    ghost0.direction = bestDir;
}
// ... exact same block for ghost1, ghost2, ghost3
```

#### After: 35 Lines in One Reusable Function

```c
Direction GetBestDirection(int cx, int cy, int tx, int ty, Direction cur) {
    Direction dirs[4] = { DIR_UP, DIR_LEFT, DIR_DOWN, DIR_RIGHT };
    float bestDist = 999999.0f;
    Direction bestDir = cur;
    
    for (int i = 0; i < 4; i++) {
        if (dirs[i] == opposite[cur]) continue;
        if (!CanMove(cx, cy, dirs[i])) continue;
        // compute distance, update bestDir
    }
    return bestDir;
}
// Called once per ghost in a loop — a bug fix now fixes ALL ghosts
```

#### Comparison Table

| Metric | Before | After | Result |
|--------|--------|-------|--------|
| **Lines of code** | 320 (4 × 80) | 35 (1 function) | 90% reduction |
| **To fix a bug** | 4 separate edits | 1 edit | 4× faster |
| **To add 5th ghost** | ~2 hours | ~15 minutes | 8× faster |
| **Code duplication** | 100% | 0% | Eliminated |

---

### State Management

#### Before: Magic Numbers

```c
int gameState = 0;  // 0=menu? 1=playing? 2=dead? nobody knows
if (gameState == 2) { /* ... */ }
```

#### After: Self-Documenting Enum

```c
typedef enum { 
    STATE_INTRO, STATE_READY, STATE_PLAYING,
    STATE_DEATH, STATE_LEVEL_COMPLETE, STATE_GAME_OVER 
} GameState;

if (game.state == STATE_PLAYING) { /* crystal clear intent */ }
```

---

## Design Decisions

<table>
<tr>
<th width="30%">Decision</th>
<th>Rationale</th>
</tr>
<tr>
<td><b>Global Game Struct</b></td>
<td>Using <code>extern Game game</code> avoids threading <code>Game*</code> through every function. Idiomatic in single-instance C games. Revisit for multiple instances.</td>
</tr>
<tr>
<td><b>Entity Composition</b></td>
<td>C has no inheritance. <code>Ghost</code> embeds <code>Entity</code> by value; ghost code accesses <code>ghost->entity.x</code> and shared helpers accept <code>Entity*</code>.</td>
</tr>
<tr>
<td><b>Dual-Coordinate Movement</b></td>
<td>Pixel coords <code>(x, y)</code> for smooth motion; tile coords <code>(tileX, tileY)</code> for fast collision lookups. Turns only apply on grid alignment.</td>
</tr>
<tr>
<td><b>Delta-Time Timers</b></td>
<td>All timers use <code>GetFrameTime()</code>. Reliable at 60 FPS. A fixed-timestep accumulator would be more robust on slow hardware.</td>
</tr>
<tr>
<td><b>Load-All-at-Startup</b></td>
<td>9 sounds + 1 texture fit in memory. No streaming needed at this scale.</td>
</tr>
</table>

---

## Human vs AI — Who Built What

> This project was built collaboratively. Some parts were written entirely by hand because they required domain judgment. Others were drafted with AI assistance and then reviewed, tested, and integrated manually.

### Built Manually

These parts needed human decisions that could not be delegated.

#### The Maze Layout (`mazeTemplate` in `map.c`)

The entire 28×31 tile grid was **hand-authored row by row**. Replicating the original arcade layout accurately required counting tiles, verifying symmetry, placing the ghost house at the right coordinates, and positioning the four power pellets at exact corners. A wrong `1` anywhere breaks pathfinding or seals off corridors.

```c
// Hand-crafted — every row checked against the original arcade layout
int mazeTemplate[MAZE_HEIGHT][MAZE_WIDTH] = {
    {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    {1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,2,2,2,2,1},
    {1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1},
    {1,3,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,3,1},
    // ... 27 more rows, all written and verified by hand
};
```

#### Ghost Scatter Corner Assignments

Each ghost's home corner `(scatterX, scatterY)` was set by reading the original Pac-Man design documents and matching tile coordinates to the layout. This required understanding **why** the corners are asymmetric:
- Blinky & Pinky → scatter to the **top**
- Inky & Clyde → scatter to the **bottom**

#### The Pinky/Inky Overflow Bug Recreation

The original arcade had a famous overflow bug: when Pac-Man faces **UP**, Pinky's target is 4 tiles ahead **AND** 4 tiles to the left due to an integer overflow in the original 6502 code. We deliberately reproduced this because removing it makes the game feel wrong to anyone who grew up with it.

```c
// Intentional bug recreation — matches original arcade behavior
if (game.pacman.direction == DIR_UP) {
    targetY -= 4;
    targetX -= 4;  // ← the overflow "bug" — not a mistake
}
```

#### Module Boundary Decisions

Deciding which functions belong in `map` vs `pacman` vs `ghost` was a design conversation. For example:
- `CanPacmanMove` lives in **`pacman.c`** (not `map.c`)
- **Why?** It encodes Pac-Man-specific rules (he cannot enter the ghost house) that the map module should not know about

#### Audio Trigger Logic

Deciding **when** to play each sound:
- When to stop the siren
- When ghost-eaten audio overrides the power-pellet loop
- How to avoid overlapping death and siren sounds

All involved playtesting and adjusting by ear.

---

### Built with AI Assistance

These parts were drafted using AI and then reviewed, tested, and adjusted.

#### Ghost Rendering (`render.c`)

Drawing each ghost — circular head, rectangular body, fringe bumps, eyes, pupils, and frightened face — involves significant coordinate arithmetic. The initial implementation was AI-generated, then manually tweaked.

<details>
<summary><b>Prompt used</b></summary>

```
I'm building a Pac-Man clone in C using Raylib. I need a function that draws
a single ghost at pixel position (x, y) with TILE_SIZE=24. The ghost should
have: a circular top half, a rectangular lower body, 3 small semicircles along
the bottom edge as a fringe, and two eyes with pupils. If the ghost is in
MODE_FRIGHTENED, draw it dark blue with a wavy mouth and small pink pupils
instead. Use only Raylib primitive draw calls — no textures.
```
</details>

#### Pac-Man Mouth Animation (`render.c`)

The `DrawCircleSector()` approach — rotating the start angle based on direction and animating the opening angle with a frame counter — was AI-suggested. The multiplier and angle range were adjusted manually.

<details>
<summary><b>Prompt used</b></summary>

```
In Raylib C, how do I draw an animated Pac-Man using DrawCircleSector()?
The mouth should open and close. The character faces different directions
(up/down/left/right) based on its current Direction enum value.
Show me how to rotate the sector and animate the mouth angle.
```
</details>

#### Power Pellet Pulse Effect (`render.c`)

The glowing pulse uses `sinf(GetTime() * 8.0f)` to oscillate the radius. The formula and layered glow + core circle approach were AI-generated.

<details>
<summary><b>Prompt used</b></summary>

```
I want a power pellet in my Pac-Man game to visually pulse — growing and
shrinking smoothly. I'm using Raylib in C. How do I use sinf and GetTime()
to animate the radius of a DrawCircle call? I want a soft outer glow and
a solid inner core, both pulsing together.
```
</details>

#### Rainbow High Score Mode (`render.c`)

`GetRainbowColor()` cycles hue over time using `sinf` with phase offsets per channel. The function skeleton and phase offsets were AI-generated. Cycling speed was chosen manually.

<details>
<summary><b>Prompt used</b></summary>

```
Write a C function GetRainbowColor(float time) that returns a Raylib Color
cycling smoothly through the full hue spectrum over time. Use sinf with
appropriate phase offsets for R, G, and B channels. No HSV conversion —
just sin math.
```
</details>

#### The Refactoring Plan

After the monolithic version was working, we described the structure to an AI and asked for a refactoring plan. The module boundaries, `extern Game game` singleton pattern, and `Entity` base struct were AI recommendations that we evaluated and accepted. All code movement and testing was done manually.

<details>
<summary><b>Prompt used</b></summary>

```
I have a working Pac-Man game in a single main.c file (~500 lines). It has
one global Game struct, 4 ghosts with duplicated AI logic, all rendering
inline in the game loop, and no separate modules. I want to refactor this
into clean separate .h/.c files. Suggest a module breakdown with clear
responsibilities for each file, and explain what should be in types.h vs
individual modules.
```
</details>

---

### Attribution Summary

<table>
<tr>
<th width="35%">Area</th>
<th width="20%">Who</th>
<th>Notes</th>
</tr>
<tr>
<td>Maze tile layout (28×31 grid)</td>
<td>Manual</td>
<td>Verified vs arcade original</td>
</tr>
<tr>
<td>Pinky/Inky overflow bug</td>
<td>Manual</td>
<td>Deliberate design decision</td>
</tr>
<tr>
<td>Ghost scatter corners</td>
<td>Manual</td>
<td>From original design docs</td>
</tr>
<tr>
<td>Module boundary decisions</td>
<td>Manual</td>
<td>Architecture discussion</td>
</tr>
<tr>
<td>Audio trigger sequencing</td>
<td>Manual</td>
<td>Tuned by playtesting</td>
</tr>
<tr>
<td>Ghost rendering geometry</td>
<td>AI-assisted</td>
<td>Eye positioning tweaked</td>
</tr>
<tr>
<td>Pac-Man mouth animation</td>
<td>AI-assisted</td>
<td>Angle calc suggested by AI</td>
</tr>
<tr>
<td>Power pellet pulse effect</td>
<td>AI-assisted</td>
<td>sinf formula AI-generated</td>
</tr>
<tr>
<td>Rainbow high score mode</td>
<td>AI-assisted</td>
<td>Phase offsets AI-generated</td>
</tr>
<tr>
<td>Refactoring plan & layout</td>
<td>AI-assisted</td>
<td>Plan accepted; exec manual</td>
</tr>
<tr>
<td>Integration, testing, bugs</td>
<td>Manual</td>
<td>Nothing ships unreviewed</td>
</tr>
</table>

---

## Code Style Conventions

<table>
<tr>
<th width="20%">Element</th>
<th width="25%">Convention</th>
<th>Example</th>
</tr>
<tr>
<td><b>Functions</b></td>
<td>Verb prefix</td>
<td><code>InitGame()</code>, <code>UpdatePacman()</code>, <code>DrawMaze()</code>, <code>CanMove()</code></td>
</tr>
<tr>
<td><b>Constants</b></td>
<td>SCREAMING_SNAKE</td>
<td><code>SCREEN_WIDTH</code>, <code>TILE_SIZE</code>, <code>GHOST_COUNT</code></td>
</tr>
<tr>
<td><b>Enums</b></td>
<td><code>PREFIX_DESCRIPTOR</code></td>
<td><code>STATE_PLAYING</code>, <code>MODE_SCATTER</code>, <code>DIR_LEFT</code></td>
</tr>
<tr>
<td><b>Structs</b></td>
<td>PascalCase</td>
<td><code>Entity</code>, <code>Ghost</code>, <code>Game</code></td>
</tr>
<tr>
<td><b>Members</b></td>
<td>camelCase</td>
<td><code>tileX</code>, <code>direction</code>, <code>powerPelletTimer</code></td>
</tr>
<tr>
<td><b>Globals</b></td>
<td>lowercase descriptive</td>
<td><code>game</code>, <code>soundChomp</code>, <code>spritesheet</code></td>
</tr>
</table>

---

## Limitations and Known Issues

<table>
<tr>
<th width="30%">Issue</th>
<th>Detail</th>
</tr>
<tr>
<td><b>Pathfinding Approach</b></td>
<td>Greedy best-first with no lookahead. Accurately reproduces arcade behavior but ghosts can be led into suboptimal loops in certain configurations.</td>
</tr>
<tr>
<td><b>No Disk Persistence</b></td>
<td>High score lives in memory only; lost on close. <code>SaveFileData()</code> is planned but not yet implemented.</td>
</tr>
<tr>
<td><b>Fixed-Timestep Fragility</b></td>
<td><code>GetFrameTime()</code> delta-time works at 60 FPS but stretches on slow hardware. A fixed-timestep accumulator would fully decouple simulation from frame rate.</td>
</tr>
<tr>
<td><b>No Sprite Rendering</b></td>
<td>All entities use Raylib primitives. <code>spritesheet.png</code> is loaded and infrastructure is ready, but the render module does not yet use it.</td>
</tr>
<tr>
<td><b>Single-Player Only</b></td>
<td>One <code>Game</code> instance. No multiplayer, no second player.</td>
</tr>
<tr>
<td><b>Windows-Primary Build</b></td>
<td>Build instructions target Windows. Linux and macOS have not been formally tested.</td>
</tr>
<tr>
<td><b>No Unit Test Suite</b></td>
<td>Logic functions are structured to be testable, but no test harness exists in the repository.</td>
</tr>
</table>

---

## Future Enhancements

### Short-term (< 1 day)

- Unit tests for pure functions (`GetBestDirection`, `CanMove`)
- Replace shape drawing with spritesheet animations
- Persist high score to disk with `SaveFileData()`
- Particle effects on ghost eaten / dot collection

### Medium-term (1 week)

- Level editor with GUI for custom mazes
- AI difficulty presets (ghost speed, frightened duration)
- Top-10 leaderboard with player names
- `config.json` for tile size, speeds, and timer values

### Architectural (2+ weeks)

- Full ECS refactor for 10+ ghost types and power-up variants
- State pattern: replace if-chains with function pointers per state
- Fixed-timestep physics accumulator
- P2P ghost-controlled multiplayer mode

---

## Conclusion

This project successfully delivered a **functionally complete Pac-Man clone** in C using the Raylib library, and demonstrated a full refactoring cycle from a monolithic prototype to a modular, maintainable codebase. The four ghost personalities — including their distinct targeting strategies and the historically accurate overflow bug in Pinky and Inky's targeting — were implemented and verified through playtesting. The game runs at a stable **60 FPS** with a complete gameplay loop covering level progression, lives, scoring, audio, and state management.

### Key Achievements

#### Technical Outcomes

- **90% Code Reduction** in ghost AI through elimination of duplication
- **60% Complexity Reduction** measured by average cyclomatic complexity
- **8 Focused Modules** with clear separation of concerns
- **Stable 60 FPS Performance** with proper game loop architecture

#### Engineering Outcomes

Beyond the game itself, the more significant outcome was the experience of **managing software complexity**. The refactoring reduced average cyclomatic complexity from approximately 25 to 8, eliminated 90% of the duplicated ghost AI code, and produced a structure where individual modules can be read, modified, and reasoned about in isolation. These are **transferable skills** applicable to any moderately sized C codebase, not only games.

#### AI Collaboration

The integration of AI-assisted development was approached **critically**. AI tooling accelerated the drafting of rendering geometry and mathematical animation formulas, but every suggestion was reviewed, tested, and adjusted before integration. The maze layout, ghost targeting corner assignments, module boundary decisions, and audio sequencing were completed manually, as they required domain judgment and iterative playtesting that AI tools cannot substitute for.

> **Professional Principle:** The explicit attribution table in this document reflects the view that **transparency about AI involvement is a professional responsibility**, not an optional disclosure.

### Documented Limitations

The most important limitations — the lack of disk persistence, the absence of a formal test suite, and the fixed-timestep fragility — are **well-understood and documented**. They represent the natural scope boundary of a semester project rather than fundamental design flaws, and each has a clear implementation path described in the Future Enhancements section.

### Portfolio Value

This project serves as a **portfolio-ready example** of:

- **Systems Programming** in C with manual memory management
- **Software Architecture** with clear module boundaries and responsibilities
- **Game Development** implementing classic AI and game loop patterns
- **Professional Documentation** including technical specifications and design decisions
- **Transparent Collaboration** with clear attribution of AI-assisted vs manual work

---

<div align="center">

**Pac-Man · Architecture & Technical Report · v1.0 · February 2026**

*Created by Sneha Shah (240242) & Prerana Gupta (240241)*

---

[![Made with C](https://img.shields.io/badge/Made%20with-C-blue?style=flat-square&logo=c)](https://en.wikipedia.org/wiki/C_(programming_language))
[![Raylib](https://img.shields.io/badge/Engine-Raylib%205.0-red?style=flat-square)](https://www.raylib.com/)
[![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)](LICENSE)

</div>
