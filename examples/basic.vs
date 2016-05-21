# Venus Script - Basic example.
# This script shows a basic program, creating
# a simple function (here called 'definition')
# and executing it.

# This is a comment!

using std # Uses library 'std'.

# Creates a new definition called 'printMyName',
# which receives a single argument called 'name'
# of type 'string'.
def printMyName(string name) {
    # Calls the method 'println' imported from library
    # 'std', concatenating the argument 'name' with
    # string literal.s
    println("AAAND HIS NAME IS " + name + "!")
}

# Calls the created definition, passing
# 'João Vitor Verona Biazibetti' as argument.
printMyName("João Vitor Verona Biazibetti")