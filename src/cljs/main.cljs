(ns main
    (:require-macros [cljs.core.async.macros :refer [go]])
    (:require [reagent.core :as r]
              [ajax.core :refer [GET POST]]))

(def cached-owners (r/atom []))

(defn repo-data-component [repo]
    [:ul
        [:li {:class "name"} (get repo "repo_name")]
        [:li [:img {:src "/img/star.svg"}] (get repo "stargazers_count")]
        [:li [:img {:src "/img/watcher.svg"}] (get repo "watchers_count")]
        [:li [:img {:src "/img/fork.svg"}] (get repo "forks_count")]
        [:li [:img {:src "/img/issue.svg"}] (get repo "open_issues_count")]])

(defn owner-repo-component [owner-name]
    (def owner-repos (r/atom []))
    (let [get-owner-repos (fn [] (GET (str "/api?org=" owner-name) :handler (fn [rsp] (reset! owner-repos rsp)) 
                                                                   :response-format :json))]
        (get-owner-repos)
        (fn []
            [:div {:class "owner"
                :on-click #(swap! (:style (js/document.getElementById "owner_foo")) "display:block")}
                ;; "document.getElementById('owner_foo').style='display:block';"}
                [:div owner-name]
                [:div {:class "repoList" :id "owner_foo" :align "center"}
                    (doall (for [index (range (count @owner-repos))]
                        ^{:key (str owner-name index)} [repo-data-component (get @owner-repos index)]))]])))

(defn owners-component []
    (let [get-cached-owners (fn [] (GET "/api" :handler (fn [rsp] (reset! cached-owners rsp))
                                               :response-format :json))]
        (get-cached-owners)
        (fn []
            [:div
            (doall (for [owner-index (range (count @cached-owners))]
                ^{:key (str "owner" owner-index)} [owner-repo-component (get @cached-owners owner-index)]))])))

(defn main-view []
    [:div "Cache new org/user:"
        [:input {:type "text"}]
        [:input {:type "button" :value "Add" }]
    [owners-component]])

(defn ^:export run []
    (r/render 
        [main-view]
        (js/document.getElementById "app")))
