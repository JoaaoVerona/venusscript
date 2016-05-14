using std

println("Printing 1 to 10...")

for i in (1, 30) {
    println(i)

    if i >= 7 {
        break
    }
}

for i in (8, 10) {
    println(i)
    continue
}

println("End!")