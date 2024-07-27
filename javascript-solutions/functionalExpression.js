const cnst = (value) => () => value;

const pi = cnst(Math.PI);

const e  = cnst(Math.E);

const variable = (name) => (x, y, z) => {
    switch (name) {
        case 'x':
            return x;
        case 'y':
            return y;
        case 'z':
            return z;
        default:
            throw new Error("Bad variable name " + name);
    }
}

const func = (operation, ...args) => (x, y, z) => operation(...args.map(arg => arg(x, y, z)));

const negate = (op1) => func((a) => -a, op1);

// :NOTE: .length
const add = (op1, op2) => func((a, b) => a + b, op1, op2);

const subtract = (op1, op2) => func((a, b) => a - b, op1, op2);

const multiply = (op1, op2) => func((a, b) => a * b, op1, op2);

const divide = (op1, op2) => func((a, b) => a / b, op1, op2);

const max3 = (op1, op2, op3) => func(Math.max, op1, op2, op3);

const min5 = (op1, op2, op3, op4, op5) => func(Math.min, op1, op2, op3, op4, op5);

const parse = (string)  => {
    let stack = [];
    let operations =  new Map([['+', {func : add, amountOfArgs : 2}],
        ['-', {func : subtract, amountOfArgs : 2}], ['*', {func : multiply, amountOfArgs : 2}],
        ['/', {func : divide, amountOfArgs : 2}], ["negate", {func : negate, amountOfArgs : 1}],
        ["max3", {func : max3, amountOfArgs : 3}], ["min5", {func : min5, amountOfArgs : 5}],
        ['x', {func : variable, amountOfArgs : 0}], ['y', {func : variable, amountOfArgs : 0}],
        ['z', {func : variable, amountOfArgs : 0}], ['e', {func : cnst(e), amountOfArgs : 0}],
        ["pi", {func : cnst(pi), amountOfArgs : 0}]]);
    for (let token of string.match(/[^ ]+/g)) {
        if (operations.has(token)) {
            let operation = operations.get(token);
            stack.push(operation.func(...stack.splice(stack.length - operation.amountOfArgs), token));
        } else {
            stack.push(cnst(Number(token)));
        }
    }
    return stack.pop();
}