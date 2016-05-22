# VenusScript - Arrays example
# This script shows a basic example of
# how to use arrays (currently homogeneous).

using math
using std

array = [5, 3, "abc", -2]
println(array)
array[0] = 333
println(array)

array = newArray(10, 0)
println(array)
println("Size: " + size(array))

println("Generating random array...")
array = newArray(5, 0)

for i in (0, size(array) - 1) {
    array[i] = randInt(0, 1000)
}

print("Random array:")

for i in array {
    print(i + " ")
}

println()