(ns bhatkhande.views
  (:require
   [re-frame.core :as re-frame :refer [dispatch subscribe]]
   [re-com.core :as re-com :refer-macros [handler-fn]]
   [bhatkhande.subs :as subs]
   [reagent.core :as reagent :refer [atom]]
   [quil.core :as q :include-macros true]
   [quil.middleware :as m]
   [bhatkhande.events :as e]
   [bhatkhande.parts :as p]
   [sargam.spec :as us]
   [bhatkhande.db :as db]
   [bhatkhande.events :as ev]
   ))

(defn color-map
  [frame-rate]
  (zipmap (iterate inc 1) (into
                           (vec (repeat (/ frame-rate 2) '(0,0,0)))
                           (vec (repeat (/ frame-rate 2) '(255,255,255))))))
(def cmap (atom 0))

(defn setup
  "takes a function to run on startup"
  ([] (setup false nil))
  ([loop? init-fn]
   (fn []
     (let [_ (q/no-loop)
           {:keys [location-info disp-info] :as imap} (init-fn)
           ]
       @(subscribe [::subs/init-state])))))

(defn viewer-sketch
  "creates an canvas for viewing.
  The third argument displays the part. If an entire composition is passed (the default),
  then p/disp-comp is used. If a part is the first argument, then use p/disp-part"
  ([size-fn dinfo]
   (viewer-sketch size-fn
                  dinfo
                  p/disp-comp))
  ([size-fn dinfo disp-fn]
   (viewer-sketch size-fn
                  dinfo
                  p/disp-comp
                  (fn[part dinfo]
                    (setup false (fn[](let [_ (q/background 255)
                                                   _ (q/fill 0)]
                                               (disp-fn dinfo @part)))))))
  ([size-fn dinfo disp-fn setup-fn]
   (fn [part div-id]
     (q/sketch
      :setup (setup-fn part dinfo)
      :update identity 
      :draw (fn [state]
              state)
      :host div-id
      :middleware [m/fun-mode]
      :size (size-fn)))))


(defn disp-swara-canvas
  " a canvas for viewing swaras on event such as button press . Note that cur-part
  must be a subscription that does not reference local vars such as part-name or composition name."
  [cur-part div-id vfn]
  (fn [cur-part]
    (reagent/create-class
     {:reagent-render
      (fn [] [(keyword (str "canvas#" div-id))])
      :component-did-update (partial (vfn) cur-part div-id) 
      :component-did-mount (partial (vfn) cur-part div-id)})))

(defn main-panel
  []
  (let [view-comp (reagent/atom "part")]
    (fn []
      (let [imap {:width "100%"
                  :height "100%"
                  :position :absolute
                  :top 0
                  :left 0}
            div-id "viewer"]
        [re-com/v-box
         :gap "20px"
         :children
         [[re-com/title
           :label "Example showing a composition annotated with Bhatkhande notation"
           :level :level2]
          [re-com/h-box
           :gap "20px"
           :children [[re-com/h-box :children [(doall (for [c ["comp" "part"]]
                                                        ^{:key c}
                                                        [re-com/radio-button
                                                         :label c :value c
                                                         :model view-comp
                                                         :on-change #(reset! view-comp %)]))]]
                      [re-com/button :label "Switch language"
                       :on-click #(dispatch [::ev/swap-language])]]]
          [:div {:style {:position :relative
                         :width "500px"
                         :height "250px"}}
           [:div {:style imap}
            (if (= "comp" @view-comp)
              [disp-swara-canvas (subscribe [::subs/saved-comp]) div-id
               #(viewer-sketch (constantly @(subscribe [::subs/div-dim :editor]))
                               (assoc @(subscribe [::subs/dispinfo]) :y 30))]

              (let [[_ y1] (p/compute-part-dim @(subscribe [::subs/dispinfo])
                                               @(subscribe [::subs/saved-part]))
                    {:keys [header-y-spacing]} @(subscribe [::subs/dispinfo])
                    [xmax _] @(subscribe [::subs/div-dim :editor])
                    idim [xmax (+ y1 (* 2 header-y-spacing))]]
                [disp-swara-canvas (subscribe [::subs/saved-part]) div-id
                 #(viewer-sketch (constantly idim)
                                 (assoc @(subscribe [::subs/dispinfo]) :y 30)
                                 p/disp-part)]))]]]]))))
