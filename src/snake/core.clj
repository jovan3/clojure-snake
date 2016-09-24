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

(defn put-apple [snake]
  (loop [new-apple [(rand-int board-size) (rand-int board-size)]]
    (cond
      (.contains snake new-apple) (recur snake)
      :else new-apple)))

(defn move-snake [snake direction apple]
  (let [new-head (into [] (+mod (first snake) direction))]
      (cond
        (.contains snake new-head) nil
        (= new-head apple)  
          (let [new-snake (vec (cons new-head snake))]
            { :snake new-snake :apple (put-apple new-snake) })
        :else {:snake (vec (cons new-head (pop snake))) :apple apple})))

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

(defn loop-snake [b s d a]
  (loop [board b snake s direction d apple a]
    (let [moved-snake-apple (move-snake snake direction apple)]
      (s/clear scr)
      (when-not (nil? moved-snake-apple)
        (let [{moved-snake :snake new-apple :apple} moved-snake-apple]
          (print-board (board-with-apple (board-with-snake board moved-snake) new-apple))
          (s/redraw scr)
          (Thread/sleep 1000)
          (let [new-dir (new-direction (s/get-key scr) direction)] 
            (recur board moved-snake new-dir new-apple)))))))

(defn game-over []
  (s/clear scr)
  (s/put-string scr 5 5 "GAME OVER")
  (s/redraw scr))

(defn -main
  [& args]
  (s/start scr)
  (s/redraw scr)
  (let [snake [[0 0] [0 1] [0 2]]]
    (loop-snake board snake left [5 5]))
    (game-over))
