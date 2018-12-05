(ns advent-of-code2018.d5p1
  (:require [clojure.string :as str]
            [advent-of-code2018.d5p0 :as d5p0]))

(def all-letters
  (map char (range 65 (+ 65 26))))

(defn same-letter? [l1 l2]
  (= (.toUpperCase l1)
     (.toUpperCase l2)))

(defn remove-letter [letter coll]
  (remove #(same-letter? letter %) coll))

(defn make-removal-map [polymer]
  (into {}
    (for [l all-letters]
      [l (d5p0/count-reduced (remove-letter l polymer))])))

(defn solve [input]
  (->> input
       d5p0/parse-input
       make-removal-map
       (apply min-key val)
       second))
