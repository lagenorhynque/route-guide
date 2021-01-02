(ns route-guide.interceptor)

(defn attach-db [db]
  {:name ::attach-db
   :enter
   (fn [context]
     (assoc-in context [:request :db] db))})
