using std

def plusSupername(string name) {
    function = @println
    function(name + " Verona Biazibetti")
}

def sendName(string name, ref callback) {
    callback(name)
}

sendName("João Vitor", @plusSupername)