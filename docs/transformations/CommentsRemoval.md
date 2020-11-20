# Java/Kotlin Comments Removal

## Overview

This transformation deletes all comments in the Java and Kotlin code. 

## Algorithm

Find all comments and delete them. The supported comments type:

- [x] Single line comment (starts with the `//` character)

- [x] Multi lines comment with using `/*...**/`

- [x] Documentation comment with using `/**...*/`

## Examples (in Java, for Kotlin they are similar))

<details><summary>Single line comment example</summary>
<p>

Code before transformation:

```
// Single line comment
// Single line comment
public class Test {
    // Single line comment
    // Single line comment
    // Single line comment
}
```

Code after transformation:

```
public class Test {
}
```

</p>
</details>


<details><summary>Multi lines comment with using `/*...**/` example</summary>
<p>

Code before transformation:

```
public class Test {
    /*
    This is a comment
    written in
    more than just one line
    * */
    /*
    This is a comment
    written in
    more than just one line
    * */
    /*
    This is a comment
    written in
    more than just one line
    * */
}
```

Code after transformation:

```
public class Test {
}
```

</p>
</details>

<details><summary>Documentation comment with using `/**...*/` example</summary>
<p>

Code before transformation:

```
/**
 This
 is
 documentation
 comment
 */
public class Test {
}
```

Code after transformation:

```
public class Test {
}
```

</p>
</details>

