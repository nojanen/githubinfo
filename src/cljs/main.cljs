(ns main
    (:require-macros [cljs.core.async.macros :refer [go]])
    (:require [reagent.core :as r]
              [ajax.core :refer [GET POST]]))

(def cached-owners (r/atom []))
(def owner-repos (r/atom []))


(defn handler [response]
    (reset! cached-owners response))

(defn repo-data-component [repo]
    [:ul
        [:li {:class "name"} (get repo "repo_name")]
        [:li [:img {:src "/img/star.svg"}] (get repo "stargazers_count")]
        [:li [:img {:src "/img/watcher.svg"}] (get repo "watchers_count")]
        [:li [:img {:src "/img/fork.svg"}] (get repo "forks_count")]
        [:li [:img {:src "/img/issue.svg"}] (get repo "open_issues_count")]])
    
(defn owners-component []
    (let [get-cached-owners (fn [] (GET "/api?org=nojanen" :handler (fn [rsp] (reset! cached-owners rsp)) :response-format :json))]
        (get-cached-owners)
        (fn []
            [:div {:class "owner"
                   :on-click #(swap! (:style (js/document.getElementById "owner_foo")) "display:block")}
                   ;; "document.getElementById('owner_foo').style='display:block';"}
                [:div "foo"]
                [:div {:class "repoList" :id "owner_foo" :align "center"}
                    (doall (for [index (range (count @cached-owners))]
                        ^{:key index} [repo-data-component (get @cached-owners index)]))]])))

(defn timer-component []
    (let [seconds-elapsed (r/atom 0)]
        (fn []
        (js/setTimeout #(swap! seconds-elapsed inc) 1000)
        [:div
            "Seconds Elapsed: " @seconds-elapsed])))

(defn ^:export run []
    (r/render 
        [owners-component]
        (js/document.getElementById "app")))
