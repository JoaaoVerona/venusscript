using std

object SimpleList(elements: []) {
    def getSize() {
        return size(elements)
    }

    def get(int index) {
        return elements[index]
    }

    def contains(any obj) {
        for element in elements {
            if element == object {
                return true
            }
        }

        return false
    }
}

array = [5, 3, -1, 2, 0]
test = new SimpleList(elements: array)
println("Created list: " + test)

for i in (0, size(array) - 1) {
    println("Get(" + i + ") -> " + test.get(i))
}

println("Size -> " + test.getSize())

for i in (0, 9) {
    println("Contains(" + i + ") -> " + test.contains(i))
}