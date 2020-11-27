# Dataset Anonymizer

An IntelliJ-based IDE plugin for code anonymization in datasets

- [Dataset Anonymizer](#dataset-anonymizer)
  - [Description](#description)
  - [Getting started](#getting-started)

# Description

The plugin deletes all comments in the code snapshots and anonymizes all variables, functions and any other names in the code.

The root folder with the data need to have the following structure:

```
-root
--language
---taskN1
----csv files
---taskN2
----csv files
```

Available languages:

- [x] Python
- [x] Java
- [ ] Kotlin (In progress, now only comments removal works)
- [ ] Cpp

Tasks folders can have any name. Csv file with the data need to have the `fragment` column with a code snapshot. 

The anonymization process description can be found here:

| Language | Comments removal       | Anonymization
| ------   | ----                   | ---- 
| `Python` | [Link](https://github.com/JetBrains-Research/ast-transformations/blob/master/docs/Anonymization.md) | TODO: add a link [Link]()
| `Java`   | [Link](/docs/transformations/CommentsRemoval.md) | [Link](/docs/transformations/Anonymization.md) and [Link](/docs/transformations/JavaAnonymizationTransformation.md)
| `Kotlin` | [Link](/docs/transformations/CommentsRemoval.md) | [Link](/docs/transformations/Anonymization.md) and TODO: add a link [Link]()


## Getting started

Just clone the repo by `git clone https://github.com/nbirillo/dataset-anonymizer.git` and run `./gradlew :cli -Pinput=/path/to/folder/with/dataset`.

To build a .zip distribution of the plugin run `./gradlew build shadowJar`. The .zip is located in `build/distributions/dataset-anonymizer-1.0-SNAPSHOT.zip`.

