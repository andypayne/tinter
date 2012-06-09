(ns tinter.core
  (:require [clojure.string :as str]
            [clojure.contrib.generic.math-functions :as mathfn]
            [clojure.math.numeric-tower :as math]))


(def PI 3.141592654)

 
(defn hex-str-to-dec
  "Convert hex RGB triples to decimal."
  [s]
  (map (comp #(Integer/parseInt % 16)
             (partial apply str))
       (partition 2 s)))


(defn dec-to-hex-str
  "Convert decimal RGB triples to hex."
  [ls]
  (reduce #(str % (format "%02X" (int %2))) "" ls))


(defn n255-to-1
  "Convert 0-255 RGB color triples to 0-1."
  [ls]
  (map #(/ % 255.0) ls))


(defn n1-to-255
  "Convert 0-1 RGB color triples to 0-255."
  [ls]
  (map #(* % 255.0) ls))


(defn chroma
  "Given an RGB triple, return the chroma."
  [ls]
  (let [nls (n255-to-1 ls)
        mx (apply max nls)
        mn (apply min nls)]
    (- mx mn)))


(defn hue
  "Given an RGB triple, return the hue (0-360 degrees)."
  [ls]
  (* 60.0
     (let  [nls  (n255-to-1 ls)
            [r g b] nls
            mx (apply max nls)
            c (chroma ls)]
       (cond
         (< c 0.0001) 0  ; TODO
         (= mx r) (mod (/ (- g b) c) 6.0)
         (= mx g) (+ 2.0 (/ (- b r) c))
         (= mx b) (+ 4.0 (/ (- r g) c))))))


(defn intensity
  "Given an RGB triple, return the intensity."
  [ls]
  (let [[r g b] (n255-to-1 ls)]
    (/ (+ r g b) 3)))


(defn hsv-value
  "Given an RGB triple, return the HSV value."
  [ls]
  (apply max (n255-to-1 ls)))


(defn lightness
  "Given an RGB triple, returns the HSL lightness."
  [ls]
  (let [nls  (n255-to-1 ls)
        mx (apply max nls)
        mn (apply min nls)]
  (/ (+ mx mn) 2)))


(defn luma
  "Given an RGB triple, returns the luma."
  [ls]
  (let [[r g b] (n255-to-1 ls)]
  (+ (* 0.3 r) (* 0.59 g) (* 0.11 b))))


(defn hsv-saturation
  "Given an RGB triple, returns the HSV saturation."
  [ls]
  (let [c (chroma ls)]
    (if (< c 0.0001)
      0
      (/ c (hsv-value ls)))))


(defn hsl-saturation
  "Given an RGB triple, returns the HSL saturation."
  [ls]
  (let [c (chroma ls)]
    (if (< c 0.0001)
      0
      (/ c (- 1 (math/abs (- (* 2 (lightness ls)) 1)))))))


(defn rgb-to-hsl
  "Given an RGB triple, returns the HSL triple."
  [ls]
  [(hue ls)
   (hsl-saturation ls)
   (lightness ls)])


(defn rgb-to-hsv
  "Given an RGB tripe, returns the HSV triple."
  [ls]
  [(hue ls)
   (hsv-saturation ls)
   (hsv-value ls)])


(defn hsv-chroma
  "Given an HSV triple, returns the chroma."
  [ls]
  (let [v (nth ls 2)
        s (second ls)]
    (* v s)))


(defn hsl-chroma
  "Given an HSL triple, returns the chroma."
  [ls]
  (let [h (first ls)
        s (second ls)
        l (nth ls 2)]
  (* s (- 1 (math/abs (- (* 2 l) 1))))))


(defn hsv-to-rgb
  "Given an HSV triple, returns the RGB triple."
  [ls]
  (n1-to-255
    (let [h (first ls)
          s (second ls)
          v (nth ls 2)
          h' (/ h 60.0)
          c (hsv-chroma ls)
          x (* c (- 1 (math/abs (- (mod h' 2) 1))))
          m (- v c)]
      (map #(+ m %) (cond
        (nil? h) [0, 0, 0]
        (and (>= h' 0) (< h' 1)) [c x 0]
        (and (>= h' 1) (< h' 2)) [x c 0]
        (and (>= h' 2) (< h' 3)) [0 c x]
        (and (>= h' 3) (< h' 4)) [0 x c]
        (and (>= h' 4) (< h' 5)) [x 0 c]
        (and (>= h' 5) (< h' 6)) [c 0 x])))))
        

(defn hsl-to-rgb
  "Given an HSL triple, returns the RGB triple."
  [ls]
  (n1-to-255
    (let [h (first ls)
          s (second ls)
          l (nth ls 2)
          h' (/ h 60.0)
          c (hsl-chroma ls)
          x (* c (- 1.0 (math/abs (- (mod h' 2) 1.0))))
          m (- l (* 0.5 c))]
      (map #(+ m %) (cond
        (nil? h) [0, 0, 0]
        (and (>= h' 0) (< h' 1)) [c x 0]
        (and (>= h' 1) (< h' 2)) [x c 0]
        (and (>= h' 2) (< h' 3)) [0 c x]
        (and (>= h' 3) (< h' 4)) [0 x c]
        (and (>= h' 4) (< h' 5)) [x 0 c]
        (and (>= h' 5) (< h' 6)) [c 0 x])))))


(defn hsl-complementary-color
  "Given an HSL triple, returns the 180-degree complementary color."
  [[h s l]]
  [(mod (+ h 180.0) 360) s l])


(defn rgb-complementary-color
  "Given an RGB triple, returns the 180-degree complementary color."
  [ls]
  (hsl-to-rgb (hsl-complementary-color (rgb-to-hsl ls))))


(defn rgb-triad-colors
  "Given an RGB triple, returns the remaining two 120-degree colors of the triad."
  [ls]
  (let [[h s l] (rgb-to-hsl ls)]
    [(hsl-to-rgb [(mod (+ h 120.0) 360) s l])
     (hsl-to-rgb [(mod (+ h 240.0) 360) s l])]))


(defn rgb-split-complementary-colors
  "Given an RGB triple, returns the two 30-degree split complementary colors."
  [ls]
  (let [[h s l] (rgb-to-hsl ls)]
    [(hsl-to-rgb [(mod (+ h 150) 360) s l])
     (hsl-to-rgb [(mod (+ h 210) 360) s l])]))


(defn rgb-gradation
  "Given an RGB triple and a number n, returns n lightness gradations with the same hue/saturation."
  [ls n]
  (let [[h s l] (rgb-to-hsl ls)]
    (for [i (range 0.0 1.0 (/ 1.0 n))]
      (hsl-to-rgb [h s i]))))


(defn rgb-rainbow
  "Return a rainbow of RGB colors with the given number of steps."
  [steps]
  (let [step-size (/ 360.0 steps)]
    (for [i (range 0 360.0 step-size)]
      (hsl-to-rgb [i 1.0 0.5]))))


(defn rgb-to-html [ls]
  (format "<div style=\"width:100px;height:100px;background:#%s\"></div>" (dec-to-hex-str ls)))




