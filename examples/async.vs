# VenusScript - Asynchronization example.
# This script shows the capability of executing
# asynchronous code in a script, and also how to
# embed it with other containers.

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

sleep(100)

# This should instantly print OK because the 'sleep'
# call is inside an 'async' container. Furthermore,
# the script execution will NOT wait for the async
# container to finish, because it is marked as a
# 'daemon' thread.
print("Instant OK: ")
async daemon sleep(10000)
println("OK")