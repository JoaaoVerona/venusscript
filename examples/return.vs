# VenusScript - Return values example.
# This script shows a small example of how
# to return values from definitions.

using std

def completeName(string name) {
    return name + " Verona Biazibetti"
}

def sum(int a, int b) {
    return a + b - 1 + 1
}

name = completeName("João Vitor")
println(name + " :: " + sum(10, 5))