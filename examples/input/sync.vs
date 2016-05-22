# VenusScript - Synchronization example.
# This script shows how to use some synchronization
# features. In this example, 'consume' and 'produce'
# are used (which resolves the producer-consumer problem).

using std

mutex = 0

async while true {
    sleep(1000)
    produce(:mutex)
}

while true {
    consume(:mutex)
    println("Produced!")
}