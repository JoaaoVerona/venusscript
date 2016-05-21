# VenusScript - Recursion example.
# This script shows a simple example of
# recursive functions.

using std

def countdown(int i) {
    println("i=" + i)
    i = i - 1

    if i >= 0 {
        countdown(i)
    }
}

countdown(10)