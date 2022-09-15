\ENCODING=ISO-8859-1
\Problem name: ilog.cplex

Minimize
 obj1: x1
Subject To
 c1:  x2 + x3 + x4 + x5 + x6 + x7 + x8 + x9 + x10  = 1
 c2:  x3 + x4 + x6 + x7 + x9 + x10  = 1
 c3:  x4 + x7 + x10  = 1
 c4:  - x1 + x8 <= 0
 c5:  - x1 + 3 x9 <= 0
 c6:  - x1 + 7 x10 <= 0
 c7:  - x1 + x5 <= 0
 c8:  - x1 + 3 x6 <= 0
 c9:  - x1 + 7 x7 <= 0
 c10: - x1 + x2 <= 0
 c11: - x1 + 3 x3 <= 0
 c12: - x1 + 7 x4 <= 0
 c13: x2 + x3 + x4 + x5 + x6 + x7 + x8 + x9 + x10 <= 3
Bounds
 0 <= x1 <= 20
 0 <= x2 <= 1
 0 <= x3 <= 1
 0 <= x4 <= 1
 0 <= x5 <= 1
 0 <= x6 <= 1
 0 <= x7 <= 1
 0 <= x8 <= 1
 0 <= x9 <= 1
 0 <= x10 <= 1
Binaries
 x2  x3  x4  x5  x6  x7  x8  x9  x10 
End
