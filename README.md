# VenusScript
Welcome to the official GitHub repository of the dynamic, injectable, scripting language **VenusScript**.
See the [examples directory](https://github.com/BloodShura/VenusScript/tree/master/examples) to get a closer look in how the language works.

```
using std

ver = "1.3.0"
println("Hi from VenusScript! Version = " + ver)

def sum(int a, int b) {
    return a + b
}

newestSum = 0

async {
    for i in (1, 10) {
        newestSum = sum(i, i)
        println(newestSum)
    }
}

wait(newestSum == 20)
println("Ok!")
```

## Version Planning
| Version | State       | Description             |
| ------- | ----------- | ----------------------- |
| 0.x     | *done*      | <ul><li>Definitions</li><li>Functions</li><li>Function references</li><li>Standard library</li><li>Loop containers</li><li>Branching components</li><li>Variable references</li><li>Initial dynamic injection</li><li>Runtime interpretation</li><li>Asynchronous features</li><li>Synchronous features</li><li>Homogeneous arrays</li> |
| **1.x** | **current** | <ul><li>Object-oriented features (classes, attributes, methods, maybe polymorphism, etc)</li><li>Java interoperability</li><li>Lists</li><li>Dictionaries/Maps</li><li>Heterogeneous arrays</li> |
| 2.x     | planned     | <ul><li>Functional features</li><li>Enumerations</li><li>Debugging utilities and breakpoints</li> |
| ?     | planned     | <ul><li>Improved performance</li><li>Own bytecode compiler and interpreter |

## How to use

##### With Gradle:

```
dependencies {
	compile 'com.github.bloodshura:venusscript:1.3.0'
}
```

##### With Maven:

```
<dependency>
	<groupId>com.github.bloodshura</groupId>
	<artifactId>venusscript</artifactId>
	<version>1.3.0</version>
</dependency>
```

##### With other build systems:

If your build system supports fetching dependencies from the Maven central repository, then just include a dependency with group `com.github.bloodshura`, artifact `venusscript` and version `1.3.0`.

## Tech
VenusScript is meant to be used within any Java or JVM-built (Kotlin, Scala, Groovy, etc) application. It is required Java 8+.

## Definition
- Highly extensible
- Dynamic typing
- Context/scope manipulation
- No 'null' type/value
- Simple syntax
- Runtime injections (of libraries, function overriding, function definition, etc)
- Multithreaded
- Extremely easy syntax

## Already implemented
- Asynchronous features: `async` scopes
- Synchronous features: monitor locks (`produce`, `consume`), value waiting (e.g. ```wait(i == 5)```)
- Value types
- Function references
- Variable references
- Top-level functions
- Higher-order functions
- Dynamic `include` statements
- Dynamic `using` statements
- Exporting variables to global context (so multiple scripts within the same application can access them)
- While, do-while containers
- Break, continue statements
- Ranged foreach container (e.g. ```for i in (0, n)```)
- Strongly typed function definitions (e.g. ```def print(string name) {...```)
- Basic standard libraries (random, input, output, cryptography, dialogs, math, runtime injection)
- Interpret source code at runtime (e.g. ```interpret("println(3 + 5 + 2 - 1)")```)
- Binary, decimal and hexadecimal literals
- Execute other scripts, asynchronously or synchronously (e.g. ```async run("../basic.vs")```)
- Homogeneous arrays

## To be implemented
- Object-oriented programming (classes, attributes, methods, maybe polymorphism, etc)
- Functional programming (lambdas, data immutability, etc)
- Collections (heterogeneous arrays, lists, maps, sets)
- Easier interoperability with Java
- User-defined value types
- Debugging features (including, but not limited to, *breakpoints*)
- Expand standard libraries
- Enumerations
- Simple exception handling?
- [... and many more...](https://github.com/BloodShura/VenusScript/issues?q=is%3Aissue+is%3Aopen+-label%3Abug)

## Documentation

*Work in progress.*
