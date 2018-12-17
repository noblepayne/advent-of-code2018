(ns advent-of-code2018.d13p0
  (:require [clojure.string :as str]))

(defn gen-input [input]
  (for [[y l] (map-indexed vector (str/split-lines input))
        [x c] (map-indexed vector l)]
    [[y x] (str c)]))

(defn parse-input [input]
  (->> (gen-input input)
       (remove #(= " " (second %)))
       (into {})))

(def cart-replacement
 {">" "-"
  "<" "-"
  "^" "|"
  "v" "|"})

(defn init-state [parsed-input]
  (into {}
    (reduce (fn [state [pos c]]
              (let [new-world-char (cart-replacement c)
                    new-state (update-in state [:world pos] (fn [_] new-world-char))
                    new-state (update-in new-state [:carts] #(assoc %1
                                                                pos
                                                                {:dir c
                                                                 :rot :left}))]
                new-state))
            {:world parsed-input
             :carts (sorted-map)
             :crashes []}
            (filter (comp #{">" "<" "^" "v"} second)
                    parsed-input))))

(def walk-dir
  {">" [0 1]
   "<" [0 -1]
   "^" [-1 0]
   "v" [1 0]})

(def new-turn
  {:left :straight
   :straight :right
   :right :left})

(def new-dir
  {["/" ">"] "^"
   ["/" "v"] "<"
   ["/" "^"] ">"
   ["/" "<"] "v"
   ["\\" "<"] "^"
   ["\\" "v"] ">"
   ["\\" "^"] "<"
   ["\\" ">"] "v"})

(def new-dir-intersection
  {[:straight ">"] ">"
   [:straight "<"] "<"
   [:straight "^"] "^"
   [:straight "v"] "v"
   [:left ">"]     "^"
   [:left "<"]     "v"
   [:left "^"]     "<"
   [:left "v"]     ">"
   [:right ">"]    "v"
   [:right "<"]    "^"
   [:right "^"]    ">"
   [:right "v"]    "<"})

(defn move-cart [cleanup? state [pos cart]]
  (let [new-pos (mapv + pos (walk-dir (:dir cart)))
        new-c   (get-in state [:world new-pos])
        crash? (or (contains? (:carts state) new-pos)
                   (= "X" new-c))]
    (if crash?
      (let [new-state
            (if cleanup?
              state
              (update-in state [:world new-pos] (fn [_] "X")))
            new-state
            (update-in new-state [:crashes] #(conj % new-pos))
            new-state
            (update-in new-state [:carts] #(dissoc % pos new-pos))]
        new-state)
      (let [new-cart
            (cond
              (contains? #{"-" "|"} new-c)
              cart
              (contains? #{"/" "\\"} new-c)
              (assoc cart :dir (new-dir [new-c (:dir cart)]))
              (= "+" new-c)
              {:dir (new-dir-intersection [(:rot cart) (:dir cart)])
               :rot (new-turn (:rot cart))})
            new-state
            (update-in state [:carts] #(dissoc % pos))
            new-state
            (update-in new-state [:carts] #(assoc % new-pos new-cart))]
        new-state))))

(defn tick [cleanup? state]
  (reduce (fn [state cart]
            (if (contains? (:carts state) (first cart))
              (move-cart cleanup? state cart)
              state))
          state
          (:carts state)))

(defn print-state [{:keys [:world :crashes :carts]}]
  (let [y-max (apply max (map (comp first first) world))
        x-max (apply max (map (comp second first) world))]
    (doseq [y (range 0 (inc y-max))]
      (let [xs (for [x (range 0 (inc x-max))
                     :let [new-pos [y x]]]
                 (cond
                   (contains? crashes new-pos)
                   "X"
                   (contains? carts new-pos)
                   (:dir (get carts new-pos))
                   :else
                   (get world new-pos " ")))]
        (println (apply str xs))))))

(defn solve [input]
  (->> input
       parse-input
       init-state
       (iterate (partial tick false)) 
       (drop-while #(empty? (:crashes %)))
       first
       :crashes
       first
       reverse
       (str/join ",")))
