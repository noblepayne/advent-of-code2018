(ns advent-of-code2018.d3p0
  (:require [clojure.string :as str]))

(def input-regex #"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)")

(defn parse-line
  "Parse line of input and output a claim, i.e. a map with
  keys of id, xoff, yoff, x y"
  [line]
  (let [[id xoff yoff x y]
        (->> line
             (re-matches input-regex)
             (drop 1)
             (map #(js/parseInt %)))]
    ; claim
    {:id id
     :xoff xoff
     :yoff yoff
     :x x
     :y y}))

(defn parse-input
  "Prase input and produce seq of claims"
  [input]
  (->> input
       str/split-lines
       (map parse-line)))


(defn points-from-claim
  "Calculate grid points specified by a claim."
  [{:keys [:xoff :yoff :x :y]}]
  (for [x (range xoff (+ xoff x))
        y (range yoff (+ yoff y))]
    [x y]))

(defn update-from-claim
  "Produce update from claim. Updates map grid points to claim ids.
  e.g.
  {:id 1, :xoff 4, :yoff 4, :x 1, :y 1} -> {[4 4] #{1}}"
  [claim]
  (->> claim
       points-from-claim
       (map #(vector % #{(:id claim)}))
       (into {})))

(defn build-master-map
  "Aggregate all updates to a master map of all claimed grid points and their
  claiming ids."
  [updates]
  (reduce
    (partial merge-with into)
    {}
    updates))

(defn multiple-ids?
  "Predicate to check if a grid point has multiple claims."
  [[pos ids]]
  (<= 2 (count ids)))

(defn solve
  "Count the number of overlapping claims."
  [input]
  (->> input
       parse-input
       (map update-from-claim)
       build-master-map
       (filter multiple-ids?)
       count))
