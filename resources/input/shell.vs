using std

print("Command: ")
command = scan(*string)
print("In new window (y/*)? ")
inNewWindow = scan(*string) == "y"
shell(command, inNewWindow)