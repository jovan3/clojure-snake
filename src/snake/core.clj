(ns snake.core
  (:gen-class))

(require '[lanterna.screen :as s])

(def scr (s/get-screen :swing))

(defn zero-vector [size]
    (for [x (range size)] 0))

(def board-size 10) ;; square board for simplicity

(def up [-1 0])
(def down [1 0])
(def left [0 -1])
(def right [0 1])

(def board 
  (vec (for [x (range board-size)]
         (into [] (zero-vector board-size)))))

(defn print-board [board]
  (dotimes [row board-size]
    (s/put-string scr 0 row (clojure.string/join " " (nth board row)))) 
  (s/redraw scr))

(defn +mod [x y]
  (map #(mod (+ %1 %2) board-size) x y))

(defn move-snake [snake direction]
 (vec (cons 
        (into [] (+mod (first snake) direction))
        (pop snake))))

(defn board-with-snake [board snake]
    (loop [piece 0
           b board]
        (if (>= piece (count snake))
         b
         (recur (inc piece) (assoc-in b (nth snake piece) "*")))))
    
(defn board-with-apple [board apple]
    (assoc-in board apple "X"))

(defn new-direction [key old-direction]
  (cond
    (= key :up) up
    (= key :down) down
    (= key :left) left
    (= key :right) right
    :else old-direction))

(defn loop-snake [b s d]
  (loop [board b snake s direction d]
    (let [moved-snake (move-snake snake direction)]
      (s/clear scr)
      (print-board (board-with-snake board moved-snake))
      (s/redraw scr)
      (Thread/sleep 1000)
      (let [new-dir (new-direction (s/get-key scr) direction)] 
        (recur board moved-snake new-dir)))))
      
(defn -main
  [& args]
  (s/start scr)
  (s/redraw scr)
  (let [snake [[0 0] [0 1] [0 2]]]
    (loop-snake board snake left)))
    ;(println (assoc-in board [1 1] "*"))
    ;;(print-board (board-with-snake board snake))
    ;;(print-board (board-with-apple board [2 2]))))
    ;;(println (move-snake snake left))))
