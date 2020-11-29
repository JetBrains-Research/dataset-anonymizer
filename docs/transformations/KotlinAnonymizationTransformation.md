# Kotlin Anonymization Transformation

## Overview

To get more details (overview and naming scheme) see [Anonymization.md](Anonymization.md)
  
### Rules

- All methods with names `"equals", "main", "hashCode", "toString"`
  are not renamed. Note, that `init` block is also not renamed.
- Methods and fields are considered as *static* if their nearest parent is an object or a companion object.

## Examples
```
public class A : Operationable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    fun main() { }


    override fun calculate(x: Int, y: Int): Int {
        return 0
    }
}

class B : A { }

interface Operationable {
    fun calculate(x: Int, y: Int): Int
}
```
is transformed to:
```
public class c1 : i1 {

    override fun equals(equals_p1: Any?): Boolean {
        return super.equals(equals_p1)
    }

    fun main() { }


    override fun i1_f1(i1_f1_p1: Int, i1_f1_p2: Int): Int {
        return 0
    }
}

class c2 : c1 { }

interface i1 {
    fun i1_f1(i1_f1_p1: Int, i1_f1_p2: Int): Int
}
```
