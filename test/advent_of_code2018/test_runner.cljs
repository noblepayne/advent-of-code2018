;; This test runner is intended to be run from the command line
(ns advent-of-code2018.test-runner
  (:require
    ;; require all the namespaces that you want to test
    [advent-of-code2018.app-test]
    [figwheel.main.testing :refer [run-tests-async]]))

(defn -main [& args]
  (run-tests-async 5000))
