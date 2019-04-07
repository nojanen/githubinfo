(ns user
  (:require
    [githubinfo.config :refer [env]]
    [clojure.spec.alpha :as s]
    [expound.alpha :as expound]
    [mount.core :as mount]
    [githubinfo.core :refer [start-app]]
    [githubinfo.db.core]
    [conman.core :as conman]
    [luminus-migrations.core :as migrations]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'githubinfo.core/repl-server))

(defn stop []
  (mount/stop-except #'githubinfo.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn restart-db []
  (mount/stop #'githubinfo.db.core/*db*)
  (mount/start #'githubinfo.db.core/*db*)
  (binding [*ns* 'githubinfo.db.core]
    (conman/bind-connection githubinfo.db.core/*db* "sql/queries.sql")))

(defn reset-db []
  (migrations/migrate ["reset"] (select-keys env [:database-url])))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration [name]
  (migrations/create name (select-keys env [:database-url])))


