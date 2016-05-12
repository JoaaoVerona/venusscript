using std

def countdown(int i) {
    println("i=" + i)
    i = i - 1
    countdown(i)
}

countdown(10)