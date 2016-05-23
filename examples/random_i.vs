# VenusLibrary - Randomization example.
# This script turns into an interactive program,
# which asks repeatedly for a minimum value, a
# maximum value and a count modifier, then generating
# a random integer from {minimum} to {maximum} value
# {count} times.

using math
using std

while true {
    print("Min: ")
    min = scan(*int)
    print("Max: ")
    max = scan(*int)
    print("Count: ")
    count = scan(*int)

    for i in (1, count) {
        println("Result: " + randInt(min, max))
    }
}