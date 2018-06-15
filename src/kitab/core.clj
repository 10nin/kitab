(ns kitab.core)
(require '[clj-http.client :as client])

(def ^:dynamic *BOOK-LIST* (ref '()))
(defrecord Book [book-name, barcode])

(defn make-book [name, bcd]
  (->Book name, bcd))

;; バーコードから楽天booksで書籍を探す
;; https://books.rakuten.co.jp/search/nm?sitem=9784274067211 的な。
(defn get-book-info-from-rakuten [book]
  (client/get "https://books.rakuten.co.jp/search/nm"
            {:query-params {:sitem (:barcode book)}}))

(defn same-book? [book1, book2]
  (and (= (:book-name book1) (:book-name book2))
       (= (:barcode book1) (:barcode book2))))
          
(defn add-to-book-list [book]
  (dosync (ref-set *BOOK-LIST* (conj @*BOOK-LIST* book))))

(defn find-books-by-title [title]
  (filter #(= title (:book-name %)) @*BOOK-LIST*))

(defn find-books-by-barcode [barcode]
  (filter #(= barcode (:barcode %)) @*BOOK-LIST*))

(defn find-books [book]
  (filter #(= (same-book? book %)) @*BOOK-LIST*))

(defn remove-books-by-title [title]
  (dosync (ref-set *BOOK-LIST* (remove #(= title (:book-name %)) @*BOOK-LIST*))))

(defn remove-books-by-barcode [barcode]
  (dosync (ref-set *BOOK-LIST* (remove #(= barcode (:barcode %)) @*BOOK-LIST*))))

(defn clear-book-list []
  (dosync (ref-set *BOOK-LIST* '())))
