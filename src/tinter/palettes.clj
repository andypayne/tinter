(ns tinter.palettes
  (:require [clojure.xml :as xml])
  (:use [tinter.core]))


(defn color-palettes-for
  "Given an RGB triple, returns the set of palettes featuring that color from COLOURlovers."
  [ls]
  (let [resp (slurp (str "http://www.colourlovers.com/api/palettes?hex=" (dec-to-hex-str ls)))]
    (map #(map hex-str-to-dec %)
         (map #(map first %)
              (remove nil?
                      (for [x (xml-seq (xml/parse (java.io.ByteArrayInputStream. (.getBytes resp))))]
                        (when (= :colors (:tag x))
                          (for [y (:content x)]
                            (:content y)))))))))

