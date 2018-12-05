(ns advent-of-code2018.d4p0
  (:require [clojure.string :as str]
            [clojure.pprint :refer [pprint]]))

(defn get-input [] (:input @advent-of-code2018.app/state))

(def input-regex #"\[(\d+)\-(\d+)\-(\d+) (\d+):(\d+)\] (.+)")

(def guard-regex #"Guard #(\d+).+")

(defn parse-int [string]
  (js/parseInt string))

(defn parse-line [line]
  (let [[year month day hour minute action]
        (rest (re-matches input-regex line))
        actionkey (case action
                   "falls asleep" :sleep
                   "wakes up"     :wake
                   :guard)
        output {:year (parse-int year) :month (parse-int month) :day (parse-int day)
                :hour (parse-int hour) :minute (parse-int minute)
                :action action :actionkey actionkey}]
    (if (= actionkey :guard)
      (let [guard-id (second (re-matches guard-regex action))]
        (assoc output :guard-id (parse-int guard-id)))
      output)))

(defn parse-input
  "Parse input"
  [input]
  (->> input
       (str/split-lines)
       sort
       (map parse-line)))

;; (defn process-guard-ids [log]
;;   (:output
;;     (reduce
;;       (fn [xs x]
;;         (if-let [gid (:guard-id x)]
;;           (assoc xs :guard-id gid)
;;           (assoc xs
;;                  :output
;;                  (conj (:output xs)
;;                        (assoc x :guard-id (:guard-id xs))))))
;;       {:output []}
;;       log)))

(defn process-guard-ids [log]
  (->> log
       (partition-by :guard-id)
       (partition 2)
       (map (fn [[{:keys [:guard-id]} & other-entries]]
              (map #(assoc % :guard-id guard-id)
                   other-entries)))))

(defn process-entry [[sleep wake]]
  {(:guard-id sleep)
   (range (:minute sleep) (:minute wake))})

(defn process-log [entries]
  (reduce
    (partial merge-with into)
    {}
    entries))

(defn process-input [input]
  (->> input
       parse-input
       process-guard-ids
       (partition 2)
       (map process-entry)
       process-log))


(defn get-sleepiest-id [processed-log]
  (first
  (apply max-key
         (comp count val)
         processed-log)))

(defn get-sleepiest-minute [minutes]
  (first
  (apply max-key
         val
         (frequencies minutes))))

(defn solve [input]
  (let [p-input (process-input input)
        gid (get-sleepiest-id p-input)
        min (get-sleepiest-minute (get p-input gid))]
    (* min gid)))

;; p2 needs clean up
(defn get-sleepiest-minute-redux [minutes]
  (second
    (apply max-key
           val
           (frequencies minutes))))

(defn get-sleepiest-id-by-minute [plog]
         (apply max-key
                (comp get-sleepiest-minute-redux val)
                plog))

(defn solve2 [input]
  (let [p-input (process-input input)
        [gid min] (get-sleepiest-id-by-minute p-input)
        sleepymin (get-sleepiest-minute min)]
    (* gid sleepymin)))
