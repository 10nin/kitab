(ns kitab.core)
(require '[clj-http.client :as client])

(defrecord Book [book-name, barcode])

(defn gen-book [name, bcd]
  (->Book name, bcd))

;; バーコードから楽天booksで書籍を探す
;; https://books.rakuten.co.jp/search/nm?sitem=9784274067211 的な。
(defn get-book-info-from-rakuten [bcd]
  (client/get "https://books.rakuten.co.jp/search/nm"
            {:query-params {:sitem bcd}}))
