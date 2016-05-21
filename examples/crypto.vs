using crypto
using std

while true {
    print("Input > ")
    input = scan(*string)

    if base64(input, :result) {
        println("Encrypted > " + result)
    }
    else {
        println("Failed to encrypt.")
    }
}