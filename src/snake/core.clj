(ns snake.core
  (:gen-class))

(defn zero-vector [size]
    (for [x (range size)] 0))

(def board-h 10)
(def board-w 10)

(def left [-1 0])
(def right [1 0])
(def up [0 -1])
(def down [0 1])

(def board 
    (vec (for [x (range board-h)]
        (into [] (zero-vector board-w)))))

(defn print-board [board]
    (dotimes [row board-h]
        (println (nth board row)))) 

(defn move-snake [snake direction]
    (cons 
        (into [] (map + (first snake) direction)) 
        (pop snake)))

(defn board-with-snake [board snake]
    (loop [piece 0
           b board]
        (if (>= piece (count snake))
        b
        (recur (inc piece) (assoc-in b (nth snake piece) "*")))))
    
(defn -main
  [& args]
  (let [snake [[4 4] [4 5] [4 6]]]
    ;(println (assoc-in board [1 1] "*"))
    (print-board (board-with-snake board snake))
    (println (move-snake snake left))))
