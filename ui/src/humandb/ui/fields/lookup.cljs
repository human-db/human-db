(ns humandb.ui.fields.lookup
  (:require
    [reagent.core :as r]
    [humandb.ui.fields.core :refer [field]]))

(defn related-object-view [object]
  [:div.related
   (or (object :name)
       (object :title)
       (object :id))])

(defn related-object-existing-view [id on-find]
  (let [object (r/atom nil)
        on-find (or on-find (fn [id cb] (cb nil)))
        _ (on-find id (fn [result]
                        (reset! object result)))]
    (fn []
      (if @object
        [related-object-view @object]
        [:div id]))))

(defmethod field :multi-lookup
  [_]
  (let [show-search? (r/atom false)
        results (r/atom [])]
    (fn [{:keys [value on-change on-search on-find]}]
      (let [ids (set value)
            on-search (or on-search (fn [query cb] (cb [])))]
        [:div.multi.lookup
         (doall
           (for [id ids]
             ^{:key id}
             [:div.value
              [related-object-existing-view id on-find]
              [:button.remove {:on-click
                               (fn [_]
                                 (on-change (vec (disj ids id))))}]]))
         (if @show-search?
           [:div.search
            [:input {:placeholder "Search"
                     :on-change (fn [e]
                                  (on-search (.. e -target -value)
                                             (fn [rs]
                                               (reset! results rs))))}]
            [:div.results
             (when @results
               (for [result (->> @results
                                 (remove (fn [r]
                                           (contains? ids (r :id)))))]
                 ^{:key (result :id)}
                 [:div.result {:on-click (fn []
                                           (on-change (conj value (result :id)))
                                           (reset! results [])
                                           (reset! show-search? false))}
                  [related-object-view result]]))]
            [:button.cancel {:on-click (fn [_]
                                         (reset! show-search? false))}]]
           [:button.plus {:on-click (fn [_]
                                      (reset! show-search? true))}])]))))


(defmethod field :single-lookup
  [_]
  (let [results (r/atom [])]
    (fn [{:keys [value on-change on-find on-search]}]
      [:div.single.lookup
       (if value
         [:div.value
          [related-object-existing-view value on-find]
          [:button.remove {:on-click
                           (fn [_]
                             (on-change nil))}]]
         [:div.search
          [:input {:placeholder "Search"
                   :on-change (fn [e]
                                (on-search (.. e -target -value)
                                           (fn [rs]
                                             (reset! results rs))))}]
          [:div.results
           (when @results
             (for [result @results]
               ^{:key (result :id)}
               [:div.result {:on-click (fn []
                                         (on-change (result :id))
                                         (reset! results []))}
                [related-object-view result]]))]])])))
