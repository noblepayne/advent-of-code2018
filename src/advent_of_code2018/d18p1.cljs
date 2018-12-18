(ns advent-of-code2018.d18p1
  (:require [advent-of-code2018.d18p0 :as d18p0]))

(defn solve [input]
  (let [world (d18p0/parse-input input)
        first-chunk (take 500 (iterate d18p0/update-world world))
        hashes     (map-indexed
                     #(vector %1 (hash %2))
                     first-chunk)
        rep-hash   (first
                     (apply max-key
                            val
                            (frequencies
                              (map second hashes))))
        first-rep  (take 2
                     (filter (comp #{rep-hash} second)
                             hashes))
        offset     (first (first first-rep))
        delta      (- (first (second first-rep)) offset)
        remaining  (rem (- 1000000000 offset) delta)
        final-world (first
                      (drop
                        remaining
                        (iterate d18p0/update-world
                                 (first
                                   (drop offset first-chunk)))))]
    (d18p0/score-world final-world)))
