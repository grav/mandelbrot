(ns mandelbrot.core
  (:require))

(enable-console-print!)

(println "This text is printed from src/mandelbrot/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )

(defn load! [w h img]
  (let [ctx (-> (js/document.getElementById "canvas")
                (.getContext "2d"))
        coll (->> img
                  (map (fn [c]
                         [(* 255 c) (* 255 c) (* 255 c) 255]))
                  (apply concat))]

    (.putImageData ctx (js/ImageData. (js/Uint8ClampedArray. coll) w h) 0 0)))

(defn mag [[a b]]
  (Math/sqrt (+ (* a a) (* b b))))

(defn c* [[a1 b1] [a2 b2]]
  [(- (* a1 a2) (* b1 b2))
   (+ (* a1 b2) (* a2 b1))])

(defn c+ [[a1 b1] [a2 b2]]
  [(+ a1 a2)
   (+ b1 b2)])

(defn range2 [a b n]
  (let [s (/ (- b a) (dec n))]
    (range a (+ b s) s)))

(defn M [c z]
  (-> (c* z z)
      (c+ c)))

(defn render [n w h]
  (for [y (range2 2 -2 h)
        x (range2 -2 2 w)]
    (let [m (->> (iterate (partial M [x y]) [0 0])
                 (map mag)
                 (take n)
                 (drop-while #(< % 2))
                 (take 1))]
      (if (seq m)
        0.7
        0))))
