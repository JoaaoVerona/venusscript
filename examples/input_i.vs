# VenusScript - Basic input example.
# This script simply asks the user to specify
# a value type, and a value. Then it prints them.

using std

while true {
    print("Input type: ")
    type = scan(*type)
    print("Input: ")
    input = scan(type)
    println("Value: " + input + " (type: " + (*input) + ")")
}