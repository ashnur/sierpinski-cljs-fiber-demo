(ns sierpinski.core
  (:require [helix.core :refer [$]]
            ["react-dom" :as rdom]
            [sierpinski.experimental :as experimental]))

(def start (.now js/Date))

(def root (atom nil))

(defn step
  []
  (.render @root ($ experimental/demo {:elapsed (- (.now js/Date) start)}))
  (.requestAnimationFrame js/window step)) 


(defn ^:dev/after-load render
  ; FIXME  start this after reload of code not page?
  [])

(defn ^:export main
  ;; FIXME this definitely runs at startup
  "Run application startup logic."
  []
  (when-let [container (.getElementById js/document "demo")]
    (when-not (some? @root)
      (reset! root (rdom/createRoot container))))
  (step)) 
