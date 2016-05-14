using std

println("Printing 0 to 9 and 10 to 19 async, and 20 to 29 normally...")

async for i in (0, 9) {
    println(i)
}

async {
    for i in (10, 19) {
        println(i)
    }
}

for i in (20, 29) {
    println(i)
}

sleep(1000)
print("Should instantly print OK: ")
async daemon sleep(10000)
println("OK")
println("Execution should be finished, since sleep is daemon.")