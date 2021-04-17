# sql-reader-lib
![GitHub Repo stars](https://img.shields.io/github/stars/eokasta/sql-reader-lib?color=orange&style=for-the-badge)
![GitHub issues](https://img.shields.io/github/issues/eokasta/sql-reader-lib?color=orange&style=for-the-badge)
![GitHub last commit](https://img.shields.io/github/last-commit/eokasta/sql-reader-lib?color=orange&style=for-the-badge)
![JitPack](https://img.shields.io/jitpack/v/github/eokasta/sql-reader-lib?color=orange&style=for-the-badge)

## Introduction
This project was created for study purposes, but also to be used for public or private projects. My plan is to keep it up to date and working in the best possible way, but for that I need the help of the community. If you encounter any problems, report by clicking [here](https://github.com/eokasta/sql-reader-lib/issues).

## Hooking into
You can get the latest version available on [JitPack](https://jitpack.io/#eokasta/sql-reader-lib).

### Gradle
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.eokasta:sql-reader-lib:VERSION'
}
```

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
    
<dependency>
    <groupId>com.github.eokasta</groupId>
    <artifactId>sql-reader-lib</artifactId>
    <version>VERSION</version>
</dependency>
```

## Examples

### Main
```java
private final ReaderAdapterMap readerAdapterMap = new ReaderAdapterMap(); /* If you want to create adapters */
private final ResultSetReader resultSetReader = new ResultSetReader(readerAdapterMap /* If you want to create adapters */);

private void registerAdapters() {
  readerAdapterMap.registerAdapter(
    String.class /* Type received */,
    ACoolClass.class /* Return type */,
    new ACoolClassAdapter() /* ReaderAdapter implemented */
  );
}

private AHappyClass parse(ResultSet resultSet) {
  return resultSetReader.parseSafe(
    resultSet /* ResultSet with query return values */,
    AHappyClass.class /* Class type to parse */
  ); /* Returns the instance of the AHappyClass class with the assigned attribute values */
}
```

### Adapter class
```java
public class ACoolClassAdapter implements ReaderAdapter<ACoolClass> {

  @Override
  public ACoolClass parse(Object object) {
    return /* Returns the ACoolClass instance */;
  }

}
```

### Object class
#### Example one
```java
@ReadClass /* All attributes present in the class will receive their due values, except those that contain @IgnoreField */
public class AHappyClass {
  
  private String name;
  private int age;
  @IgnoreField /* This attribute will not receive any value when it is instantiated by ResultSetReader */
  private Double balance;
  private ACoolClass coolClass;
  
  public AHappyClass() { } /* If there is a constructor with parameters, an overload must be made with another constructor without parameters */
  
}
```


#### Example two
```java
/* It is no longer necessary to use @IgnoreField in this type of example */
public class AHappyClass {
  
  @ReadField /* All attributes that contain @ReadField will receive values when they are instantiated by ResultSetReader */
  private String name;
  @ReadField
  private int age;
  private Double balance;
  @ReadField
  private ACoolClass coolClass;
  
  public AHappyClass() { } /* If there is a constructor with parameters, an overload must be made with another constructor without parameters */
  
}
```

For more examples, click [here](https://github.com/eokasta/sql-reader-lib/tree/master/src/test/java/com/github/eokasta/sqlreader/example).
