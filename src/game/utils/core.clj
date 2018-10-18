(ns game.utils.core
  (:require [clojure.set :as set]
            [clojure.string :refer [join]]))

(defn random-pairs
  "Random generation of a set (of size `ttake`)
   of unique pairs of random numbers from 0 to `n`-1"
  [n ttake]
  (set
    (repeatedly ttake #(vector (rand-int n) (rand-int n)))))

(defn replace-elements
  "Replaces the elements of a 2D vector (`coll`) at specific indexes (`indexes`)
   with a particular value (`v`)"
  [coll indexes v]
  (loop [result-coll coll
         i indexes]
    (if (empty? i)
      result-coll
      (recur (assoc-in result-coll (first i) v) (rest i)))))

(defn print-grid
  "Pretty prints a grid (2D collection) specified by `coll`
   Only prints the elements which have value as key in `show-map` as it's value in `show-map`
   and prints the rest of the elements as empty strings, basically masking them
   NOTE: Works only with 2D collection whose number of rows and columns are equal for now"
  [coll show-map]
  (println (join (take (inc (* 4 (count (first coll)))) (repeat "-"))))
  (doseq [row coll]
    (println (str (join (map #(str "| " (get show-map % " ") " ") row)) "|"))
    (println (join (take (inc (* 4 (count (first coll)))) (repeat "-"))))))

(defn sub-2d-coll
  "Gets a small part of the 2D collection (`coll`) from a particular index (`idx`)
   expanding by `n` in all directions
   NOTE: Works only with 2D collection whose number of rows and columns are equal for now"
  ([coll idx] (sub-2d-coll coll idx 1))
  ([coll [i j :as idx] n]
   (let [dimension (count coll)]
     (mapv
       (fn [x]
         (mapv
           (fn [y]
             (get-in coll [x y]))
           (remove #(or (neg? %) (> % (dec dimension))) (concat (reverse (range (dec j) (dec (- j n)) -1))
                                                                (range j (inc (+ j n)))))))
       (remove #(or (neg? %) (> % (dec dimension))) (concat (reverse (rest (range i (dec (- i n)) -1)))
                                                            (range i (inc (+ i n)))))))))
