using std

def countdown(int i) {
    println("i=" + i)
    i = i - 1
    sleep(500)
    countdown(i)
}

def main() {
    println("Iniciando countdown...")
    sleep(1500)
    countdown(10)
}

main()