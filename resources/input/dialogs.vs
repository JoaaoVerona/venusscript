using dialogs

while true {
    infoDialog("Hello!", "Welcome to dialogs.vs.")
    name = inputDialog("Try it.", "Please, insert your name:")

    if name == "João Vitor" {
        infoDialog("Yay!", "Hello, my creator!")
    }
    else {
        warnDialog("Oh...", "You are not my developer :(.", "", "", "(new lines are so cool)")

        if askDialog("Oh...", "Do you want to try again?") {
            continue
        }

        break
    }
}

dialog("Well...", "Bye bye!")