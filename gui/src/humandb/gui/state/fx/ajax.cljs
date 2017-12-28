(ns humandb.gui.state.fx.ajax
  (:require
    [ajax.core :as ajax]
    [cognitect.transit :as transit]
    [re-frame.core :refer [dispatch]]))

(defn ajax-fx
  [{:keys [uri method params body format on-success on-error headers]
    :or {format (ajax/transit-request-format)}}]
  (ajax/ajax-request
    {:uri uri
     :method method
     :body body
     :params params
     :headers headers
     :handler
     (fn [[ok response]]
       (if ok
         (when on-success
           (dispatch [on-success response]))
         (when on-error
           (dispatch [on-error response]))))
     :format format
     :response-format (ajax/transit-response-format
                        {:type :json
                         :reader (transit/reader :json {:handlers {"u" uuid}})})}))
