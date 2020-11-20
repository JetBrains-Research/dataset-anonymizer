# Java/Kotlin Anonymization: common part

## Overview

This transformation renames most named entities in a Java/Kotlin program to have
automatically generated names.

## Naming scheme

Each named entity is renamed to have the form
`parentName + "_" + kindPrefix + kindIndex`, where:

- `parentName` is the name of the entity's parent after renaming. 
  The parent is usually the
  most nested scope owner that the entity's definition resides in.
  For example, the parent of a class method is the corresponding class.
  Global entities do not have a parent and, for them, `parentName` is
  considered to be empty. In this case, the separator `_` is also omitted.
  
- `kindPrefix` is one of the following, depending on what kind of entity
  is being renamed:

    | Prefix | Kind                                          |
    | ------ | ----                                          |
    | `f`    | Function                                      |
    | `sf`   | Static (or Companion Object) function         |
    | `v`    | Variable                                      |
    | `c`    | Class                                         |
    | `f`    | Field                                         |
    | `sf`   | Static (or Companion Object) field            |
    | `p`    | Parameter                                     |
    | `i`    | Interface                                     |
    | `l`    | Lambda expression                             |
 
  Note: lambda expressions do not have a name, thus cannot be renamed. However,
  they are assigned a "name" in case it needs to be used as an entity's
  `parentName`.
  
- `kindIndex` is a 1-based index of all entities of this entity's kind within
  its parent scope.