(ns advent-of-code2018.d18p0
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (into (sorted-map)
    (for [[y xs] (map-indexed vector (str/split-lines input))
          [x c] (map-indexed vector xs)]
      [[y x] (str c)])))

(defn print-world [world]
  (let [y-max (apply max (map (comp first first) world))
        x-max (apply max (map (comp second first) world))]
    (doseq [y (range 0 (inc y-max))
            :let [xs (range 0 (inc x-max))
                  cs (map #(get world [y %]) xs)]]
      (println (apply str cs)))))

(defn get-neighbors [world [yi xi]]
  (for [y (range (dec yi) (inc (inc yi)))
        x (range (dec xi) (inc (inc xi)))
        :when (not= [y x] [yi xi])
        :let [c (get world [y x])]
        :when c]
    c))

(defn update-cell [world cell-coord]
  "An open acre will become filled with trees if three or more adjacent acres contained trees. Otherwise, nothing happens.
  An acre filled with trees will become a lumberyard if three or more adjacent acres were lumberyards. Otherwise, nothing happens.
  An acre containing a lumberyard will remain a lumberyard if it was adjacent to at least one other lumberyard and at least one acre containing trees. Otherwise, it becomes open."
  (let [c (get world cell-coord)
        neighbors (get-neighbors world cell-coord)]
    (case c
      "." (if (<= 3 (count (filter #{"|"} neighbors)))
            "|"
            ".")
      "|" (if (<= 3 (count (filter #{"#"} neighbors)))
            "#"
            "|")
      "#" (if (and (<= 1 (count (filter #{"#"} neighbors)))
                   (<= 1 (count (filter #{"|"}  neighbors))))
            "#"
            "."))))


(defn update-world [world]
  (reduce (fn [xs x]
            (assoc xs x (update-cell world x)))
          (sorted-map)
          (keys world)))

(defn score-world [world]
  (let [cs (vals world)
        trees (filter #{"|"} cs)
        lumberyards (filter #{"#"} cs)]
    (* (count trees) (count lumberyards))))

(defn solve [input]
  (->> input
       parse-input
       (iterate update-world)
       (drop 10)
       first
       score-world))
