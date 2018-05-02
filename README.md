# Welcome to the repository

Here you can find a food diary desktop application, work in progress.

Completed functionality to date:

- possibility to create a username
  - username is saved to a database

- possibility to create a food with its nutritional information




## Documentation

[Conception](https://github.com/perander/otm-project/blob/master/FoodDiary/documentation/conception.md)

[Timeline](https://github.com/perander/otm-project/blob/master/FoodDiary/documentation/timeline.md)

[Architecture](https://github.com/perander/otm-project/blob/master/FoodDiary/documentation/architecture.md)


## Releases

[All releases](https://github.com/perander/otm-project/releases)

## Command line commands

### Generating the executable jar-file

You can generate the file with the command

```
mvn package
```

The file will be created to the target-directory. It can be run with the command

```
java -jar path/target/filename
```


### Running tests

The tests can be run with the command

```
mvn test
```

The test coverage report can be created with the command

```
mvn jacoco:report
```

You can see the report by opening the file target/site/jacoco/index.html with a browser.


### Checkstyle

To see the results of the checkstyle-tests defined in the checkstyle.xml -file, run the command

```
mvn jxr:jxr checkstyle:checkstyle
```

Possible errors are explained in a file target/site/checkstyle.html (open with a browser).

### JavaDoc

To see JavaDoc, run the command

```
mvn javadoc:javadoc
```

You can see the JavaDoc by opening the file target/site/apidocs/index.html






