using std

i = 5

if i >= 0 {
    println("Sim.")
}

if i <= 0 {
    println("Não.")
}

j = i == 5

if j {
    println("Sim.")
}
else if !j {
    println("Não.")
}

if !!j {
    println("Sim.")
}
else if j == false {
    println("Não.")
}

if j == false {
    println("Não.")
}
else if j == (i == 5) {
    println("Sim.")
}

if !j {
    println("Não.")
}
else {
    println("Sim.")
}

if j == 2 {
    println("Não.")
}
else if j == 3 {
    println("Não.")
}
else {
    println("Sim.")
}