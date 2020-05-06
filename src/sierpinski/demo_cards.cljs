(ns sierpinski.demo-cards
  (:require [devcards.core :as dc :refer [defcard]]
            [helix.core :refer [$]]
            [sierpinski.experimental :refer [demo]])) 

(def state (atom {:elapsed 1}))

(def start (.now js/Date))

(defcard Experimental-Demo
  "Display a sierpenski demo rendering using experimental react"
  (fn [state _]
    ($ demo {& @state}))
  state 
  {:inspect-data true
   :history true
   :frame false
   :heading false}) 

