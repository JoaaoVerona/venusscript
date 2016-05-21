# VenusScript - Branches example.
# This script shows how to use branch components
# to manipulate a loop container's workflow.

using std

# This code will print from 1 to 7.
# Even though the for-container loops from
# 1 to 30, when it reaches 7, it calls
# 'break', which stops the workflow of this
# container.
for i in (1, 30) {
    println(i)

    if i >= 7 {
        break
    }
}

# This code will print from 8 to 10.
# Even though the for-container loops from
# -20 to 10, inside of it, any value lower or
# equal to 7 will call 'continue', which goes
# to the next step of the loop.
# Notice the additional 'continue' at the end,
# which have, in practice, no effect, since at
# the end of the scope the loop would continue
# it's workflow normally.
for i in (-20, 10) {
    if i <= 7 {
        continue
    }

    println(i)
    continue
}

println("End!")