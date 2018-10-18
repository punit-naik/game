(ns game.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [game.core :refer [process]]))

(deftest game-completed-test
  (testing "Game Completed Test ..."
    (is (= (with-in-str "move down\n" (process [0 0] [[1 0] [3 0]] 2 10 [1 0]))
           "Player has reached the destination with a health of 10 points, game completed!"))
    (is (= (with-in-str
             "move down\nmove down\nmove down\n"
             (process [0 0] [[1 0 0 0] [0 0 0 0] [2 0 0 0] [3 0 0 0]] 4 31 [3 0]))
           "Player has reached the destination with a health of 1 points, game completed!"))
    (is (= (with-in-str
             "move right\nmove left\nshow map\nshow surroundings\nmove down\nmove up\nmove down\nmove down\nmove down\n"
             (process [0 0] [[1 0 0 0] [0 0 0 0] [2 0 0 0] [3 0 0 0]] 4 41 [3 0]))
           "Player has reached the destination with a health of 1 points, game completed!"))))

(deftest game-over-test
  (testing "Game Over Test ..."
    (is (= (with-in-str "move down\n" (process [0 0] [[1 0] [2 3]] 2 10 [1 1]))
           "Game over as the player's health is over!"))))
