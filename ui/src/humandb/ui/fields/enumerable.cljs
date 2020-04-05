(ns humandb.ui.fields.enumerable
  (:require
    [humandb.ui.fields.core :refer [field]]))

(defn enum-select-one
  [{:keys [value disabled options on-change]}]
  (let [lookup (reduce (fn [memo option]
                         (assoc memo (str option) option))
                       {} options)]
    [:select {:value (str value)
              :disabled disabled
              :on-change (fn [e]
                           (on-change (lookup (.. e -target -value))))}
     (for [option options]
       ^{:key (str option)}
       [:option {:value option} (str option)])]))

(defn enum-list-one
  [{:keys [value disabled options on-change]}]
  [:div
   (for [option options]
     ^{:key (str option)}
     [:label
      [:input {:type "radio"
               :checked (= value option)
               :on-change (fn [e]
                            (on-change option))}]
      (str option)])])

(defmethod field :enum
  [{:keys [value disabled options on-change] :as config}]
  (if (< (count options) 10)
    [enum-list-one config]
    [enum-select-one config]))
