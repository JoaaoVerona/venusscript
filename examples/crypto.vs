using crypto
using engine
using std

println("Select the cryptography function. Examples:")
println("* aes")
println("* base64")
println("* md5")
println("* sha256")
print("> ")
functionName = scan(*string)

if !hasFunction(functionName, *string, *var) {
    println("Function \"" + functionName + "\" not found.")
    return
}

while true {
    print("Input > ")
    input = scan(*string)

    if evaluate(functionName + "(input, :result)") {
        println("Encrypted > " + result)
    }
    else {
        println("Failed to encrypt.")
    }
}