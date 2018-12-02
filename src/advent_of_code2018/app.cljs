(ns ^:figwheel-hooks advent-of-code2018.app
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   ;; days
   [advent-of-code2018.d0p0 :as d0p0]
   [advent-of-code2018.d1p0 :as d1p0]
   [advent-of-code2018.d1p1 :as d1p1]
   [advent-of-code2018.d2p0 :as d2p0]
   [advent-of-code2018.d2p1 :as d2p1]))

(def days
  {:d0p0 {:prompt "2016 day1 as practice." :solve-fn d0p0/solve}
   :d1p0 {:prompt "2018 day1 placeholder" :solve-fn d1p0/solve}
   :d1p1 {:prompt "2018 day1 part 2" :solve-fn d1p1/solve}
   :d2p0 {:prompt "" :solve-fn d2p0/solve}
   :d2p1 {:prompt "" :solve-fn d2p1/solve}})

(defonce state (atom {:solve-fn d0p0/solve}))

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
    (swap! state assoc :solve-fn #(js/alert (solve-fn (:input @state))))))


(defn main-app []
  [:div
   [:h1 "Advent of Code 2018"]
   [:h3 "Select Day"]
   [:div
     [:select {:id :daypicker :on-change update-solve-fn!}
      (for [k (keys days)]
        ^{:key k} [:option k])]]
   [:div
     [:button {:on-click (:solve-fn @state)} "solve"]
     [:input {:type :file :id "input-file" :on-change load-file!}]]])


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
