using std

object SimpleList(size: -55, elements, enabled: 1 + 1 == 2, parent) {
    def now() {
        println(size)
        size = 1024
        println(size)
    }
}

array = [5, 3, -1, 2, 0]
test = new SimpleList(size: size(array), elements: array)
test2 = new SimpleList(parent: test)
println("Test  --> " + test)
println("Test2 --> " + test2)