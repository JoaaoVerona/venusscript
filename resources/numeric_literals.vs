using std

binary = 0b11111111
decimal = 255
hexadecimal = 0xFF
println("Bin=" + binary + ", Dec=" + decimal + ", Hex=" + hexadecimal)
assert((binary == decimal) && (decimal == hexadecimal))