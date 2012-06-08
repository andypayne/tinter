(ns tinter.core-test
  (:use clojure.test
        [clojure.math.numeric-tower :only (round expt)] 
        tinter.core))


(deftest test-hex-str-to-dec
  (testing "Hex string to list of decimals"
    (is (= '(0 0 0) (hex-str-to-dec "000000")))
    (is (= '(255 255 255) (hex-str-to-dec "ffffff")))
    (is (= '(255 255 255) (hex-str-to-dec "FFFFFF")))
    (is (= '(255 127 0) (hex-str-to-dec "FF7F00"))) 
    (is (= '(12 34 56) (hex-str-to-dec "0c2238")))))

(deftest test-dec-list-to-hex-str
         (testing "List of decimals to hex string"
                  (is (= "000000" (dec-to-hex-str '(0 0 0))))
                  (is (= "FFFFFF" (dec-to-hex-str '(255 255 255))))))

(deftest test-hue
         (testing "Hue calculations"
                  (is (= 0.0 (hue [255 0 0])))
                  (is (= 120.0 (hue [0 255 0])))
                  (is (= 240.0 (hue [0 0 255])))
                  (is (= 0.0 (hue [127 127 127])))))

(defn- hack-float= [m n]
  (< (- m n) 0.001))

(deftest test-hsv-saturation
         (testing "HSV saturation"
                  (is (hack-float= 0.316 (hsv-saturation (map #(* 255 %) [0.495 0.493 0.721]))))
                  (is (hack-float= 0.667 (hsv-saturation (map #(* 255 %) [0.750 0.250 0.750]))))
                  (is (= 1.0 (hsv-saturation [255 0 0])))
                  (is (= 0 (hsv-saturation [0 0 0])))
                  (is (= 0 (hsv-saturation [255 255 255])))))

(deftest test-roundtrip-rgb-hsv
          (testing "RGB-to-HSV-to-RGB roundtrip"
                   (is (= [12 34 56] (vec (map int (hsv-to-rgb (rgb-to-hsv [12 34 56]))))))
                   (is (= [51 102 153] (vec (map int (hsv-to-rgb (rgb-to-hsv [51 102 153]))))))
                   (is (= [255 255 255] (vec (map int (hsv-to-rgb (rgb-to-hsv [255 255 255]))))))
                   (is (= [0 0 0] (vec (map int (hsv-to-rgb (rgb-to-hsv [0 0 0]))))))))

(deftest test-roundtrip-rgb-hsl
          (testing "RGB-to-HSL-to-RGB roundtrip"
                   (is (= [12 34 56] (vec (map int (hsl-to-rgb (rgb-to-hsl [12 34 56]))))))
                   (is (= [51 102 153] (vec (map int (hsl-to-rgb (rgb-to-hsl [51 102 153]))))))
                   (is (= [255 255 255] (vec (map int (hsl-to-rgb (rgb-to-hsl [255 255 255]))))))
                   (is (= [0 0 0] (vec (map int (hsl-to-rgb (rgb-to-hsl [0 0 0]))))))))

(deftest test-triad-colors
         (testing "Triad colors"
                  (is (= ['(0.0 255.0 0.0) '(0.0 0.0 255.0)] (rgb-triad-colors [255 0 0])))
                  (is (= ['(153 51 102) '(102 153 51)] (map #(map int %) (rgb-triad-colors [51 102 153]))))))

(deftest test-split-comp-colors
         (testing "Split complementary colors"
                  (is (= ['(0.0 255.0 127.5) '(0.0 127.5 255.0)] (rgb-split-complementary-colors [255 0 0])))))

