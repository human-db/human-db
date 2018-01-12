(ns humandb.ui.field
  (:require
    [reagent.core :as r]
    [humandb.ui.fields.core :as fields]
    [humandb.ui.fields.misc]))

(defn debounce
  [f ms]
  (let [timeout (atom nil)]
    (fn [& args]
      (js/clearTimeout @timeout)
      (reset! timeout (js/setTimeout (fn []
                                       (apply f args))
                                     ms)))))

(defn field [opts]
  (let [temp-value (r/atom nil)
        on-change (r/atom nil)
        debounced-fn (debounce (fn [value]
                                 (@on-change value))
                               250)]

    (r/create-class
      {:component-did-mount
       (fn [this]
         (reset! temp-value (:value (r/props this)))
         (reset! on-change (:on-change (r/props this))))

       :component-did-update
       (fn [this [_ prev-props]]
         (when (not= (:value prev-props)
                     (:value (r/props this)))
           (reset! temp-value (:value (r/props this))))
         (when (not= (:on-change prev-props)
                     (:on-change (r/props this)))
           (reset! on-change (:on-change (r/props this)))))

       :reagent-render
       (fn [opts]
         (fields/field (-> opts
                           (assoc :on-change (fn [value]
                                               (reset! temp-value value)
                                               (debounced-fn value)))
                           (assoc :value @temp-value))))})))


