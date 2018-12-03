(ns advent-of-code2018.d3p1
  (:require [clojure.set :as set]
            [advent-of-code2018.d3p0 :as d3p0]))

(defn get-ids
  "Produce set of all claim ids."
  [claims]
  (into #{} (map :id claims)))

(defn get-contaminated-ids
  "Produce set of all contaminated ids, that is any claim that is
  overlapped by another"
  [master-map]
  (->> master-map
       (filter d3p0/multiple-ids?)
       (map second)
       (reduce into)))

(defn solve
  "Find the id of the only non-overlapped claim."
  [input]
  (let [claims (d3p0/parse-input input)
        ids (get-ids claims)
        updates (map d3p0/update-from-claim claims)
        master-map (d3p0/build-master-map updates)
        contaminated-ids (get-contaminated-ids master-map)
        id (first (set/difference ids contaminated-ids))]
    id))
