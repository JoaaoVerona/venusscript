# VenusScript - Flowchart example.
# This script demonstrates the new flowchart
# of the parser, as seen in issue #22.
using std

export var = 0

println($var)
println($var = 5)
println($var + 5)
println(i = 3 + 5)
println(i)

array = [5, 2, 3]
println(array)
println(array[0] = 100)
println(array)
array[1] -= 2
println(array[1] - 2)
println(array)
array[2] = array[1] - array[0]
println(array)