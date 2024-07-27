checkSort([]).
checkSort([_]).
checkSort([(H1, _), (H2, _) | T]) :- H1 =< H2,
    checkSort([(H2, _) | T]).

null.

max(A, B, R) :- (A =< B -> R is B + 1; R is A + 1).

map_build(_, L, L, [null, null, null, 0]).
map_build(ListMap, L, R, [Elem, Left, Right, Size]) :- L < R, C is (L + R) // 2, C1 is C + 1,
    element(C1, ListMap, Elem),
    map_build(ListMap, L, C, Left),
    map_build(ListMap, C1, R, Right),
    [_, _, _, LeftSize] = Left, [_, _, _, RightSize] = Right, max(LeftSize, RightSize, Size).

map_build(ListMap, TreeMap) :- checkSort(ListMap), length(ListMap, R),
    map_build(ListMap, 0, R, TreeMap), !.
map_build(ListMap, TreeMap) :-
    quicksort(ListMap, '@<', SortedListMap), length(ListMap, R),
    map_build(SortedListMap, 0, R, TreeMap).

map_get([(Idx, R) | _], Idx, R) :- !.
map_get([(ElemIdx, _), Left | _], Idx, R) :- Idx < ElemIdx,
    map_get(Left, Idx, R), !.
map_get([_, _, Right | _], Idx, R) :-
    map_get(Right, Idx, R).

map_ceilingEntry([(Idx, ElemValue) | _], Idx, (Idx, ElemValue)) :- !.
map_ceilingEntry([(ElemIdx, ElemValue), [null | _], [null | _] | _], Idx, (ElemIdx, ElemValue)) :- !, ElemIdx >= Idx.
map_ceilingEntry([(ElemIdx, ElemValue), [null | _], _, Idx, (ElemIdx, ElemValue)]) :- Idx =< ElemIdx, !.
map_ceilingEntry([(ElemIdx, ElemValue), Left | _], Idx, R) :- Idx < ElemIdx,
    (map_ceilingEntry(Left, Idx, TR) -> R = TR; R = (ElemIdx, ElemValue)), !.
map_ceilingEntry([(ElemIdx, ElemValue), _, Right | _], Idx, R) :-
    (map_ceilingEntry(Right, Idx, TR) -> R = TR; Idx < ElemIdx, R = (ElemIdx, ElemValue)).

leftRotate([Elem, Left, Right, _], [RightElem, [Elem, Left, RightLeft, NewLeftSize], RightRight, NewSize]) :- [_, _, _, LeftSize] = Left,[RightElem, RightLeft, RightRight, RightSize] = Right,  [_, _, _, LeftSize] = Left,[_, _, _, RightLeftSize] = RightLeft ,[_, _, _, RightRightSize] = RightRight, max(LeftSize, RightLeftSize, NewLeftSize), max(NewLeftSize, RightRightSize, NewSize).

rightRotate([Elem, Left, Right, _], [RightElem, [Elem, Left, RightLeft, NewLeftSize], RightRight, NewSize]) :- [_, _, _, LeftSize] = Left,[RightElem, RightLeft, RightRight, RightSize] = Right,  [_, _, _, LeftSize] = Left,[_, _, _, RightLeftSize] = RightLeft ,[_, _, _, RightRightSize] = RightRight, max(LeftSize, RightLeftSize, NewLeftSize), max(NewLeftSize, RightRightSize, NewSize).

bigLeftRotate([Elem, Left, Right, _], [RightElem, [Elem, Left, RightLeft, NewLeftSize], RightRight, NewSize]) :- [_, _, _, LeftSize] = Left,[RightElem, RightLeft, RightRight, RightSize] = Right,  [_, _, _, LeftSize] = Left,
[_, _, _, RightLeftSize] = RightLeft ,[_, _, _, RightRightSize] = RightRight, max(LeftSize, RightLeftSize, NewLeftSize), max(NewLeftSize, RightRightSize, NewSize).

