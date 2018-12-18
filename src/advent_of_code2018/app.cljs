(ns ^:figwheel-hooks advent-of-code2018.app
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   ;; days
   [advent-of-code2018.d0p0 :as d0p0]
   [advent-of-code2018.d1p0 :as d1p0]
   [advent-of-code2018.d1p1 :as d1p1]
   [advent-of-code2018.d2p0 :as d2p0]
   [advent-of-code2018.d2p1 :as d2p1]
   [advent-of-code2018.d3p0 :as d3p0]
   [advent-of-code2018.d3p1 :as d3p1]
   [advent-of-code2018.d4p0 :as d4p0]
   [advent-of-code2018.d4p1 :as d4p1]
   [advent-of-code2018.d5p0 :as d5p0]
   [advent-of-code2018.d5p1 :as d5p1]
   [advent-of-code2018.d6p0 :as d6p0]
   [advent-of-code2018.d7p0 :as d7p0]
   [advent-of-code2018.d10p0 :as d10p0]
   [advent-of-code2018.d11p0 :as d11p0]
   [advent-of-code2018.d12p0 :as d12p0]
   [advent-of-code2018.d13p0 :as d13p0]
   [advent-of-code2018.d13p1 :as d13p1]
   [advent-of-code2018.d17p0 :as d17p0]
   [advent-of-code2018.d18p0 :as d18p0]
   [advent-of-code2018.d18p1 :as d18p1]
   ))

(def days
  {:d0p0 {:prompt "2016 day1 as practice." :solve-fn d0p0/solve}
   :d1p0 {:prompt "2018 day1 placeholder" :solve-fn d1p0/solve}
   :d1p1 {:prompt "2018 day1 part 2" :solve-fn d1p1/solve}
   :d2p0 {:prompt "" :solve-fn d2p0/solve}
   :d2p1 {:prompt "" :solve-fn d2p1/solve}
   :d3p0 {:prompt "" :solve-fn d3p0/solve}
   :d3p1 {:prompt "" :solve-fn d3p1/solve}
   :d4p0 {:prompt "" :solve-fn d4p0/solve}
   :d4p1 {:prompt "" :solve-fn d4p1/solve}
   :d5p0 {:prompt "" :solve-fn d5p0/solve}
   :d5p1 {:prompt "" :solve-fn d5p1/solve}
   :d6p0 {:prompt "" :solve-fn d6p0/solve}
   :d7p0 {:prompt "" :solve-fn d7p0/solve}
   :d10p0 {:prompt "" :solve-fn d10p0/solve}
   :d11p0 {:prompt "" :solve-fn d11p0/solve}
   :d12p0 {:prompt "" :solve-fn d12p0/solve}
   :d13p0 {:prompt "" :solve-fn d13p0/solve}
   :d13p1 {:prompt "" :solve-fn d13p1/solve}
   :d17p0 {:prompt "" :solve-fn d17p0/solve}
   :d18p0 {:prompt "" :solve-fn d18p0/solve}
   :d18p1 {:prompt "" :solve-fn d18p1/solve}
   })

(defonce state (atom {:solve-fn d0p0/solve :answer ""}))

(defn get-app-element []
  (gdom/getElement "app"))

(defn load-file! []
  (let [reader (js/FileReader.)
        el     (gdom/getElement "input-file")
        file   (aget (.-files el) 0)]
    (set! (.-onload reader)
          (fn [event]
            (swap! state assoc :input (-> event .-target .-result))))
    (.readAsText reader file)))

(defn update-solve-fn! [event]
  (let [picked-day (-> event .-target .-value)
        solve-fn (-> days (get (keyword picked-day)) :solve-fn)]
    (swap! state assoc :solve-fn #(swap! state assoc :answer (solve-fn (:input @state))))))


(defn main-app []
  [:div
   [:h1 "Advent of Code 2018"]
   [:h3 "Select Day"]
   [:div
     [:select {:id :daypicker :on-change update-solve-fn!}
      (for [k (keys days)]
        ^{:key k} [:option k])]]
   [:h3 "Select Input"]
   [:div
     [:input {:type :file :id "input-file" :on-change load-file!}]]
     [:br]
     [:button {:on-click (:solve-fn @state)} "solve"]
     [:br]
     [:p "Answer: " (:answer @state)]
   [:p "Find the source: "
    [:a {:href "https://github.com/noblepayne/advent-of-code2018"}
        "https://github.com/noblepayne/advent-of-code2018"]]])


(defn mount [el]
  (reagent/render-component [main-app] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

(mount-app-element)

(defn ^:after-load on-reload []
  (mount-app-element)
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
