(ns humandb.gui.views.styles
  (:require 
    [garden.core :as garden]
    [humandb.gui.views.reset-styles :refer [reset-styles]]
    [humandb.gui.views.app-styles :refer [>app]]))

(defn styles-view []
  [:style
   {:type "text/css"
    :dangerouslySetInnerHTML
    {:__html 
     (garden/css 
       (reset-styles)
       [:#app
        (>app)])}}])
