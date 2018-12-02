(ns advent-of-code2018.d2p1
  (:require [clojure.string :as str]
            [advent-of-code2018.d2p0 :as d2p0]
            [clojure.math.combinatorics :as combo]
            [advent-of-code2018.bk :as bk]))

(defn string-match? [[s1 s2]]
  (let [wrong-count (->> (map not= s1 s2)
                         (filter identity)
                         (count))]
    (if (= 1 wrong-count)
      true
      false)))

(defn find-boxes [box-combos]
  (filter string-match? box-combos))

(defn old-make-boxes [input]
  (combo/combinations input 2))

(defn make-boxes [input]
  (let [tree (bk/create (first input))
        tree (reduce #(bk/insert %1 %2) (rest input))]
    (->> input
         (mapcat
          (fn [boxid]
            (let [res (bk/query tree boxid 1)]
              (map #(set [boxid %2]) res))))
         (filter second)
         (into #{}))))

(defn find-common-letters [[[box1 box2]]]
  (->> (map vector box1 box2)
       (filter #(apply = %))
       (map first)
       (apply str)))

(defn solve [input]
  (->> input
       d2p0/parse-input
       make-boxes
       (map vec)
       find-boxes
       find-common-letters))
