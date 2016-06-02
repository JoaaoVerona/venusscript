using std

object SimpleList {
    int size: -55
    bool enabled : 1 + 1 == 2
}

println("Heh.")
test = new SimpleList(size: 5, enabled: false)
println("enabled=" + test.enabled + ", size=" + test.size)
test.size += 5
test.size += test.size
test.enabled = !test.enabled
println("enabled=" + test.enabled + ", size=" + test.size)
test = new SimpleList()
println("size=" + test.size + ", enabled=" + test.enabled)