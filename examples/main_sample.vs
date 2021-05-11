using std

ver = "1.3.0"
println("Hi from VenusScript! Version = " + ver)

def sum(int a, int b) {
    return a + b
}

newestSum = 0

async {
    for i in (1, 10) {
        newestSum = sum(i, i)
        println(newestSum)
    }
}

wait(newestSum == 20)
println("Ok!")
