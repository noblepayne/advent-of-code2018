(ns advent-of-code2018.d2p1
  (:require [clojure.string :as str]
            [advent-of-code2018.d2p0 :as d2p0]
            [clojure.math.combinatorics :as combo]))

(defn string-match? [[s1 s2]]
  (let [wrong-count (->> (map not= s1 s2)
                         (filter identity)
                         (count))]
    (if (= 1 wrong-count)
      true
      false)))

(defn find-boxes [box-combos]
  (filter string-match? box-combos))

(defn find-common-letters [[[box1 box2]]]
  (->> (map vector box1 box2)
       (filter #(apply = %))
       (map first)
       (apply str)))

(defn solve [input]
  (-> input
      d2p0/parse-input
      (combo/combinations 2)
      find-boxes
      find-common-letters))
