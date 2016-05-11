using std

def countdown(int i) {
    println("i->" + i)
    i = i - 1
    sleep(500)
    countdown(i)
}

def printMyName(string name) {
    println("Hello, my name is " + name + "!")
}

def main() {
    i = 3
    j = 7
    println("Olá! i -> " + i + ", j -> " + j + ".")
    printMyName("João Vitor")
    println("Hasta" + " " + "la" + " " + "vista" + ".")
    println("Iniciando countdown...")
    sleep(1500)
    countdown(10)
}

main()