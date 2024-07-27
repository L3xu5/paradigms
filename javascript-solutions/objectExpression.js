function expr(operation, symbol) {
    function Func(...args) {
        this.args = args;
    }

    Func.numberOfArguments = operation.length;

    Func.prototype.operation = operation;

    Func.prototype.symbol = symbol;

    Func.prototype.diffOperation = function (...args) {
        return new Func(...args)
    };

    Func.prototype.diff = function(name) {
        return this.diffOperation(...this.args.map(arg => arg.diff(name)));
    }

    Func.prototype.evaluate = function(...args) {
        return this.operation(...this.args.map(arg => arg.evaluate(...args)));
    }

    Func.prototype.toString = function() {
        return this.args.join(" ") + " " + this.symbol;
    }

    Func.prototype.prefix = function () {
        return "(" + this.symbol + " " + this.args.map(arg => arg.prefix()).join(" ") + ")";
    }

    Func.prototype.postfix = function () {
        return "(" + this.args.map(arg => arg.postfix()).join(" ") + " " + this.symbol + ")";
    }

    return Func;
}

function Const(value) {
    this.value = value;
}

Const.prototype.toString = function() {
    return String(this.value).valueOf();
}

Const.prototype.prefix = Const.prototype.postfix  = Const.prototype.toString;

Const.prototype.evaluate = function() {
    return this.value;
}

Const.prototype.diff = function() {
    return Const.ZERO;
}

Const.ZERO = new Const(0);
Const.ONE = new Const(1);

function Variable(name) {
    this.name = name;
    if (Variable.argsIndexes.has(this.name)) {
        this.index = Variable.argsIndexes.get(this.name);
    } else {
        throw new Error("Bad variable name " + this.name);
    }
}

Variable.argsIndexes = new Map([['x', 0], ['y', 1], ['z', 2]]);

Variable.prototype.evaluate = function(...args) {
    return args[this.index]
}

Variable.prototype.toString = function() {
    return this.name.valueOf();
}

Variable.prototype.prefix = Variable.prototype.postfix = Variable.prototype.toString;

Variable.prototype.diff = function(name) {
    return this.name === name ? Const.ONE : Const.ZERO;
}

const Add = expr((op1, op2) => op1 + op2, '+');

const Subtract = expr((op1, op2) => op1 - op2, '-');

const Multiply = expr((op1, op2) => op1 * op2, '*');

Multiply.prototype.diffOperation = function (op1Diff, op2Diff) {
    const [op1, op2] = this.args;
    return new Add(
        new Multiply(op1Diff, op2),
        new Multiply(op1, op2Diff)
    );
}

const Divide = expr((op1, op2) => op1 / op2, '/');

Divide.prototype.diffOperation = function (op1Diff, op2Diff) {
    const op1 = this.args[0]
    const op2 = this.args[1]
    return new Divide(
        new Subtract(
            new Multiply(op1Diff, op2),
            new Multiply(op1, op2Diff)
        ),
        new Multiply(op2, op2)
    );
}

const Negate = expr((op1) => -op1, "negate");

const ArcTan = expr(Math.atan, "atan",)

ArcTan.prototype.diffOperation = function (op1Diff) {
    const op1 = this.args[0]
    return new Multiply(
        op1Diff,
        new Divide(
            Const.ONE,
            new Add(
                Const.ONE,
                new Multiply(op1, op1)
            )
        )
    );
}

const ArcTan2 = expr(Math.atan2, "atan2");

ArcTan2.prototype.diff = function (name) {
    return new ArcTan(new Divide(...this.args)).diff(name);
}

const mean = (...args) => args.reduce((a, b) => a + b, 0) / args.length;

const Mean = expr(mean, "mean");

const Var = expr((...args) => mean(...args.map(arg => arg ** 2)) - mean(...args)**2, "var")

