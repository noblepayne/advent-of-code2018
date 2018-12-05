(ns advent-of-code2018.d5p0
  (:require [clojure.string :as str]))

(defn pair? [l1 l2]
  (let [diff (- (.charCodeAt l1) (.charCodeAt l2))]
    (or (= -32 diff) (= 32 diff))))

(defn fixed-point [f init]
  (let [ans (f init)]
    (if (= ans init)
      ans
      (recur f ans))))

(defn parse-input [input]
  (str/trim-newline input))

(defn reactor [xf]
  (let [prev (volatile! nil)]
    (fn
      ([] (xf))
      ([result] (xf (xf result @prev)))
      ([result input]
       (let [prior @prev]
         (vreset! prev input)
         (cond
           (not prior)         result
           (pair? prior input) (do (vreset! prev nil)
                                    result)
                               :else (xf result prior)))))))

(defn react [polymer]
  (reduce str (eduction reactor polymer)))

(defn count-reduced [polymer]
  (count (fixed-point react polymer)))

(defn solve [input]
  (count-reduced (parse-input input)))
