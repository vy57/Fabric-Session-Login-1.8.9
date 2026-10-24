# 🪪 Session ID Login Mod

![GitHub Downloads](https://img.shields.io/github/downloads/Majanito/Fabric-Session-Login/total?style=for-the-badge)

A lightweight Fabric client-side mod that adds a GUI to log in using Minecraft session IDs.

> 🔒 No account switching tools, launchers, or file editing required — just paste your session token and you're in.

---

## 🧭 Available Versions

| Minecraft Version | Branch | Link |
|-------------------|--------|------|
| 1.8.9             | `main` | [View Branch](https://github.com/Majanito/Fabric-Session-Login/tree/main) |

---

## ✨ Features

- 🧩 Adds a login screen to input your Minecraft session ID (Bearer token)
- ✅ Displays current login status (`Valid` or `Invalid`) with real-time validation
- 💬 Shows username and session info in the Multiplayer screen
- 🧑‍💼 Built-in screen to **edit your session account**:
  - 🔤 Change Minecraft **username**
  - 🖼️ Change Minecraft **skin** via URL
- 🛡️ Protects your main account: edits only allowed for session logins
- 🎯 Fully client-side — no server-side impact
- 🖥️ Clean UI that blends into the default Minecraft style

---

## 📥 Installation

1. Download the mod `.jar` from [Releases](https://github.com/Majanito/Fabric-Session-Login/releases)
2. Place it in your `.minecraft/mods` folder
3. Launch the game using a Legacy Fabric 1.8.9 profile

> 💡 Requires [Legacy Fabric Loader](https://github.com/Legacy-Fabric) for 1.8.9. Fabric API is not required. The game can run on Java 8; building this project requires Java 17 or newer.

---

## ⚙️ How It Works

- You’ll find a new **Login** button on the **Multiplayer** screen
- Paste your session token and click Login — no restart needed
- An indicator shows whether the session is valid
- A second button opens the **Edit Account** screen
- There, you can safely update your session account’s name or skin

---

## 🛠️ For Developers

- Built for **Minecraft 1.8.9** with Legacy Fabric and Yarn mappings
- Targets Java 8 bytecode
- Uses **Gradle 8.11.1**, **Fabric Loom 1.9**, and the **Legacy Looming** extension

---

## 📸 Screenshots

**Multiplayer Screen**  
![Multiplayer Screen](https://github.com/user-attachments/assets/05d1ba99-01c5-429d-8feb-d0523a807df3)

**Login Screen (Before Session Login)**  
![Login Before](https://github.com/user-attachments/assets/cf19e550-b7fa-4154-a160-0b071c04c4e7)

**Login Screen (After Session Login)**  
![Login After](https://github.com/user-attachments/assets/0922a793-531b-4ccc-a70b-e7a9bd2d40a3)

**Edit Account Screen (On Original Session)**  
![Edit Account Original](https://github.com/user-attachments/assets/4449124d-0cd1-416d-8187-86aef753dd5b)

**Edit Account Screen (On Session ID)**  
![Edit Account Session](https://github.com/user-attachments/assets/1f8cd89f-aa8b-4c6a-b154-46785984e00f)

---

## 🤝 Contributions

Issues and PRs are welcome! Suggestions and improvements are appreciated — feel free to contribute or discuss ideas.
