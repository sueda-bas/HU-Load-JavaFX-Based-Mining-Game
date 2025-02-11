# ⛏️ HU-Load – JavaFX-Based Mining Game

HU-Load is a **JavaFX-based mining game** inspired by the classic **Motherload** game. Players control a **drill machine** to explore underground, mine valuable resources, and earn money while managing **fuel and storage**. The objective is to **mine as much wealth as possible before running out of fuel**.

## 📌 Features
- **Drill Machine Mechanics**:
  - Moves **left, right, and down** using **arrow keys**.
  - **Consumes fuel** while drilling.
  - **Collects valuable minerals** with weight & monetary value.
  - **Cannot drill upwards**, but can fly up in empty spaces.
  - Falls due to **gravity** if no block is below.
- **Underground Terrain Elements**:
  - **Soil (Basic terrain)** – Can be drilled easily.
  - **Valuables (Minerals & Gems)** – Adds **money & weight**.
  - **Boulders (Rocks)** – Blocks movement; cannot be drilled.
  - **Lava** – Causes **instant game over** when touched.
- **Game Over Conditions**:
  - Running out of **fuel**.
  - Drilling into **lava**.
- **Fuel Management**:
  - **Passive fuel consumption** even when idle.
  - Fuel consumption **increases when drilling**.
- **Game UI**:
  - **Real-time fuel bar**.
  - **Money counter**.
  - **Game over screen** when losing.

## 🎮 How It Works
1. The game **starts with the drill machine** at the surface.
2. The player **moves and drills** to collect valuable minerals.
3. The game **updates fuel, money, and storage** dynamically.
4. If fuel runs out or the machine **drills into lava**, the game ends.
5. The **final score (money collected)** is displayed.

## 📂 Assets
- The game **uses pre-provided images**.

## 🚀 Running the Game
Compile and run the game using:
```bash
javac8 Main.java
java8 Main

