(ns snake.core
  (:gen-class))

(defn zero-vector [size]
    (for [x (range size)] 0))

(def board-size 10) ;; square board for simplicity

(def left [-1 0])
(def right [1 0])
(def up [0 -1])
(def down [0 1])

(def board 
    (vec (for [x (range board-size)]
          (into [] (zero-vector board-size)))))

(defn print-board [board]
    (dotimes [row board-size]
        (println (nth board row)))) 

(defn +mod [x y]
  (map #(mod (+ %1 %2) board-size) x y))

(defn move-snake [snake direction]
 (cons 
   (into [] (+mod (first snake) direction))
   (pop snake)))

(defn board-with-snake [board snake]
    (loop [piece 0
           b board]
        (if (>= piece (count snake))
         b
         (recur (inc piece) (assoc-in b (nth snake piece) "*")))))
    
(defn board-with-apple [board apple]
    (assoc-in board apple "X"))

(defn -main
  [& args]
  (let [snake [[0 0] [0 1] [0 2]]]
    ;(println (assoc-in board [1 1] "*"))
    ;;(print-board (board-with-snake board snake))
    ;;(print-board (board-with-apple board [2 2]))))
    (println (move-snake snake left))))
