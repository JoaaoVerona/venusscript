# VenusScript - Cryptography and engine libraries example.
# This script shows the 'crypto' and 'engine'
# library methods used together.
# With 'crypto' library giving a wide range set of
# methods to use, 'engine' library allows using the
# method 'hasFunction' and 'evaluate', demonstrated
# below.

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

# If, in this context, there is no function named
# {functionName} accepting a string (*string) as
# first argument and a variable reference (*var)
# as second and last argument, then prints a
# message and exits the script ('return' statement).
if !hasFunction(functionName, *string, *var) {
    println("Function \"" + functionName + "\" not found.")
    return
}

while true {
    print("Input > ")
    input = scan(*string)

    # After requesting the user input, we now must
    # call the selected cryptography function to do the
    # work. Since the cryptography functions have a
    # signature of two arguments: string and variable
    # reference, and they return a 'bool' value indicating
    # if the operation succeeded, then we can call this
    # function passing the 'input' variable value (which is
    # what the user typed) and a variable reference to,
    # for example, 'result', which will store the cryptography
    # result. Also, we then check if the result of the called
    # function was 'true' or 'false', via 'if evaluate(...)'.
    if evaluate(functionName + "(input, :result)") {
        println("Encrypted > " + result)
    }
    else {
        println("Failed to encrypt.")
    }
}