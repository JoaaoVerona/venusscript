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

print("Should instantly print OK: ")
async sleep(10000)
println("OK")