Var.prototype.diffOperation = function (...opsDiff) {
    const sumOfArgs = (args) => args.reduce((a, b) => new Add(a, b), Const.ZERO);
    const sumOpsAndOpsDiff = sumOfArgs(opsDiff.map((op, i) => new Multiply(op, this.args[i])));
    const sumOps = sumOfArgs(this.args);
    const sumOpsDiff = sumOfArgs(opsDiff);
    return new Multiply(
        new Const(2 / opsDiff.length ** 2),
        new Subtract(
            new Multiply(new Const(opsDiff.length), sumOpsAndOpsDiff),
            new Multiply(sumOps, sumOpsDiff)
        )
    );
}

const operations = new Map([
    ['+', Add],
    ['-', Subtract],
    ['*', Multiply],
    ['/', Divide],
    ["negate", Negate],
    ["atan", ArcTan], ["atan2", ArcTan2],
    ["mean", Mean],
    ["var", Var]
])

const variables = new Map([
    ['x', new Variable('x')],
    ['y', new Variable('y')],
    ['z', new Variable('z')]
]);

function parse(string) {
    const stack = [];
    for (let token of string.match(/[^ ]+/g)) {
        if (operations.has(token)) {
            const operation = operations.get(token);
            stack.push(new operation(...stack.splice(-operation.numberOfArguments)));
        } else if (variables.has(token)) {
            stack.push(variables.get(token));
        } else {
            stack.push(new Const(parseFloat(token)));
        }
    }
    return stack.pop();
}

function Tokenizer(string, postfixMode = false) {
    const bracketsReplacePattern = new Map([["(", " ) "], [")", " ( "]]);
    this.tokens = postfixMode
        ? string.replace(/[()]/g, match => bracketsReplacePattern.get(match)).match(/[^ ]+/g).reverse()
        : string.replace(/[()]/g, " $& ").match(/[^ ]+/g);
    this.currentIndex = 0;
}

Tokenizer.prototype.take = function(pattern) {
    const token = this.tokens[this.currentIndex];
    if (token === pattern) {
        this.currentIndex++;
        return true;
    }
    return false;
}

Tokenizer.prototype.next = function() {
    return this.tokens[this.currentIndex++];
}

Tokenizer.prototype.done = function() {
    return this.currentIndex === this.tokens.length;
};

function customError(name) {
    function Err(message = "") {
        this.name = name;
        this.message = message;
    }
    Err.prototype = Object.create(Error.prototype);
    Err.prototype.constructor = Err;
    return Err;
}

const InvalidNumberOfArgumentsError = customError("InvalidNumberOfArgumentsError");

const InvalidOperationError = customError("InvalidOperationError");

const InvalidArgumentError = customError("InvalidArgumentError");

const InvalidExpressionError = customError("InvalidExpressionError");

const parseExpression = (tokenizer, postfixMode = false) => {
    let token = tokenizer.next();
    let result;
    if (token === '(') {
        token = tokenizer.next();
        const args = [];
        if (operations.has(token)) {
           const operation = operations.get(token);
           while (!tokenizer.take(")")) {
               args.push(parseExpression(tokenizer, postfixMode));
           }
           if (operation.numberOfArguments !== 0 && operation.numberOfArguments !== args.length) {
               throw new InvalidNumberOfArgumentsError(`Invalid number of arguments = ${args.length} for operation ${token}`);
           }
           result = new operation(...postfixMode ? args.reverse() : args);
        } else if (token === ')') {
            throw new InvalidOperationError(`Empty operation`);
        } else {
            throw new InvalidOperationError(`Invalid operation ${token}`);
        }
    } else if (variables.has(token)) {
        result = variables.get(token);
    } else {
        const number = Number(token)
        if (isNaN(number)) {
            throw new InvalidArgumentError(`"${token}" is not a valid value name or number`);
        }
        result = new Const(number);
    }
    return result;
}

const parseWithMode = (string, postfixMode) => {
    if (string.trim() === "") {
        throw new InvalidExpressionError("Empty expression");
    }
    const tokenizer = new Tokenizer(string, postfixMode);
    const result = parseExpression(tokenizer, postfixMode);
    if (!tokenizer.done()) {
        throw new InvalidExpressionError("Invalid expression " + string);
    }
    return result;
}

const parsePrefix = (string) => parseWithMode(string, false)

const parsePostfix = (string) => parseWithMode(string, true)