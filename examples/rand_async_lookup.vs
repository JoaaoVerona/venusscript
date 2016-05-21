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