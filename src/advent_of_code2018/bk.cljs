(ns advent-of-code2018.bk)

(defn distance [s1 s2]
  (->> (map not= s1 s2)
       (filter identity)
       (count)))

(defrecord Node [edges word])

(defn create [root-word]
  (Node. {} root-word))

(defn insert [root word]
  (let [root-word (:word root)
        root-edges (:edges root)
        d (distance word root-word)
        e (get root-edges d)
        new-node (if e
                   (insert e word)
                   (Node. {} word))
        new-edges (assoc root-edges d new-node)]
    (Node. new-edges root-word)))

(defn query
  ([root word difference] (query root word difference {:edges '() :output []}))
  ([root word difference state]
   (let [root-word (:word root)
         old-output (:output state)
         d (distance word root-word)
         bounds (range (- d difference) (inc (+ d difference)))
         edges (map #(get (:edges root) %) bounds)
         new-edges (filter identity (into (:edges state) edges))
         new-output (if (<= d difference)
                      (conj old-output root-word)
                      old-output)
         new-state {:edges (rest new-edges) :output new-output}]
     (if (empty? new-edges)
       new-output
       (recur (first new-edges) word difference new-state)))))
