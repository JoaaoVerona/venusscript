# VenusScript - Waiting example.
# This script shows how to use 'wait' function,
# for waiting until a given expression resolves into
# 'true'.

using std

i = 0

async {
    println("T2: 3")
    sleep(1000)
    println("T2: 2")
    sleep(1000)
    println("T2: 1")
    sleep(1000)
    println("T2: changing i...")
    i = 5
}

println("T1: waiting for i to be changed...")
wait(i == 5)
println("T1: done")