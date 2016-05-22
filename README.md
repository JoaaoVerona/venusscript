# VenusScript
Welcome to the official GitHub repository of the dynamic, injectable, scripting language **VenusScript**.
See the [examples directory](https://github.com/BloodShura/VenusScript/tree/master/examples) to get a closer look in how the language works.

### Tech
VenusScript is meant to be used within a Java application, although it's implementation is, since the beginning, already being made targeting different languages in the future.
It is required Java 8+ and libraries:
- [ShuraX](todo): core dependency
- [ShuraX Cryptography](todo): required if enabling CryptoLibrary
- [ShuraX Dialogs](todo): required if enabling DialogsLibrary

### Definition
- Highly extensible
- Dynamic typing
- Context/scope manipulation
- No 'null' type/value
- Simple syntax
- Runtime injections (of libraries, overriding functions, new functions, ...)
- Multithreaded
- Easy to learn
- Decent performance

### Already implemented
- Asynchronous features: async. scopes
- Synchronous features: monitor locks (produce, consume), value waiting (e.g. ```wait(i == 5)```)
- Value types
- Function references
- Variable references
- Dynamic 'include' statements
- Dynamic 'using' statements
- Exporting variables to global context (so multiple scripts within the same application can access them)
- While, do-while containers
- Break, continue statements
- Ranged foreach container (e.g. ```for i in (0, n)```)
- Strongly typed function definitions (e.g. ```def print(string name) {...```)
- Basic standard libraries (random, input, output, cryptography, dialogs, math, runtime injection)
- Interpret source code at runtime (e.g. ```interpret("println(3 + 5 + 2 - 1)")```)
- Binary, decimal and hexadecimal literals
- Execute other scripts, asynchronously or synchronously (e.g. ```async run("../basic.vs")```)
- Useful libraries (rich cryptography ecosystem, dialogs, math, ...)
- Homogeneous arrays

### To be implemented
- Object-oriented programming (classes, attributes, methods)
- Functional programming (lambdas, utility methods)
- Collections (heterogeneous arrays, lists, maps, sets)
- Easier interoperability with Java
- Enumerations?
- User-defined value types
- Debugging features (including, but not limited to, *breakpoints*)
- More method libraries
- Simple exception handling?
- [... and many more...](https://github.com/BloodShura/VenusScript/issues?q=is%3Aissue+is%3Aopen+-label%3Abug)