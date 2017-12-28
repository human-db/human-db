(ns humandb.gui.views.pick-styles)

(defn >pick []
  [:>.pick
   {:text-align "center"
    :display "flex"
    :justify-content "center"
    :align-items "center"
    :background "#eee"
    :width "100vw"
    :height "100vh"}

   [:>div
    {:background "white"
     :padding "1em"
     :box-shadow "0 1px 5px 0 rgba(0, 0, 0, 0.1)"}

    [:>h1
     {:font-size "1.5em"
      :font-weight "bold"}]

    [:>.servers
     {:margin "0.5em 0"}

     [:>.server
      {:padding "1em"
       :border "1px solid #ccc"
       :margin-bottom "0.5em"
       :cursor "pointer"}

      [:&:hover
       {:border-color "#666"}]

      [:&:active
       {:border-color "#000"}]

      [:&:last-child
       {:margin-bottom 0}]]]]])
