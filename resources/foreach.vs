using std

println("Olá!")

# 0...2
{
    j = 2

    for i in (0, j) {
        println("i = " + i)
    }
}

# 3...9
{
    j = 3
    for i in (j, j + 6) {
        println("i = " + i)
    }
}

# 10...14
{
    k = 5 + (10 / 2)
    for i in (k, k - k + k + 5 - 1) {
        println("i = " + i)
    }
}

# 15, 17, 19, 21, 23, 25
{
    for i in (15, 25, i + 2) {
        println("i = " + i)
    }
}

# 16, 18, 20, 22, 24
{
    for i in (16, 24, i + 2) {
        println("i = " + i)
    }
}