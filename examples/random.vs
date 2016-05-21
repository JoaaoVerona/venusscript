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