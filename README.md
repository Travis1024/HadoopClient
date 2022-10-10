# HadoopClient
 BayesProbabilityCalc





<1>--I13000

<2>--I33020

<3>--I75000

<4>--I81402



|         |       | Real | Real | Real | Real |
| ------- | ----- | ---- | ---- | ---- | ---- |
|         | Class | 1    | 2    | 3    | 4    |
| Predict | 1     | a    | e    | i    | m    |
| Predict | 2     | b    | f    | j    | n    |
| Predict | 3     | c    | g    | k    | p    |
| Predict | 4     | d    | h    | l    | q    |

-   predictClassNums[0] = a + e + i + m
-   predictClassNums[1] = b + f + j + n
-   predictClassNums[2] = c + g + k + p
-   predictClassNums[3] = d + h + l + q



-   realClassNums[0] = a + b + c + d
-   realClassNums[1] = e + f + g + h
-   realClassNums[2] = i + j + k + l
-   realClassNums[3] = m + n + p + q



-   对于第一类：

    FP1 = e + i + m
    TP1 = a
    FN1 = b + c + d
    TN1 = f + g + h + j + k + l + n + p +q

-   对于第二类：

    FP2 = b + j + n
    TP2 = f
    FN2 = e + g + h
    TN2 = a + c + d + i + k + l + m + p + q

-   对于第三类：

    FP3 = c + g + p
    TP3 = k
    FN3 = i +j +l
    TN3 = a + b + d + e + f + h + m + n + q

-   对于第四类：

    FP4 = d + h + l
    TP4 = q
    FN4 = m + n + p
    TN4 = a + b + c + e + f + g + i + j + k
