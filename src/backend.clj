(ns backend
  (:require [ring.adapter.jetty :as j]
            [hiccup2.core :as h]
            [compojure.core :as cpj]
            [compojure.route :as cpjrt]
            [clojure.pprint :as pp]))

(def db-lessons
  [{:id 0, :title "Basics", :body "Lorem ipsum"}
   {:id 1, :title "map and reduce", :body "Ipsum lorem"}
   {:id 2, :title "Conclusion", :body "Dolore sit amet"}])

(defn get-lesson [id-str]
  (if-let [id (Integer/parseInt id-str)]
    ; first-filter is basicaly sql request
    (first (filter (fn [le] (= id (:id le))) db-lessons))
    nil))

(defn lesson-html [id]
  (if-let [lesson (get-lesson id)]
    (h/html [:div
             [:h1 (:title lesson)]
             [:p (:body lesson)]])
    (h/html [:p "No such lesson"])))

(defn lesson-li [lesson]
  [:li [:a {:href (str "../lesson/" (:id lesson))}
        (:title lesson)]])

(cpj/defroutes app
  (cpj/GET "/" [] (str (h/html [:h "MAIN"])))
  (cpj/GET "/lessons" [] (str (h/html
                               [:div [:h "Lessons"]
                                (into [:ul] (map lesson-li db-lessons))])))
  (cpj/GET "/lesson/:id" [id] (str (lesson-html id)))
  (cpjrt/not-found (str (h/html [:p "404 Not Found"]))))

(defonce server
  (j/run-jetty #'app {:port 3000
                      :join? false}))

; litesql (data base)
; abstract BD in a way you can use atom instead
; protect from SQL injection (honeysql)
; bidi ?
