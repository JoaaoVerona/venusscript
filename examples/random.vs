using std

while true {
    print("Min: ")
    min = scan(*int)
    print("Max: ")
    max = scan(*int)

    for i in (0, 10) {
        println("Result: " + randInt(min, max))
    }
}