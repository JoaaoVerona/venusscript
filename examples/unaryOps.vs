# VenusScript - Unary operations example.
# This script shows some of the unary operations
# available on the language, and how they interact
# (or should) with binary operations.

using std

i = 55
j = -35
println("i = " + i + ", j = " + j)
println("-i = " + (-i) + ", j = " + (-j))
isPositive = true
println("isPositive = " + isPositive)
println("isNegative = " + !isPositive)
println("isSurelyPositive = " + !!!!isPositive)