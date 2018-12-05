(ns advent-of-code2018.d5p0
  (:require [clojure.string :as str]))

(defn pair? [l1 l2]
  (let [diff (- (.charCodeAt l1) (.charCodeAt l2))]
    (or (= -32 diff) (= 32 diff))))

(defn parse-input [input]
  (str/trim-newline input))

(defn react [polymer]
  (reduce (fn [xs x]
            (let [prev (peek xs)]
              (if (and prev
                       (pair? prev x))
                (pop xs)
                (conj xs x))))
          [(first polymer)]
          (rest polymer)))

(defn count-reduced [polymer]
  (count (react polymer)))

(defn solve [input]
  (count-reduced (parse-input input)))
