(ns human-db.test.core
  (:require
    [clojure.test :refer :all]
    [human-db.test.helpers :refer [with-dir]]
    [human-db.core :as human-db]
    [human-db.processors.yaml]))

(deftest get-record
  (testing "return record"
    (with-dir [dir {"foo.yaml" "id: foo\na: 1"}]
      (is (= {:id "foo"
              :a 1}
             (human-db/get-record {:processor :yaml
                                  :persistor {:type :file-system
                                              :data-path dir}} "foo"))))))

(deftest get-records
  (testing "returns all records"
    (with-dir [dir {"foo.yaml" "id: foo\na: 1"
                    "bar.yaml" "id: bar\na: 2"}]
      (is (= #{{:id "foo"
                :a 1}
               {:id "bar"
                :a 2}} 
             (set (human-db/get-records {:processor :yaml
                                        :persistor {:type :file-system
                                                    :data-path dir}})))))))

(deftest store-record!
  (testing "creates record"
    (with-dir [dir {"foo.yaml" "id: foo\na: 1"}]
      (let [db-config {:processor :yaml
                       :persistor {:type :file-system
                                   :data-path dir}}]
        (human-db/store-record! db-config "bar" {:id "bar"
                                                :a 3})
        (is (= {:id "bar"
                :a 3} 
               (human-db/get-record db-config "bar"))))))

  (testing "overwrites record"
    (with-dir [dir {"foo.yaml" "id: foo\na: 1"}]
      (let [db-config {:processor :yaml
                       :persistor {:type :file-system
                                   :data-path dir}}]
        (human-db/store-record! db-config "foo" {:id "foo"
                                                :a 3})
        (is (= {:id "foo"
                :a 3} 
               (human-db/get-record db-config "foo")))))))

(deftest update-record!
  (testing "creates record"
    (with-dir [dir {"foo.yaml" "id: foo\na: 1"}]
      (let [db-config {:processor :yaml
                       :persistor {:type :file-system
                                   :data-path dir}}]
        (human-db/update-record! db-config "bar" {:id "bar"
                                                 :a 3})
        (is (= {:id "bar"
                :a 3} 
               (human-db/get-record db-config "bar"))))))

  (testing "updates existing record"
    (with-dir [dir {"foo.yaml" "id: foo\na: 1"}]
      (let [db-config {:processor :yaml
                       :persistor {:type :file-system
                                   :data-path dir}}]
        (human-db/update-record! db-config "foo" {:a 3})
        (is (= {:id "foo"
                :a 3} 
               (human-db/get-record db-config "foo")))))))
