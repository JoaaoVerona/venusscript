# VenusScript - Dynamic library example.
# This is a fast script which demonstrates how
# some methods from 'dynamic' library can be used
# to 'inject' scripts and libraries at runtime.

using dynamic

dynamicUsing("std")
println("If this is printing out, then dynamicUsing() worked!")