evalComposites(V, C, N) :- V1 is V * C, V1 > N, !.
evalComposites(V, C, N) :- V1 is V * C, assert(composites(V1)), C1 is C + 1, evalComposites(V, C1, N).

evalPrimes(V, N) :- V > N, !.
evalPrimes(V, N) :- composites(V), !, V1 is V + 1, evalPrimes(V1, N).
evalPrimes(V, N) :- evalComposites(V, 2, N), assert(prime(V)), V1 is V + 1, evalPrimes(V1, N).

init(N) :- number(N), evalPrimes(2, floor(sqrt(N))).

multiplyToAll(A, [], A).
multiplyToAll(A, [H], R) :- R is A * H.
multiplyToAll(V, [H1, H2 | T], R) :- V1 is V * H1, multiplyToAll(V1, [H2 | T], R).

checkSort([]).
checkSort([_]).
checkSort([H1, H2 | T]) :- H1 =< H2, checkSort([H2 | T]).

findCountOfDivisors(1, [], []) :- !.
findCountOfDivisors(V, [], [V]).
findCountOfDivisors(V, [D | TD], [D | R]) :- 0 is mod(V, D), !, V1 is V // D, findCountOfDivisors(V1, [D | TD], R).
findCountOfDivisors(V, [_ | TD], R) :- findCountOfDivisors(V, TD, R).

prime_divisors(V, R) :- number(V), !, findall(X, (prime(X), 0 is mod(V, X)), R1), findCountOfDivisors(V, R1, R).
prime_divisors(V, R) :- checkSort(R), multiplyToAll(1, R, V).

prime(N) :- number(N), findall(X, (prime(X), 0 is mod(N, X), !), []).
composite(N) :- \+ prime(N).

intersection(_, [], []) :- !.
intersection([], _, []).
intersection([H1 | T1], [H2 | T2], R) :- H1 < H2, intersection(T1, [H2 | T2], R), !.
intersection([H1 | T1], [H2 | T2], R) :- H1 > H2, intersection([H1 | T1], T2, R), !.
intersection([H1 | T1], [_ | T2], [H1 | TR]) :- intersection(T1, T2, TR).

gcd(A, B, R) :- prime_divisors(A, RA), prime_divisors(B, RB), intersection(RA, RB, RR), multiplyToAll(1, RR, R).