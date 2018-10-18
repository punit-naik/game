# game

A Text Adventure Game in Clojure

##  How the game is played

Player will be positioned at [0, 0] and will have 100 points for their health at the start of the game

The player can move:
* Up by typing `move up` in the console an pressing enter
* Left by typing `move left` in the console an pressing enter
* Right by typing `move right` in the console an pressing enter
* Down by typing `move down` in the console an pressing enter

The player can request a map of the currently explored locations by typing `show map` in the console and pressing enter
Sample `show map` output:
```
---------------------
|   |   |   |   |   |
---------------------
|   |   |   |   |   |
---------------------
|   |   |   |   |   |
---------------------
| x | x |   |   |   |
---------------------
| x | x |   |   |   |
---------------------
```
Locations marked as `x` are the locations which have been visited.

The player can also view their immediate surroundings (one step in all directions) by typing `show surroundings` in the console and pressing enter
Sample `show surroundings` output:
```
-------------
| e |   |   |
-------------
|   | p |   |
-------------
|   | e | d |
-------------
```
Locations marked as `e` are the locations where the enemies are present
Location marked as `p` is the player's location
Location marked as `d` is the destination
And empty cells are just locations which can be traversed

**NOTE**: The player can never see the entire map of the game world

Once the player is in the vicinity (one step away in all ***possible*** directions of movement) of any enemy, their health will go down by 10 points times the number of enemies present in the vicinity  
If the player moves through an enemy at a specific location, the enemy will be killed but player's health will go down by an additional 20 points

## Usage

### Build

```
./build.sh
```

### Run

```
./run.sh n
```
OR
```
./run.sh n p
```

* `n` (Type: Int) -> dimension of your `n x n` map
* `p` (Type: Int between [0 100]) (optional) -> percentage of enemies to be created/placed in the game world as per the size/dimension of the map (10% by default if you don't specify)

**NOTE**: The game console will exit once the game is completed or over. At that point you will have to use the command(s) given above to play again if you want to.

### Test

```
lein test
```

#### Code Coverage Report

```
|-----------------+---------+---------|
|       Namespace | % Forms | % Lines |
|-----------------+---------+---------|
|       game.core |   57.63 |   63.51 |
| game.utils.core |   77.69 |   92.59 |
|-----------------+---------+---------|
|       ALL FILES |   65.38 |   71.29 |
|-----------------+---------+---------|
```

## API Docs

[game](https://punit-naik.github.io/game)

## License

Copyright © 2018 [Punit Naik](https://github.com/punit-naik)

Distributed under the Eclipse Public License version 1.0
