# Escape-From-Slusher-Tower-

Escape from Slusher Tower is a text based choose your own adventure game.

You pick a starting weapon, then work your way through a series of doors.
Behind each door is a randomly spawned enemy (Witch, Zombie, or Skeleton),
each with its own unique ability. Fight or flee your way through the
required number of encounters to escape the tower.

## How to compile and run

This project has no build tool config; it's meant to be compiled directly
with `javac`/`java` (or opened as a plain Java project in Eclipse).

From the repo root:

```
javac -d out src/*.java player/*.java
java -cp out Game
```

To run the JUnit tests, put `junit-4.13.2.jar`, `hamcrest-core.jar`, and
the course's `student.jar` on the classpath along with the compiled
classes, then run each `*Test` class with `org.junit.runner.JUnitCore`
(or run them from Eclipse's built-in JUnit runner).

## System diagram

```
Game --owns-------------> Player
Game --creates/uses-----> Combat
Game --spawns------------> Enemy (Witch | Zombie | Skeleton)
Player --equips----------> Weapon
Combat --applies damage to-> Player
Combat --applies damage to-> Enemy
Witch, Skeleton, Zombie --extend--> Enemy
```

`Game` runs the input loop (door selection, weapon selection) and cleans
and validates all raw input before it's used. `Combat` resolves a single
fight (fight/flee, hit rolls, damage) using an injected `Random` so it can
be tested deterministically. `Enemy` is an abstract base class; `Witch`,
`Zombie`, and `Skeleton` each override `useAbility()` with their own
unique effect (curse, HP drain, and one-time revival respectively).

## Known limitations

- `Game.spawnEnemy()` returns the same template `Enemy` instance from its
  list rather than a fresh copy per encounter, per the original design
  discussion — an enemy's HP does not currently reset between spawns of
  the same template.
- Doors are cosmetic in the current MVP: every door leads to a random
  encounter rather than a fixed layout.
