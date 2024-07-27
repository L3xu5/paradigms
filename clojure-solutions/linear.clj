(defn vecOfVectorsPred [args]
  (and (not (zero? (count args)))
       (every? vector? args)
       (every? (fn [vec] (every? number? vec)) args)
       (let [vec1Length (count (first args))]
         (every? (fn [vec] (= (count vec) vec1Length)) args))))

(defn vecOfVectorsPost [args res]
  (and (= (count res) (count (first args)))
       (every? number? res)
       (vector? res)))

(defn voperation [oper args]
  {:pre [(vecOfVectorsPred args)]
   :post [(vecOfVectorsPost args %)]}
  (apply mapv oper args))

(defn v+ [& args]
  (voperation + args))

(defn v- [& args]
  (voperation - args))

(defn v* [& args]
  (voperation * args))

(defn vd [& args]
  (voperation / args))

(defn v*s [vec, & args]
  {:pre [(vecOfVectorsPred [vec])
         (every? (fn [elem] (number? elem)) args)]
   :post [(= (count %) (count vec))]}
  (let [scalar (apply * args)]
    (mapv (partial * scalar) vec)))

(defn scalar [& args]
  {:pre [(vecOfVectorsPred args)
         (every? (fn [vec] (every? (fn [elem] (not (zero? elem))) vec)) (rest args))]
   :post [(number? %)]}
      (reduce + (apply v* args)))

(defn vect [& args]
  {:pre [(vecOfVectorsPred args)
         (every? (fn [vec] (= (count vec) 3)) args)]
   :post [(vecOfVectorsPost args %)]}
  (reduce (fn [vec1 vec2] [(- (* (vec1 1) (vec2 2))
                              (* (vec1 2) (vec2 1)))
                           (- (* (vec1 2) (vec2 0))
                              (* (vec1 0) (vec2 2)))
                           (- (* (vec1 0) (vec2 1))
                              (* (vec1 1) (vec2 0)))])
          args))

(defn vecOfMatrixPred [args] (and (every? vector? args)
                                  (every? vecOfVectorsPred args)
                        (let [mat1Length (count (first args))]
                          (every? (fn [mat] (= (count mat) mat1Length)) args))
                        (let [vec1Length (count (first (first args)))]
                          (every? (fn [mat] (every? (fn [vec] (= (count vec) vec1Length)) mat)) args))))

(defn vecOfMatrixPost [args res] (and (= (count res) (count (first args)))
                                  (every? (fn [vec] (= (count vec) (count (first (first args))))) res)))

(defn moperation [oper args]
  {:pre [(vecOfMatrixPred args)]
   :post [(vecOfMatrixPost args %)]}
  (apply mapv oper args))

(defn m+ [& args]
  (moperation v+ args))

(defn m- [& args]
  (moperation v- args))

(defn m* [& args]
  (moperation v* args))

(defn md [& args]
  (moperation vd args))

(defn isSimplexCorrect [simplex n]
  (if (every? number? simplex)
    (= n (count simplex))
    (and (every? (fn [subSimplex] (isSimplexCorrect subSimplex (- n (.indexOf simplex subSimplex)))) simplex)
         (= n (count simplex)))))

(defn depth [vec]
  (if (every? number? vec)
    1
    (+ 1 (depth (first vec)))))

(defn simplexPred [args]
  (let [n (count (first args))]
    (let [simplexDepth (depth (first args))]
      (every? (fn [simplex] (and (isSimplexCorrect simplex n) (= simplexDepth (depth simplex)))) args))))

(defn simplexPost [res n simplexDepth]
  (and (isSimplexCorrect res n) (= simplexDepth (depth res))))

(defn soperation [oper oper2 args]
  {:pre [(simplexPred args)]
   :post [(simplexPost % (count (first args)) (depth (first args)))]}
  (if (every? (fn [arg] (every? number? arg)) args)
    (apply oper args)
    (apply mapv oper2 args)))

(defn x+ [& args]
  (soperation v+ x+ args))

(defn x- [& args]
  (soperation v- x- args))

(defn x* [& args]
  (soperation v* x* args))

(defn xd [& args]
  (soperation vd xd args))

(defn m*s [mat & args]
  {:pre [(vecOfVectorsPred mat)
         (every? (fn [elem] (number? elem)) args)]
   :post [(= (count %) (count mat))
          (every? (fn [vec] (= (count vec) (count (first mat)))) %)]}
  (let [scalar (apply * args)]
    (mapv (fn [vec] (v*s vec scalar)) mat)))

(defn m*v [mat vec]
  {:pre [(vecOfVectorsPred mat)
         (vector? vec)
         (= (count (first mat)) (count vec))]
   :post [(= (count %) (count mat))
          (every? number? %)]}
    (mapv (fn [vec2] (scalar vec vec2)) mat))

(defn transpose [mat]
  {:pre [(vecOfVectorsPred mat)]
   :post [(= (count %) (count (first mat)))
          (let [numberOfColumns (count mat)]
            (every? (fn [vec] (= (count vec) numberOfColumns)) %))]}
  (apply mapv vector mat))

(defn m*m [& args]
  {:pre [(every? vecOfVectorsPred args)
         (every? (fn [mat] (let [vec1Length (count (first mat))]
                             (every? (fn [vec] (= (count vec) vec1Length)) mat))) args)
         (if (>= (count args) 2)
           (= (count (first (first args))) (count (nth args 1)))
           true)
         ]
   :post [(= (count %) (count (first args)))
          (let [numberOfColumns (count (first (last args)))]
            (every? (fn [vec] (= (count vec) numberOfColumns)) %))]}
  (reduce (fn [mat1 mat2] (mapv (fn [vec] (m*v (transpose mat2) vec)) mat1)) args))