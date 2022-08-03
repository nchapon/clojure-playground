(ns nchapon.basics-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [nchapon.basics :as SUT]))

(deftest multiply-test
  (testing "Multiply two numbers."
    (is (= 4
           (SUT/multiply 2 2)))
    (is (= 6
           (SUT/multiply 2 3))))
  (testing "Multiply two decimal numbers"
    (is (= 0.25 (SUT/multiply 0.5 0.5)))))
