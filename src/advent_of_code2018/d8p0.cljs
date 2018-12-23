(ns advent-of-code2018.d8p0
  (:require [clojure.string :as str]
            [clojure.pprint :refer [pprint]]))

(defn str->int [s]
  (js/parseInt s))

(defn parse-input [input]
  (map str->int
       (str/split
         (str/trim-newline input)
         #" ")))

;; example input
:input [1 3 1 1 0 1 99 2 1 1 2]

;; should produce
{:0 {:metadata [1 1 2]
     :children [1]}
 :1 {:metadata [2]
     :children [2]}
 :2 {:metadata [99]
     :children []}}

;; example state evolution
{:0 {:metadata [] :children [] :m-to 3 :c-to 1}}
:to-proc '(:0)
:input [1 1 0 1 99 2 1 1 2]

{:0 {:metadata [] :children [:1] :m-to 3 :c-to 0}
 :1 {:metadata [] :children [] :m-to 1 :c-to 1}}
:to-proc '(:1 :0)
:input [0 1 99 2 1 1 2]

{:0 {:metadata [] :children [:1] :m-to 3 :c-to 0}
 :1 {:metadata [] :children [:2] :m-to 1 :c-to 0}
 :2 {:metadata [] :children [] :m-to 1 :c-to 0}}
:to-proc '(:2 :1 :0)
:input [99 2 1 1 2]

{:0 {:metadata [] :children [:1] :m-to 3 :c-to 0}
 :1 {:metadata [] :children [:2] :m-to 1 :c-to 0}
 :2 {:metadata [99] :children [] :m-to 0 :c-to 0}}
:to-proc '(:1 :0)
:input [2 1 1 2]

{:0 {:metadata [] :children [:1] :m-to 3 :c-to 0}
 :1 {:metadata [2] :children [:2] :m-to 0 :c-to 0}
 :2 {:metadata [99] :children [] :m-to 0 :c-to 0}}
:to-proc '(:0)
:input [1 1 2]

{:0 {:metadata [1 1 2] :children [:1] :m-to 0 :c-to 0}
 :1 {:metadata [2] :children [:2] :m-to 0 :c-to 0}
 :2 {:metadata [99] :children [] :m-to 0 :c-to 0}}
:to-proc []
:input []


(defn prepare-state [input]
  (let [root (random-uuid)
        [n m & new-input] input]
    {:input new-input
     :to-proc (list root)
     :output {root {:meta '() :children '() :m-to m :c-to n}}}))

(defn build-tree [{:keys [:input :to-proc :output] :as state}]
  (let [cur-id (first to-proc)
        {:keys [:meta :children :m-to :c-to] :as current}
        (get output cur-id)]
    (cond
      (empty? to-proc)
        output
      (pos? c-to)
        (let [n-c-to (dec c-to)
              new-id (random-uuid)
              new-childs (conj children new-id)
              new-current (assoc current :children new-childs :c-to n-c-to)
              [c-c-to c-m-to & new-input] input
              new-output (assoc output cur-id new-current
                                       new-id {:meta '() :children '()
                                               :m-to c-m-to :c-to c-c-to})]
          (recur {:input new-input
                  :output new-output
                  :to-proc (conj to-proc new-id)}))
      (pos? m-to)
        (let [new-meta (take m-to input)
              new-input (drop m-to input)]
          (recur {:input new-input
                  :output (assoc output cur-id (assoc current :meta new-meta
                                                              :m-to 0))
                  :to-proc (rest to-proc)})))))

(defn solve [input]
  (->> input
       parse-input
       prepare-state
       build-tree
       vals
       (mapcat :meta)
       (reduce +)))
