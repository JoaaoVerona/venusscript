# VenusScript - Shell execute example.
# This script demonstrates how to use the 'shell'
# function to execute shell commands, in a new
# window or 'behind the scenes'.

using std

while true {
    print("Command: ")
    command = scan(*string)
    print("In new window (y/*)? ")
    inNewWindow = scan(*string) == "y"
    shell(command, inNewWindow)
}