(ns game.core
  (:require [clojure.string :refer [trim]]
            [clojure.set :as set]
            [game.utils.core :refer [random-pairs replace-elements sub-2d-coll print-grid]])
  (:gen-class))

(defn- make-map
  "Creates a `n`x`n` map (as a 2D vector) of the game world
   If the value of the 2D vector element :
   a. is -1, then it's the location which has been already visited/traversed
   b. is 0, then it's an accessible/traverseable/unvisited location
   c. is 1, then it's the current position of the player
   d. is 2, then it's the location where the enemy is present
   e. is 3, then it's the location of the destination/exit/finish of the game"
  [n]
  (vec (take n (repeat (vec (take n (repeat 0)))))))

(defn- create-enemies
  "Creates enemies at random locations bounded by `n` (dimension of the map)
   The number of enemies are controlled through `num-enemies`
   Optionally one can specify `disallowed-pairs` which is a set of locations
   where the enemies aren't allowed to be placed"
  ([n num-enemies] (create-enemies n num-enemies #{}))
  ([n num-enemies disallowed-pairs]
   (let [a-set (set/difference (random-pairs n num-enemies) disallowed-pairs)]
     (if (= (count a-set) num-enemies)
       a-set
       (loop [result-set a-set]
         (if (= (count result-set) num-enemies)
           result-set
           (recur
             (set/difference
               (set/union
                 result-set
                 (random-pairs n (- num-enemies (count result-set))))
               disallowed-pairs))))))))

(defn- populate-game-world
  "Populates a game world (map) specified by the arg `world`
   with the entities specified by `what?`
   which will be [enemy, player, destination, unvisited-location, visited-location]
   and `where` which is the location(s)"
  [world [what? where?]]
  (replace-elements
    world
    where?
    (condp = what?
      "visited-location" -1
      "unvisited-location" 0
      "player" 1
      "enemy" 2
      "destination" 3)))

(defn process
  "Recursive logic of the gameplay"
  [player-location gworld dimension health destination]
  (loop [[pli plj :as player-current-location] player-location
         game-world gworld
         h health]
    (if (<= h 0)
      (str "Game over as the player's health is over!")
      (if (= player-current-location destination)
        (str "Player has reached the destination with a health of " h " points, game completed!")
        (let [immediate-surroundings (sub-2d-coll game-world player-current-location)
              enemies-in-the-immediate-surroundings (remove #(not= % 2)
                                                      (list (get-in game-world [pli (inc plj)])
                                                            (get-in game-world [pli (dec plj)])
                                                            (get-in game-world [(dec pli) plj])
                                                            (get-in game-world [(inc pli) plj])))
              health-points-to-be-negated (* 10 (count enemies-in-the-immediate-surroundings))]
          (if (not-empty enemies-in-the-immediate-surroundings)
            (println
              (str "Players health negated by "
                   health-points-to-be-negated
                   " points because enemies are present in the vicinity")))
          (println "Please enter a command for the game to proceed\nRefer to `README.md for all the commands`")
          (let [command (trim (read-line))]
            (cond
              (contains? #{"move up"
                           "move down"
                           "move left"
                           "move right"} command)
              (let [updated-player-location (condp = command
                                              "move up" [(if (neg? (dec pli)) 0 (dec pli)) plj]
                                              "move down" [(if (> (inc pli) (dec dimension)) (dec dimension) (inc pli)) plj]
                                              "move left" [pli (if (neg? (dec plj)) 0 (dec plj))]
                                              "move right" [pli (if (> (inc plj) (dec dimension)) (dec dimension) (inc plj))])]
                (recur
                  updated-player-location
                  (-> game-world
                      (populate-game-world ["visited-location" [player-current-location]])
                      (populate-game-world ["player" [updated-player-location]]))
                  (- h (if (= (get-in game-world updated-player-location) 2)
                         (do
                           (println
                             "Player's health reduced by another 20 points as they chose to move through the enemy")
                           (+ health-points-to-be-negated 20))
                         health-points-to-be-negated))))
              (contains? #{"show map"
                           "show surroundings"} command)
              (do
                (if (= command "show map")
                  (print-grid game-world {-1 "x"})
                  (print-grid immediate-surroundings {1 "p" 2 "e" 3 "d"}))
                (recur
                  player-current-location
                  game-world
                  (- h health-points-to-be-negated)))
              :else (do
                      (println "Invalid command, try again!")
                      (recur
                        player-current-location
                        game-world
                        (- h health-points-to-be-negated))))))))))

(defn- play-game
  "Starts the gameplay loop"
  ([map-size] (play-game map-size 10))
  ([map-size enemy-percentage]
   (let [player-init-location [0 0]
         random-destination (first (random-pairs map-size 1))
         ; Percentage specified by `enemy-percentage` of the entire game world covered by enemies
         random-enemies (create-enemies
                          map-size
                          (int (* map-size (if (> enemy-percentage 100) 1 (/ enemy-percentage 100))))
                          #{player-init-location random-destination})
         initial-game-world (-> (make-map map-size)
                                (populate-game-world ["player" [player-init-location]])
                                (populate-game-world ["destination" [random-destination]])
                                (populate-game-world ["enemy" random-enemies]))]
     (process player-init-location initial-game-world map-size 100 random-destination))))

(defn -main
  [& args]
  (if (= (count args) 1)
    (println (play-game (Integer/parseInt (first args))))
    (println (play-game (Integer/parseInt (first args)) (Integer/parseInt (second args))))))
