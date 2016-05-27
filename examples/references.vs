# VenusScript - Function references example.
# This script shows how to use function references
# (a.k.a. callbacks), which are tokenized with '@'.

using math
using std

def plusSupername(string name) {
    function = @println
    function(name + " Verona Biazibetti")
}

def sendName(string name, ref callback) {
    callback(name)
}

sendName("João Vitor", @plusSupername)

################

def tryout(ref callback) {
    return callback(2, 20)
}

println("Rand int=" + tryout(@randInt))