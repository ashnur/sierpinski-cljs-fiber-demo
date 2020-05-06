(ns sierpinski.experimental
  (:require [helix.core :refer [defnc $ <> memo]]
            [helix.hooks :as hooks]
            [clojure.string :refer [blank?]]
            [cljs.core.async :refer [take!]]))
(def targetSize 25)
(def container-style
  {:position "absolute"
   :transformOrigin "0 0"
   :left "50%"
   :top "50%"
   :width "10px"
   :height "10px"
   :background "#eee"})

(def dotStyle {:position "absolute"
               :background "#61dafc"
               :font-size "1.3rem"
               :textAlign "center"
               :cursor "pointer"})
(defnc Dot
  [{:keys [size idx x y text]}]
  (let [s (* 1.3 size)
        [hover set-hover] (hooks/use-state false)]
    ($ "div" {:style (merge dotStyle {:width (str s "px")
                                      :height (str s "px")
                                      :left (str x "px")
                                      :top (str y "px")
                                      :borderRadius (str (/ s 2) "px")
                                      :lineHeight (str s "px")
                                      :background (if hover "#ff0" (:background dotStyle))})
              :onMouseEnter #(set-hover true)
              :onMouseLeave #(set-hover false)}
      (if hover "X" text))))

(defnc SierpinskiTriangle
  [{:keys [x, y, s, children]}]
  {:wrap [memo]}
  (if (<= s targetSize)
    ($ Dot {:x (- x (/ targetSize 2))
            :y (- y (/ targetSize 2))
            :size targetSize
            :text children})
    (let [size (/ s 2) 
          e (+ 0.3 (.now js/performance))]
      ; (while (< e (.now js/performance)))  
      ; (prn x y s)
      (<>
        ($ SierpinskiTriangle {:x x :y (- y (/ size 2)) :s size}
          (<> children))
        ($ SierpinskiTriangle {:x (- x size) :y (+ y (/ size 2)) :s size}
          (<> children))
        ($ SierpinskiTriangle {:x (+ x size) :y (+ y (/ size 2)) :s size}
          (<> children))))))
  

(defnc demo
  [{:keys [elapsed]}]
  (let [seconds (mod (/ elapsed 1000) 10) 
        scale (+ 1 (/ (if (> seconds 5) 
                       (- 10 seconds)
                       seconds)
                     10))
        transform {:transform (str "scaleX(" (/ scale 2.1) ") scaleY(0.7) translateZ(0.1px)")}
        lowPriChildren false]
    ($ "div" {:style (merge container-style transform)}
      ($ "div" {:hidden lowPriChildren} 
        ($ SierpinskiTriangle {:x 0 :y 0 :s 1000}
          (str (.floor js/Math seconds)))))))
  

