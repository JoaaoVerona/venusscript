# VenusScript - Numeric literals example.
# This script shows how to use numeric literals
# with varrying radix.

using std

binary = 0b11111111 # A binary int literal.
decimal = 255 # A decimal numeric (can be decimal or int) literal.
hexadecimal = 0xFF # A hexadecimal int literal.

println("Bin=" + binary + ", Dec=" + decimal + ", Hex=" + hexadecimal)
assert((binary == decimal) && (decimal == hexadecimal))