# VenusScript - Types example.
# This script is used for testing purposes.
# Basically, shows the value type of some variables,
# and asserts they are correct.

using std

a = "I <3 COMPUTER SCIENCE!"
b = 5.3547
c = -33
d = 'o'
e = false
f = true
g = *string
h = *any

println(*a)
println(*b)
println(*c)
println(*d)
println(*e)
println(*f)
println(*g)
println(*h)
println("Running assertions...")
assert((*a) == (*string))
assert((*b) == (*decimal))
assert((*c) == (*int))
assert((*d) == (*string))
assert((*e) == (*bool))
assert((*f) == (*bool))
assert((*g) == (*type))
assert((*h) == (*type))