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