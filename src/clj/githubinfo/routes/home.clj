(ns githubinfo.routes.home
  (:require
    [githubinfo.layout :as layout]
    [githubinfo.db.core :as db]
    [clojure.java.io :as io]
    [githubinfo.middleware :as middleware]
    [ring.util.http-response :as response]
    [ring.util.http-response :refer [content-type ok]]
    [clj-http.client :as client]
    [cheshire.core :refer [parse-string generate-string]]))

(defn home-page [request]
  (layout/render request "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page [request]
  (layout/render request "about.html"))

(defn githubinfo-page [request]
  (layout/render request "githubinfo.html"))

(defn github-query [owner]
  (parse-string 
    ((client/get 
      (clojure.string/join ["https://api.github.com/users/" owner "/repos"])
      {:accept :json})
    :body
    "")
    true))

(defn cache-repos [repos]
  (for [repo repos] (db/create-repo! (assoc repo
    :repo_owner (:login (:owner repo))
    :repo_name (:name repo)))))

(defn repo-query [request]
  (def owner (get (:query-params request) "org"))
  (def cached-repos (for [item (db/list-owners)] (:repo_owner item)))
  (if (not (some #{owner} cached-repos))
    (some #{1} (cache-repos (github-query owner))))
  (content-type
    (ok (generate-string (db/get-repos {:repo_owner owner})))
    "application/json"))

(defn org-query [request]
  (content-type
    (ok (generate-string (for [item (db/list-owners)] (:repo_owner item))))
    "application/json"))

(defn api-query [request]
  (if (get (:query-params request) "org") 
    (repo-query request)
    (org-query request)))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get githubinfo-page}]
   ["/about" {:get about-page}]
   ["/api" {:get api-query}]])
   ;; ["/githubinfo" {:get githubinfo-page}]])

