# Battleship Android

A classic two-player **Battleship** board game implemented as a native Android mobile application, built with Java and the Android SDK.

## Features

| Feature | Description |
|---------|-------------|
| **Full Gameplay** | Complete Battleship flow — welcome, setup, battle, and results |
| **Multi-Activity** | Separate Activities for each game phase (Welcome, Create, Play, Help, Results) |
| **Network Ready** | Internet permission configured for potential online multiplayer |
| **Custom Icon** | Includes a custom launcher icon and round icon variant |
| **Gradle Build** | Standard Android Gradle build system for easy Studio integration |

## How to Play

1. Launch the app to reach the **Welcome** screen
2. Tap **Create New Game** to set up your ship placements
3. Take turns attacking your opponent's grid in **MainActivity**
4. View win/loss outcomes on the **Results** screen
5. Tap **Help** at any time for game rules and instructions

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest stable)
- Android SDK matching the `compileSdkVersion` in `build.gradle`
- A physical Android device *(USB Debugging enabled)* or an AVD emulator

### Installation

```bash
git clone https://github.com/samadmd786/Battlleship-Android.git
```

1. Open **Android Studio** → **Open an existing project**
2. Navigate to the cloned `Battlleship-Android/` directory
3. Wait for Gradle to sync and resolve dependencies
4. Connect a device or launch an emulator
5. Click **Run ▶** to build and deploy

## Project Structure

| Path | Description |
|------|-------------|
| `app/src/main/java/` | Java source — all Activity classes |
| `app/src/main/res/` | XML layouts, drawables, strings |
| `AndroidManifest.xml` | App permissions and activity declarations |
| `build.gradle` | Project and module-level build config |

## Tech Stack

- **Language:** Java
- **Platform:** Android SDK
- **IDE:** Android Studio
- **Build:** Gradle
