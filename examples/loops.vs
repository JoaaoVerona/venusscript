# VenusScript - Loop containers example.
# This script shows some variations of
# usages for loop containers:
# - do-while
# - for
# - while
# Should be self explanatory if you already
# understand resultors.

using std

# 0...2
{
    i = 0

    while i < 3 {
        println("i = " + i)
        i = i + 1
    }
}

# 3...9
{
    j = 3
    for i in (j, j + 6) {
        println("i = " + i)
    }
}

# 10
{
    k = 5 + (10 / 2)
    for i in (k, k - k + k) {
        println("i = " + i)
    }
}

# 11...12
{
    i = 11

    # NOTE: 'do' containers, without a trailing
    # 'while' keyword, are just like a simple
    # container; it is, they DO NOT loop.
    do {
        println("i = " + i)
        i = i + 1
        println("i = " + i)
    }
}

# 13...14
{
    i = 13

    do {
        println("i = " + i)
        i = i + 1
    }
    while i < 15
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