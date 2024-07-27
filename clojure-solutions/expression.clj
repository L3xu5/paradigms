(defn createOperation [f]
  (fn [& args]
    (fn [variables]
      (apply f (mapv #(% variables) args)))))

(defn constant [value]
  (constantly value))

(defn variable [name]
  (fn [variables]
    (get variables name)))

(def add (createOperation +))

(def subtract (createOperation -))

(def multiply (createOperation *))

(defn fixedDivide [& args]
  (let
    [divisible (double (first args))
     dividers (apply * (rest args))]
    (if (= 1 (count args))
      (/ 1 divisible)
      (/ divisible dividers))))

(def divide (createOperation fixedDivide))

(def negate subtract)

(defn evaluateMean [& args]
  (/
    (apply + args)
    (count args)))

(def mean (createOperation evaluateMean))

(defn evaluateVarn [& args]
  (-
    (apply evaluateMean (mapv #(Math/pow % 2) args))
    (Math/pow (apply evaluateMean args) 2)))

(def varn (createOperation evaluateVarn))

(def operations
  {
   :+ add,
   :- subtract,
   :* multiply,
   :/ divide,
   :negate negate,
   :mean mean,
   :varn varn,
   :constant constant,
   :variable variable
   })

(definterface OperationInterface
  (^Number evaluate [vars])
  (^String toString [])
  (diff [name]))

(defn defaultEvaluate [f vars args]
  (apply f (mapv #(.evaluate % vars) args)))

(defn defaultToString [operationSymbol args]
  (str "(" operationSymbol " " (clojure.string/join " " (mapv #(.toString %) args)) ")"))

(defmacro defaultDiff [constructorName]
  `(fn [diffArgs# ~'_] (new ~constructorName diffArgs#)))

(defmacro createConstructor [constructorName f diffF opSym]
  `(deftype ~constructorName [~'args]
     OperationInterface
     (evaluate [this# vars#] (~f vars# (.-args this#)))
     (toString [this#] (defaultToString ~opSym (.-args this#)))
     (diff [this# name#] (~diffF (mapv #(.diff % name#) (.-args this#)) (.-args this#)))))


(createConstructor AddConstructor (partial defaultEvaluate +) (defaultDiff AddConstructor) "+")

(defn Add [& args] (AddConstructor. args))

(createConstructor SubtractConstructor (partial defaultEvaluate -) (defaultDiff SubtractConstructor) "-")

(defn Subtract [& args] (SubtractConstructor. args))

(defmacro diffMultiply []
  `(fn diff# [diffArgs# args#]
         (let [op1# (first args#)
               diffOp1# (first diffArgs#)
               op2# (~'MultiplyConstructor. (rest args#))]
           (if (= (count args#) 1)
             diffOp1#
             (AddConstructor.
               [(~'MultiplyConstructor. [diffOp1# op2#])
                (~'MultiplyConstructor. [op1# (diff# (rest diffArgs#) (rest args#))])])))))

(createConstructor MultiplyConstructor
                   (partial defaultEvaluate *)
                   (diffMultiply)
                   "*")

(defn Multiply [& args] (MultiplyConstructor. args))

(createConstructor NegateConstructor (partial defaultEvaluate -) (defaultDiff NegateConstructor) "negate")

(defn Negate [& args] (NegateConstructor. args))

(createConstructor DivideConstructor
                   (partial defaultEvaluate fixedDivide)
                   (fn diff [diffArgs args]
                         (let [op1 (first args)
                               diffOp1 (first diffArgs)
                               op2 (MultiplyConstructor. (rest args))]
                           (if (= (count args) 1)
                             (DivideConstructor.
                               [(Negate diffOp1)
                                (Multiply op1 op1)])
                             (DivideConstructor.
                               [(Subtract
                                  (Multiply diffOp1 op2)
                                  (Multiply op1 ((diffMultiply) (rest diffArgs) (rest args))))
                                (Multiply op2 op2)]))))
                   "/")

(defn Divide [& args] (DivideConstructor. args))

(deftype ConstantConstructor [value]
  OperationInterface
  (evaluate [this _] (.value this))
  (toString [this] (str (.value this)))
  (diff [_ _] (ConstantConstructor. 0)))

(def zeroConstant (ConstantConstructor. 0))

(def oneConstant (ConstantConstructor. 1))

(defn Constant [value] (ConstantConstructor. value))

(deftype VariableConstructor [name]
  OperationInterface
  (evaluate [this vars] (get vars (.name this)))
  (toString [this] (.name this))
  (diff [this name] (if (= name (.name this))
                      oneConstant
                      zeroConstant)))

(createConstructor MeanConstructor (partial defaultEvaluate evaluateMean) (defaultDiff MeanConstructor) "mean")

(defn Mean [& args] (MeanConstructor. args))

(createConstructor VarnConstructor
                   (partial defaultEvaluate evaluateVarn)
                   (fn [diffArgs args]
                     (let [pairs (map vector args diffArgs)
                           sumArgsAndDiffArgs (AddConstructor. (mapv #(MultiplyConstructor. %) pairs))
                           sumArgs (AddConstructor. args)
                           sumDiffArgs (AddConstructor. diffArgs)]
                       (MultiplyConstructor.
                         [(ConstantConstructor. (/ 2 (Math/pow (count args) 2))),
                          (SubtractConstructor.
                            [(MultiplyConstructor.
                               [(ConstantConstructor. (count args)),
                                sumArgsAndDiffArgs]),
                             (MultiplyConstructor. [sumArgs, sumDiffArgs])])])))
                   "varn")

(defn Varn [& args] (VarnConstructor. args))

(defn Variable [name] (VariableConstructor. name))

(defn evaluate [expression vars] (.evaluate expression vars))

(defn toString [expression] (.toString expression))

(defn diff [expression name] (.diff expression name))

(def objectOperations
  {
   :+ Add,
   :- Subtract,
   :* Multiply,
   :/ Divide,
   :negate Negate,
   :mean Mean,
   :varn Varn,
   :constant Constant,
   :variable Variable
   })

(defn buildExpression [tokens operations]
  (cond
    (number? tokens) ((get operations :constant) (double tokens))
    (symbol? tokens) ((get operations :variable) (name tokens))
    :else (let [operation (get operations (keyword (first tokens)))
                arguments (rest tokens)]
            (apply operation (mapv #(buildExpression % operations) arguments)))))

(defn parseFunction [string]
  (buildExpression (read-string string) operations))

(defn parseObject [string]
  (buildExpression (read-string string) objectOperations))