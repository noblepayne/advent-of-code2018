(ns advent-of-code2018.d2p0
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (->> input
       str/split-lines))

(defn count-same-letters [string]
  (let [freq (frequencies string)
        lcountset (set (map val freq))]
    {:twos   (if (lcountset 2)
              1
              0)
     :threes (if (lcountset 3)
              1
              0)}))

(defn process-lines [lines]
  (reduce
    (fn [totalcount line]
      (let [linecount (count-same-letters line)]
        (merge-with + totalcount linecount)))
    {:twos 0 :threes 0}
    lines))

(defn checksum [{:keys [:twos :threes]}]
  (* twos threes))

(defn solve [input]
  (-> input
      parse-input
      process-lines
      checksum))
