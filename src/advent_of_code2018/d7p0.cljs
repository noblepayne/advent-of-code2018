(ns advent-of-code2018.d7p0
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def input-regex #"Step (\w) must be finished before step (\w) can begin.")

(defn parse-line [line]
  (let [[dep letter] (rest (re-matches input-regex line))]
    {letter #{dep}}))

(defn make-dep-map [pinput]
  (reduce #(merge-with into %1 %2) {} pinput))

(defn add-missing [dep-map]
  (let [keyset (set (keys dep-map))
        valset (reduce into (vals dep-map))
        missing (set/difference valset keyset)]
    (into dep-map (map vector missing (repeat #{})))))

(defn parse-input [input]
  (->> input
       str/split-lines
       (map parse-line)
       make-dep-map
       add-missing))

(defn pick-next [dep-map]
  (->> dep-map
       (filter (comp empty? second))
       sort
       first
       first))

(defn serialize
  ([dep-map] (serialize dep-map [] (pick-next dep-map)))
  ([dep-map output nxt]
   (if (empty? dep-map)
     output
     (let [new-map (dissoc dep-map nxt)
           new-map (into {} (map (fn [[k v]] [k (disj v nxt)]) new-map))
           new-nxt (pick-next new-map)
           new-output (conj output nxt)]
       (recur new-map new-output new-nxt )))))

(defn solve [input]
  (->> input
       parse-input
       serialize
       (apply str)))
