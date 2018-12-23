(ns advent-of-code2018.d8p1
  (:require [clojure.string :as str]
            [advent-of-code2018.d8p0 :as d8p0]))

;; TODO use recur
(defn score-node [tree id]
  (let [{:keys [:children :meta]} (get tree id)]
    (if (empty? children)
      (reduce + meta)
      (let [childs-to-add (filter #(< 0 % (inc (count children))) meta)
            rev-childs (vec (reverse children))]
        (reduce +
                (map #(score-node tree
                                  (nth rev-childs (dec %)))
                     childs-to-add))))))

(defn solve [input]
  (let [prep (d8p0/prepare-state (d8p0/parse-input input))
        root (first (keys (:output prep)))
        tree (d8p0/build-tree prep)]
    (score-node tree root)))
