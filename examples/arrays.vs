# VenusScript - Arrays example
# This script shows a basic example of
# how to use arrays (currently homogeneous).

using std

array = [5, 3, "abc", -2]
println(array[0] + ", " + array[1] + ", " + array[2] + ", " + array[3])
array[0] = -333
println(array[0] + "~" + array[1] + "~" + array[2] + "~" + array[3])