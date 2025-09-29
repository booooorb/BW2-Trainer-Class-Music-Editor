# BW2 Trainer Class Music Editor

Java CLI to patch Pokémon BW2:
- Applies Mr. Cheeze's music assignment patch, found here: https://projectpokemon.org/home/forums/topic/36863-b2w2-mod-different-battle-music-for-each-trainer-class/
- Edits `overlay_0036`, `y9.bin` size field, and the ARM9 trainer-music table (236 × 4; second byte = replaces gender indicator with battle music; from low byte of 0x00 to 0xF8).

## Incompatabilities
- Patches ROM to be smaller than the original size, thus making it incompatible with an actual DS or accurate NDS emulations like melonDS. 
- Works with latest versions of DeSmuME, including X432R.

## Prereqs
- Windows
- **JDK 17+**
- `ndstool` and `blz` on in tools file.

## Build & Run (Windows)
.\build.bat
.\run.bat
