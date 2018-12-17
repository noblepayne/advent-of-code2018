(ns advent-of-code2018.d13p1
  (:require [clojure.string :as str]
            [advent-of-code2018.d13p0 :as d13p0]))

(defn solve [input]
  (->> input
       d13p0/parse-input
       d13p0/init-state
       (iterate (partial d13p0/tick true))
       (drop-while #(< 1 (count (:carts %))))
       first
       :carts
       first
       first
       reverse
       (str/join ",")))
