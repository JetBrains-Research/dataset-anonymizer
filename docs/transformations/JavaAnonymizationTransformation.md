# Java Anonymization Transformation

## Overview

To get more details (overview and naming scheme) see [Anonymization.md](Anonymization.md)
  
### Exceptions

- All methods with names `"equals", "main", "hashCode", "toString"`
  are not renamed. 
- **Note** all interfaces, which are used after `implement` keyword are not supported. 
For example, the piece of the code `public class Test2_0 implements Operationable` after this transformation
will be `public class c1 implements Operationable`.
**TODO**: add a link on the youtrack issue

## Examples
```
public abstract class A {

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public static void main() {
    }

    public int calculate(int x, int y) {
        return 0;
    }

    abstract public void test(int x, int y);
}

class B extends A {

    @Override
    public void test(int x, int y) {

    }
}

interface Operationable{
    int calculate(int x, int y);
}
```
is transformed to:
```
public abstract class c1 {

    @Override
    public boolean equals(Object equals_p1) {
        return super.equals(equals_p1);
    }

    public static void main() {
    }

    public int c1_f1(int c1_f1_p1, int c1_f1_p2) {
        return 0;
    }

    abstract public void c1_f2(int c1_f2_p1, int c1_f2_p2);
}

class c2 extends c1 {

    @Override
    public void c1_f2(int c1_f2_p1, int c1_f2_p2) {

    }
}

interface i1 {
    int i1_f1(int i1_f1_p1, int i1_f1_p2);
}
```
