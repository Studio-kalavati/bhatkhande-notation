(ns bhatkhande.db
  (:require
   [sargam.spec :as us]
   [sargam.talas :as t]
   ))


(defn percentage-95
  [i]
  (let [ iw (js/parseInt i)]
    (- iw (* 0.05 iw))))

(def dispinfo
  {:x 20 :y 30 :under 30
   :x-start 20
   :y-inc 80
   ;:x-end (int (percentage-95 (.-innerWidth js/window)))
   :x-end (.-innerWidth js/window);(int (percentage-95 (.-innerWidth js/window)))
   :y-end (int (percentage-95 (.-innerHeight js/window)))
   :over 30
   :write-part-label true
   :write-comp-label true
   :write-line-separator true

   :kan {:kan-raise 10
         :reduce-font-size 5
         :reduce-spacing 3
         :reduce-octave-size 5}
   :octave 15
   :part-coordinates []
   :part-header-font-size 30
   :comp-label-font-size 35
   :header-y-spacing 50
   :sam-khaali 35
   :debug {:disp-swara false}
   :font-size 20
   :spacing 10 :text-align :left})

(def iseq1 [[{:note [:taar :s]
              :meend-start true}]
            [{:note [:taar :s]
              :meend-end true}]
            [{:note [:taar :r]
              :meend-start true}]
            [{:note [:taar :s]
              :meend-end true}]
            [{:note [:madhyam :-g]
              :meend-start true}]
            [{:note [:madhyam :r]
              :meend-end true}]
            [{:note [:taar :p]
              :meend-start true}]
            [{:note [:taar :-n]
              :meend-end true}]
            [{:note [:madhyam :d]
              :meend-start true}]
            [{:note [:madhyam :s]
              :meend-end true}]
            [{:note [:taar :n]
              :meend-start true}]
            [{:note [:taar :g]
              :meend-end true}]

            [{:note [:madhyam :r]
              :kan [:madhyam :-g]}]
            [{:note [:taar :g]}
             {:note [:taar :r]}
             {:note [:mandra :n]}
             {:note [:madhyam :s]}
             {:note [:madhyam :d]}
             {:note [:madhyam :m]}
             ]
            [{:note [:taar :r]}
             {:note [:mandra :n]}
             {:note [:madhyam :s]}
             {:note [:madhyam :s]}
             {:note [:madhyam :m]}
             ]
            [{:note [:taar :r]}
             {:note [:mandra :n]}
             {:note [:madhyam :s]}
             {:note [:madhyam :s]}
             ]
            [{:note [:madhyam :-g]}
             {:note [:madhyam :m]}]
            [{:note [:madhyam :-]}
             {:note [:madhyam :m]}]
            [{:note [:taar :m+]}]
            [{:note [:taar :r]
              :kan [:taar :-g]}]
            [{:note [:mandra :r]
              :kan [:mandra :-g]}]
            [{:note [:madhyam :s]}]
            [{:note [:taar :r]}]
            [{:note [:mandra :-n]}]
            [{:note [:madhyam :r]
              :kan [:madhyam :-g]}]
            [{:note [:taar :r]}
             {:note [:mandra :n]
              :kan [:madhyam :-g]}
             {:note [:madhyam :s]}]
            [{:note [:madhyam :-g]}
             {:note [:madhyam :m]}]
            [{:note [:madhyam :s]}]
            [{:note [:taar :r]}]
            [{:note [:madhyam :g]}]
            [{:note [:madhyam :g]}]
            [{:note [:madhyam :-]}]
            [{:note [:madhyam :a]}]
            [{:note [:madhyam :g]}]
            [{:note [:madhyam :r] :meend-start true}]
            [{:note [:madhyam :r] :meend-end true}]
            [{:note [:madhyam :-g]}
             {:note [:madhyam :m]}
             {:note [:madhyam :m]}]
            ])

(def antara1 [[{:note [:madhyam :s]}]
              [{:note [:madhyam :r]}]
              [{:note [:mandra :-n]}]
              [{:note [:madhyam :r]
                :kan [:madhyam :s] }]
              [{:note [:madhyam :s]}]
              [{:note [:madhyam :r]}]
              [{:note [:mandra :-n]}]
              [{:note [:madhyam :s]}]
              [{:note [:madhyam :r]}]
              [{:note [:mandra :-n]}]
              ])

(def test1
  {:m-noteseq iseq1
   :taal t/teentaal 
   :part-label "partname"})

(def comp1 {:parts [{:m-noteseq iseq1
                     :part-label "स्थाइ"}
                    {:m-noteseq antara1 :part-label "अन्तरा"}]
            :taal t/teentaal 
            :comp-id "fadaccaa"
            :comp-label "नीर भरन कैसे जाउ"})

(def default-db
  {:init-state {:cursor-color 1}
   :dim (mapv dispinfo [:x-end :y-end])
   :language :english
   :saved-comp comp1
   :saved-part test1
   :dispinfo dispinfo})
