# VenusScript - Global variables example.
# This script demonstrates how to use global
# variables, for using in all the scripts which
# are created within the same ApplicationContext,
# or event between asynchronous containers.

using std

export test = 0

async do {
    sleep(1000)
    produce(:$test)
    println("Produced!")
} while true

println("Idle.")

while true {
    consume(:$test)
    println("Consumed!")
}