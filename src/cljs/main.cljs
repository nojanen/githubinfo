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
    (let [owner-repos (r/atom [])] (fn [] 
    (let [get-owner-repos (fn [] (GET (str "/api?org=" owner-name) :handler (fn [rsp] (reset! owner-repos rsp)) 
                                                                   :response-format :json))]
        (get-owner-repos)
        (fn []
            [:div.owner
                [:div owner-name]
                [:div.repoList {:id (str "owner_" owner-name) :align "center"}
                    (doall (for [index (range (count @owner-repos))]
                        ^{:key (str owner-name index)} [repo-data-component (get @owner-repos index)]))]])))))

(defn owners-component []
    (let [get-cached-owners (fn [] (GET "/api" :handler (fn [rsp] (reset! cached-owners rsp))
                                               :response-format :json))]
        (get-cached-owners)
        (fn []
            [:div
            (doall (for [owner-index (range (count @cached-owners))]
                ^{:key (str "owner" owner-index)} [owner-repo-component (get @cached-owners owner-index)]))])))

(defn new-owner-input []
    (let [new-owner (r/atom "")]
        (fn []
            [:div "Cache new org/user:"
                [:input {:type "text" :on-change #(reset! new-owner (-> % .-target .-value))}]
                [:input {:type "button" :value "Add" :on-click #(owner-repo-component @new-owner)}]]
    )))

(defn main-view []
    [:div    
        [new-owner-input]
        [owners-component]])

(defn ^:export run []
    (r/render 
        [main-view]
        (js/document.getElementById "app")))