bigRightRotate([Elem, Left, Right, _], [RightElem, [Elem, Left, RightLeft, NewLeftSize], RightRight, NewSize]) :- [_, _, _, LeftSize] = Left,[RightElem, RightLeft, RightRight, RightSize] = Right,  [_, _, _, LeftSize] = Left,
[_, _, _, RightLeftSize] = RightLeft ,[_, _, _, RightRightSize] = RightRight, max(LeftSize, RightLeftSize, NewLeftSize), max(NewLeftSize, RightRightSize, NewSize).


rebuildIfNeeded(Current, R) :- [_, Left, Right, Size] = Current, [_, LeftLeft, LeftRight, LeftSize] = Left,[_, RightLeft, RightRight, RightSize] = Right,
    [_, _, _, RightLeftSize] = RightLeft, [_, _, _, RightRightSize] = RightRight,
    [_, _, _, LeftLeftSize] = LeftLeft, [_, _, _, LeftRightSize] = LeftRight,
    (-2 is LeftSize - RightSize ->  (1 is RightLeftSize - RightRightSize -> bigLeftRotate(Current, R); leftRotate(Current, R));
     (2 is LeftSize - RightSize -> (rightRotate(Current, R)); R = Current)), !.
rebuildIfNeeded(R, R).

map_put([], Idx, Value, [(Idx, Value), [null, null, null, 0], [null, null, null, 0], 1]) :- !.
map_put([null | _], Idx, Value, [(Idx, Value), [null, null, null, 0], [null, null, null, 0], 1]) :- !.
map_put([(Idx, _) | T], Idx, Value, [(Idx, Value) | T]) :- !.
map_put([(ElemIdx, ElemValue), Left, Right, _], Idx, Value, R) :-
    [_, _, _, LeftSize] = NewLeft, [_, _, _, RightSize] = NewRight,
    (Idx < ElemIdx -> map_put(Left, Idx, Value, NewLeft), NewRight = Right; map_put(Right, Idx, Value, NewRight), NewLeft = Left),
    max(LeftSize, RightSize, NewSize),
    TR = [(ElemIdx, ElemValue), NewLeft, NewRight, NewSize],
    rebuildIfNeeded(TR, R).

leftmostInTheRight([Current, [null | _], Right, _], Current, Right) :- !.
leftmostInTheRight([Current, Left, Right, _], NewCurrent, R) :- [_, _, _, LeftSize] = NewLeft, [_, _, _, RightSize] = Right,
    leftmostInTheRight(Left, NewCurrent, NewLeft), max(LeftSize, RightSize, Size), R = [Current, NewLeft, Right, Size].

map_remove([], _, [null, null, null, 0]) :- !.
map_remove([null | _], _, [null, null, null, 0]) :- !.
map_remove([(Idx, _), _, _, 1], Idx, [null, null, null, 0]) :- !.
map_remove([(Idx, _), [null | _], Right, _], Idx, Right) :- !.
map_remove([(Idx, _), Left, [null | _], _], Idx, Left) :- !.
map_remove([(Idx, _), Left, Right | _], Idx, R) :- [_, _, _,  LeftSize] = Left, [_, _, _,  RightSize] = NewRight,
    leftmostInTheRight(Right, NewCurrent, NewRight),
    max(LeftSize, RightSize, Size), R = [NewCurrent, Left, NewRight, Size], !.
map_remove([(ElemIdx, ElementValue), Left, Right | _], Idx, R) :-
    [_, _, _, LeftSize] = NewLeft, [_, _, _, RightSize] = NewRight,
    (Idx < ElemIdx -> map_remove(Left, Idx, NewLeft), NewRight = Right; map_remove(Right, Idx, NewRight), NewLeft = Left),
    max(LeftSize, RightSize, Size), TR = [(ElemIdx, ElementValue), NewLeft, NewRight, Size],
    rebuildIfNeeded(TR, R).

map_removeCeiling(Map, Idx, Result) :-
    map_ceilingEntry(Map, Idx, (ElemIdx, _)),
    map_remove(Map, ElemIdx, Result), !.
map_removeCeiling(Map, Idx, Map).