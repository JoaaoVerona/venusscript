# VenusScript - Interpret example.
# This script turns into a simple
# 'interactive console' where you can
# input, line-by-line, a script code.

using engine
using std

while true {
    line = scan(*string)
    interpret(line)
}