(ns sierpinski.core
  (:require [helix.core :refer [$]]
            ["react" :as react]
            ["react-dom" :as rdom]
            [sierpinski.experimental :as experimental]))

(def start (.now js/Date))

(def root (atom nil))
(def container (atom nil))

(defn- clear-children [] 
  (when-let [root-node @root] 
    (.unmount root-node)
    (when-let [container-node @container]
      (doseq [ch (.-children container-node)]
        (.remove ch)))))
                                

(defn step
  []
  (when-let [root-node @root] (.render root-node ($ (.-StrictMode react) ($ experimental/demo {:elapsed (- (.now js/Date) start)}))))
  (.requestAnimationFrame js/window step)) 


(defn ^:dev/after-load render
  ; FIXME  start this after reload of code not page?
  []
  (clear-children)
  (step))

(defn ^:export main
  ;; FIXME this definitely runs at startup
  "Run application startup logic."
  []
  (reset! container (.getElementById js/document "demo"))
  (reset! root (.createRoot rdom @container))
  (step)) 
