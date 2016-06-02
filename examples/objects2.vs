using std

object SimpleList {
    int size: -55
    array elements
    bool enabled : 1 + 1 == 2
    SimpleList parent

    def now() {
        println(size)
        size = 1024
        println(size)

    }

    def eqq() {
        println("PRINT! Size: " + size + ", Enabled: " + enabled)
    }
}

test = new SimpleList()
println("Size: " + test.size)
test.eqq()
test.now()
println("Size: " + test.size)