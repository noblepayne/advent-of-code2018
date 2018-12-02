(ns advent-of-code2018.d1p0
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (->> input
       str/split-lines
       (map #(js/parseInt %))))

(defn solve [input]
  (reduce + (parse-input input)))
