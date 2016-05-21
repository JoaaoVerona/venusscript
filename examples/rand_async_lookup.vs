# VenusScript - Asynchronous, locks and randomization example.
# This script is a small program which, given LOOKUP and MAXIMUM
# variables, it will, asynchronously and repeatedly, generate
# a random integer from 0 to MAXIMUM. Then, it will check if the
# generated integer is equal to LOOKUP; if yes, it will produce
# on the mutex variable 'lock'.
# Also, another asynchronous, and daemon, code will execute,
# one second per time (since it is sleeping for 1000ms in each
# iteration), printing how many integers were already generated.
# Meanwhile, in the main code, it will just wait for consume
# on the mutex variable 'lock', waiting for the asynchronous code
# to find the LOOKUP number.

using math
using std

MAXIMUM = pow(2, 20)
LOOKUP = MAXIMUM / 2

println("Randomizing 0~" + MAXIMUM + " until " + LOOKUP + " is found.")
println("Initializing...")
lock = 0
amount = 0
time = 0
end = false

async while true {
    value = randInt(0, MAXIMUM)
    amount += 1

    if value == LOOKUP {
        produce(:lock)
        end = true
        break
    }
}

async daemon while !end {
    sleep(1000)
    time += 1
    println("Processed " + amount + " elements in " + time + " seconds.")
}

println("Lookup...")
consume(:lock)
println("Found it! Bye.")