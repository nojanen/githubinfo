(ns main
    (:require [reagent.core :as r]
              [ajax.core :refer [GET POST]]))

(defn handler [response]
    (.log js/console (str response))
    (reset! cached-owners response))

(def cached-owners (r/atom nil))

(defn owners-component []
    (let [get-cached-owners (fn [] (GET "/api" :handler handler :response-format :json))]
        (get-cached-owners)
        (fn []
            [:div {:class "owner"
                   :on-click #(swap! (:style (js/document.getElementById "owner_foo")) "display:block")}
                   ;;"document.getElementById('owner_foo').style='display:block';"}
                [:div "foo"]
                [:div {:class "repoList" :id "owner_foo" :align "center"}
                    (for [index (range 3)]
                        [repo-data-component {:repo_name (str "Foo" index) 
                                          :stargazers_count 1
                                          :watchers_count 2
                                          :forks_count 3
                                          :open_issues_count 4}])]])))

(defn repo-data-component [repo]
    [:ul {:key (str repo "_stats")}
        [:li {:key (str repo "_name") :class "name"} (:repo_name repo)]
        [:li {:key (str repo "_stars")} [:img {:src "/img/star.svg"}] (:stargazers_count repo)]
        [:li {:key (str repo "_watchers")} [:img {:src "/img/watcher.svg"}] (:watchers_count repo)]
        [:li {:key (str repo "_forks")} [:img {:src "/img/fork.svg"}] (:forks_count repo)]
        [:li {:key (str repo "_issues")} [:img {:src "/img/issue.svg"}] (:open_issues_count repo)]])

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
