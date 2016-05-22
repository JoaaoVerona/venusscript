# VenusScript - Run script example.
# This script shows how to execute other
# scripts, asynchronously or synchronously.

using engine
using std

println("Executing basic.vs...")
run("../basic.vs")
println("Done.")
println("Executing asynchronously input.vs...")
async run("input.vs")

while true {
    println("I'm still here.")
    sleep(2000)
}