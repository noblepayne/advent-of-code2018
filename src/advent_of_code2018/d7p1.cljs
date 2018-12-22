(ns advent-of-code2018.d7p1
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [advent-of-code2018.d7p0 :as d7p0]))

(defn sleep-time [letter]
  (- (.charCodeAt letter) 4))

(defn make-init-state [n dep-map]
  {:dep-map dep-map
   :output []
   :nxt (d7p0/pick-next dep-map)
   :t 0
   :free (set (range n))
   :busy {}})

(defn remove-deps [dep-map deps]
  (into {}
    (map (fn [[l ds]] [l (reduce disj ds deps)]) dep-map)))

(defn schedule [{:keys [:dep-map :output :nxt :t :free :busy] :as state}]
  (let [worker (first free)
        new-free (disj free worker)
        work-item {:val nxt :time (+ t (sleep-time nxt))}
        new-busy (assoc busy worker work-item)
        new-map (dissoc dep-map nxt)
        new-nxt (d7p0/pick-next new-map)]
    {:dep-map new-map
     :output output
     :nxt new-nxt
     :t t
     :free new-free
     :busy new-busy}))

(defn tick [{:keys [:dep-map :output :nxt :t :free :busy] :as state}]
  (let [new-t (inc t)
        finished (filter (fn [[id {time :time}]] (= new-t time)) busy)
        finished-ids (keys finished)
        new-out (into output (map :val (vals finished)))
        new-free (into free finished-ids)
        new-busy (reduce dissoc busy finished-ids)
        new-map (remove-deps dep-map new-out)]
    {:dep-map new-map
     :output new-out
     :nxt nxt
     :t new-t
     :free new-free
     :busy new-busy}))

(defn parallel-serialize
  [{:keys [:dep-map :output :nxt :t :free :busy] :as state}]
    (cond
      (and (empty? dep-map)
           (nil? nxt)
           (empty? busy))
        {:output output
         :time t}
      (empty? free)
        (recur (tick state))
      nxt
        (recur (schedule state))
      :else
        (if-let [new-nxt (d7p0/pick-next dep-map)]
          (recur (assoc state :nxt new-nxt))
          (recur (tick state)))))

(defn solve [input]
  (->> input
       d7p0/parse-input
       (make-init-state 5)
       parallel-serialize))
