(ns advent-of-code2018.d1p1
  (:require [clojure.string :as str]
            [advent-of-code2018.d1p0 :as d1p0]))

(defn solve [input]
  (reduce (fn [{:keys [:sum :seen]} el]
            (let [new-sum (+ el sum)
                  new-seen (conj seen new-sum)]
              (if (seen new-sum)
                (reduced new-sum)
                {:sum new-sum :seen new-seen})))
          {:sum 0 :seen #{}}
          (cycle (d1p0/parse-input input))))
