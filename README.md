# FinalGame
### Grade 12 Final By Mohamed and Ahmed
Check out [Whimsical](https://whimsical.co/XRnDKKfsQ9CrTStVnGK3qq) for all data structures

 *How to Play*
  - To get a weapon at the start of the game left click on the chest or in the bottom right corner of the screen when in the main room
  - Basically everything that occurs in the game uses the luck stat, highly recommend investing in it
  - Mess around with each book before leaving the safe zone to learn how to use it, the glyph in the book shows what it does
  - Click anywhere to start and close window to restart (didnt have time to make it elegant 👍)
  - If you want to cheat a little, hold L and click the screen, then watch your EXP grow

*Notes*
  - Some of our sprites have pink particles around them. That isn't a bug in the code. When they were exported from photoshop the files were compressed and some of the colors were slightly changed and is now handled differently.
  - Classes that are entire ours: All character classes except collision checker, Spawn, Game except run and render, Particle, Stats
  - Classes that were made with tutorials: Rectangle, all map classes, RenderHandler, all sprite Classes
  - We do however, have _full_ understanding in how each class works (except map classes)
  
  *Bugs*
  - Mobs sometimes spawn outside of the map. I used the randomizer equations we use in class and am unsure why this happens
  - Didnt have time to make the mobs smart, sometimes they'll just run around you without hitting you
  - Sometimes the text in the JMenu glitches, not sure if this is a mac thing or what
  - On mac, not sure about windows, Making the window fullsize mode (different from full screen), causes the map to go black (Grabbing the edges and making it fullscreen still works)
      *Never Mind I think I fixed the spawning issue*
