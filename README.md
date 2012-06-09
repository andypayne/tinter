# Tinter

Tinter is a simple Clojure library for color conversions and manipulations.

Colors are specified in the following ways:
* RGB triples of max value 255 (ex: [255 127 0])
* HTML-style RGB triples that consist of hex strings (ex: "FF7F00")
* Hue/saturation/lightness (HSL) format
* Hue/saturation/value (HSV) format


## Usage Examples

Convert a 255 RGB triple to a hex string:
```clojure
(dec-to-hex-str '(255 255 255))
    ; ==> "FFFFFF"
```

Convert a hex string to 255 RGB triple list:
```clojure
(hex-str-to-dec "FF7F00")
    ; ==> (255 127 0)
```

Calculate the 360-degree hue value of an RGB triple:
```clojure
(hue [0 0 255])
    ; ==> 240.0
```

Calculate the two triad colors for a given RGB triple:
```clojure
(map #(map int %) (rgb-triad-colors [51 102 153]))
    ; ==> [(153 51 102) (102 153 51)]
```

Calculate the 30-degree split complementary colors for an RGB triple:
```clojure
(rgb-split-complementary-colors [255 0 0])
    ; ==> [(0.0 255.0 127.5) (0.0 127.5 255.0)]
```

Tinter also provides a simple convenience function "rgb-to-html" to display colored blocks. The following code calculates a 10-step lightness gradation for the provided RGB triple, and prints a set of appropriately colored div elements:
```clojure
(println (map rgb-to-html (rgb-gradation [255 0 0] 10)))
    ; ==> (<div style="width:100px;height:100px;background:#000000"></div> <div style="width:100px;height:100px;background:#320000"></div> <div style="width:100px;height:100px;background:#660000"></div> <div style="width:100px;height:100px;background:#990000"></div> <div style="width:100px;height:100px;background:#CC0000"></div> <div style="width:100px;height:100px;background:#FF0000"></div> <div style="width:100px;height:100px;background:#FF3232"></div> <div style="width:100px;height:100px;background:#FF6565"></div> <div style="width:100px;height:100px;background:#FF9898"></div> <div style="width:100px;height:100px;background:#FFCBCB"></div> <div style="width:100px;height:100px;background:#FFFEFE"></div>)
```

<div style="width:100px;height:100px;background:#FF0000"></div>
<div style="width:100px;height:100px;background:#FFBF00"></div>
<div style="width:100px;height:100px;background:#7FFF00"></div>
<div style="width:100px;height:100px;background:#00FF3F"></div>
<div style="width:100px;height:100px;background:#00FFFF"></div>
<div style="width:100px;height:100px;background:#003FFF"></div>
<div style="width:100px;height:100px;background:#7F00FF"></div>
<div style="width:100px;height:100px;background:#FF00BF"></div>


## License

Copyright © 2012 Andy Payne

Distributed under the Eclipse Public License, the same as Clojure.

