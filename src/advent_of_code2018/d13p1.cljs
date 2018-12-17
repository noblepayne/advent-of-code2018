(ns advent-of-code2018.d13p1
  (:require [clojure.string :as str]
            [advent-of-code2018.d13p0 :as d13p0]))

(defn move-cart [state [pos cart]]
  (let [new-pos (mapv + pos (d13p0/walk-dir (:dir cart)))
        new-c   (get-in state [:world new-pos])
        crash? (or (contains? (:carts state) new-pos))]
    (if crash?
      (let [new-state
            (update-in state [:crashes] #(conj % new-pos))
            new-state
            (update-in new-state [:carts] #(dissoc % pos new-pos))]
        new-state)
      (let [new-cart
            (cond
              (contains? #{"-" "|"} new-c)
              cart
              (contains? #{"/" "\\"} new-c)
              (assoc cart :dir (d13p0/new-dir [new-c (:dir cart)]))
              (= "+" new-c)
              {:dir (d13p0/new-dir-intersection [(:rot cart) (:dir cart)])
               :rot (d13p0/new-turn (:rot cart))})
            new-state
            (update-in state [:carts] #(dissoc % pos))
            new-state
            (update-in new-state [:carts] #(assoc % new-pos new-cart))]
        new-state))))

(defn tick [state]
  (reduce (fn [state cart]
            (if (contains? (:carts state) (first cart))
              (move-cart state cart)
              state))
          state
          (:carts state)))

(defn solve [input]
  (->> input
       d13p0/parse-input
       d13p0/init-state
       (iterate tick)
       (drop-while #(< 1 (count (:carts %))))
       first
       :carts
       first
       first
       reverse
       (str/join ",")))
