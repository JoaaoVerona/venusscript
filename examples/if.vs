# VenusScript - If container example.
# This script shows some variations of
# usages for 'if' containers.
# Should be self explanatory if you already
# understand expressions.

using std

i = 5

if i >= 0 {
    println("Yeah!")
}

if i <= 0 {
    println("Nope...")
}

j = i == 5

if j {
    println("Yeah!")
}
else if !j {
    println("Nope...")
}

if !!j {
    println("Yeah!")
}
else if j == false {
    println("Nope...")
}

if j == false {
    println("Nope...")
}
else if j == (i == 5) {
    println("Yeah!")
}

if !j {
    println("Nope...")
}
else {
    println("Yeah!")
}

if j == 2 {
    println("Nope...")
}
else if j == 3 {
    println("Nope...")
}
else {
    println("Yeah!")
}