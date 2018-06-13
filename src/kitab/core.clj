(ns kitab.core)
(require '[clj-http.client :as client])

(def ^:dynamic *BOOK-LIST* (ref '()))
(defrecord Book [book-name, barcode])

(defn gen-book [name, bcd]
  (->Book name, bcd))

;; バーコードから楽天booksで書籍を探す
;; https://books.rakuten.co.jp/search/nm?sitem=9784274067211 的な。
(defn get-book-info-from-rakuten [bcd]
  (client/get "https://books.rakuten.co.jp/search/nm"
            {:query-params {:sitem bcd}}))

(defn add-to-book-list [book]
  (dosync (ref-set *BOOK-LIST* (conj @*BOOK-LIST* book))))

(defn find-books-by-title [title]
  (filter #(= title (:book-name %)) @*BOOK-LIST*))

(defn remove-book-by-title [title]
  (dosync (ref-set *BOOK-LIST* (remove #(= titl (:book-name %)) @*BOOK-LIST*))))

