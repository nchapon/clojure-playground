(ns nchapon.scrapbook
  (:require
   [config.core :refer [env]]
   [jdbc-ring-session.cleaner :refer [start-cleaner]]
   [jdbc-ring-session.core :refer [jdbc-store]]
   [migratus.core :as migratus]
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
   [ring.middleware.session-timeout :refer [wrap-idle-session-timeout]]
   [rum.core :refer [defc render-static-markup]])
  (:gen-class))

(defc html-frame []
  [:html
   [:head [:title "A scrapbook"]]
   [:body
    [:h1 {:id "main-headline"} "Wellcome to Scrapebook"]
    [:p {:id "main-content"} "We hope you will like it there"]]])

(defn app-handler
  "Callable entry point to the application."
  [request]
  (let [{session :session} request]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (render-static-markup (html-frame))
     :session (assoc session :username "Nicolas")}))

(defn init-db
  "Init database"
  [db-conf]
  (migratus/migrate {:store :database
                     :migration-dir "migrations"
                     :init-in-transaction? false
                     :migration-table-name "migratus_table"
                     :db db-conf}))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [port (:port env)
        db-conf (:db env)
        session-store (jdbc-store (:db env))
        session-expiry-in-minutes 5]
    (init-db db-conf)
    (start-cleaner db-conf)
    (run-jetty
     (-> app-handler
         (wrap-idle-session-timeout {:timeout (* session-expiry-in-minutes 60)
                                     :timeout-response {:status 200
                                                        :body "Session Timeout"}})
         (wrap-defaults  (-> site-defaults (assoc-in [:session :store] session-store))))
     {:port port})))


