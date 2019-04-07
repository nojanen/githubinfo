(ns githubinfo.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[githubinfo started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[githubinfo has shut down successfully]=-"))
   :middleware identity})
