(ns nchapon.fixtures-test
  (:require
   [clojure.test :refer [deftest is testing use-fixtures]]))

(defn my-fixture
  "My Test Fixture"
  [f]
  (println "SetUp before test here")
  (f)
  (println "Teardown after test here"))

(deftest my-test
  (testing "My simple test"
    (is (= 1 1))))

(deftest my-second-test
  (testing "My simple test"
    (is (= 1 1))))

;; wraps each test 
(use-fixtures :each my-fixture)









