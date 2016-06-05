using std

object SimpleList(size: -55, elements, enabled: 1 + 1 == 2, parent) {
    def now() {
        println(size)
        size = 1024
        println(size)

    }
}

test = new SimpleList()
assert(test.size == -55)
println("--> " + (test.size != -55))
assert(test.enabled)
test.now()
assert(test.size == 1024)
test.enabled = false
assert(!test.enabled)
println("Size: " + test.size)