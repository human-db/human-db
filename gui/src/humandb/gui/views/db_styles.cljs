(ns humandb.gui.views.db-styles)

(defn >db []
  [:>.db
   {:background "#ededed"
    :min-height "100vh"}

   [:>.header
    {:background "#ccc"
     :padding "0.25em"
     :display "flex"
     :justify-content "space-between"}

    [:>.search-bar
     {:width "50%"}]

    [:>.record-counter]]

   [:>.search-results
    {:padding "1em"}

    [:>.record
     {:background "white"
      :box-shadow "0 1px 5px 0 rgba(0, 0, 0, 0.2)"
      :margin-bottom "2em"}

     [:&:last-child
      {:margin-bottom 0}]

     [:>table
      {:width "100%"}

      [:>tbody

       [:>tr

        [:>td
         {:padding "0.25em 0.5em"}

         [:&.key
          {:background "#027998"
           :color "white"
           :white-space "nowrap"
           ; hack to keep column just as wide as content
           :width "1px"}]

         [:&.value]]

        ["&:nth-child(even)"

         [:>td.key
          {:background "#04708c"}]

         [:>td.value
          {:background "#f0f0f0"}]]]]]]]])
