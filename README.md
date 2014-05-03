## Welcome to Yadi. 
###Yet Another Dependency Injection Container!

Yadi is a type safe Dependency Injection container built from the ground up with Java 8.0. It uses Java 8.0 features to do type safe object wiring.

Yadi eschews features for simplicity, and tries to apply that simplicity in several spheres of operation namely:
* It must have a small memory footprint, both in deployment and class footprint.
* It must not create unnecessary dependencies on problematic dependencies.
* It must be quick and easy to get up and running.
* You should be able to read this wiki page and know everything you need to know to work with Yadi.

To this end Yadi is developed with the following principles:
* The core Yadi container will never be more than 100Kb in size.
* The core Yadi container will only ever be dependant on core JDK libraries.
* Yadi will be pluggable enough to bring in your own extensions.
* Yadi will never tempt you to import a Yadi class to make things easy.
* In Yadi code is king
* In Yadi use of Annotations will be minimised.

***
## Getting started.

Yadi uses Java 8 features in order to do type safe dependency injection. Specifically it uses [Method References](http://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html) In order to do type safe wiring.

## Getting Yadi.
Currently Yadi is only available in Source code. However it's a relatively simple download just clone:

[https://github.com/jexenberger/yadi.git](https://github.com/jexenberger/yadi.git)

Yadi currently uses [Apache Maven](http://maven.apache.org/) to build. Once you've got Maven installed simply run:

`mvn clean install`



## Development Guide

Yadi has two main concepts, ObjectDefinitions and the Container. ObjectDefinitions provide a type safe fluent DSL for defining how objects should be wired together. The Container is a simple a context that Yadi uses to manage the creation and lifecycle of objects in the container.

### Using the container.
Containers are the main way of accessing managed objects in Yadi as well as managing their lifecycle. Furthermore they provide a hook which can be used to create and wire objects via the `build()` method which can be implemented.

The base container interface is:

`org.yadi.core.Container`

This class is used and extended by various different Container implementations. The simplest one to use is:

`org.yadi.core.DefaultContainer`

This class provides a default static method which takes a Lambda expression which can be used to create a container with managed objects. An example of this is as follows:
```java
import static org.yadi.core.DefaultContainer.create;
...
Container container = create((builder) -> {
   //code to create object definitions go here
   ....
});
```

Once you have the container you essentially have Yadi up and running, now it's time to add Objects.

### Using Object Definitions
Object Definitions are simply objects which are used to define how objects in the Yadi container should be wired.

The wiring is done using a fluent DSL which is exposed by the Object Definitions API.

The root object which provides the basic DSL features we are working with is:

`org.yadi.core.ObjectDefinition`

This class is further extended in the Container context to allow for injection of dependencies and is:

`org.yadi.core.InjectableObjectDefinition`

The classes are made available via the `define()` method(s) in the Container.

### Defining your Objects
The following is an example of creating a simple String object in the Container:

 ```java
import static org.yadi.core.DefaultContainer.create;
...
Container container = create((builder) -> {
   builder.define(String.class);
});
```

This example simple calls the `define(Class)` method to create an instance of a String.

Yadi allows you to look up all instances in the container via String, or if defined an specific Type. By default if no name is specified for the object, the fully qualified class name is used. Therefore to look up 
The `String` we just defined we do the following:

 ```java
import static org.yadi.core.DefaultContainer.create;
...
Container container = create((builder) -> {
   builder.define(String.class);
});
String myString = container.get("java.lang.String");
System.out.println("An empty string: "+myString);
```
### Using constructors
An empty String useless so Lets improve things a little by adding a value to the String constructor...
 ```java
import static org.yadi.core.DefaultContainer.create;
...
Container container = create((builder) -> {
   builder.define(String.class).addConstructorArg("hello world");
});
String myString = container.get("java.lang.String");
System.out.println("An non-empty string: "+myString);
```
Here are add a constructor value of `hello world` called the `addConstructorArg()` method to set the value of the string to `hello world`.

### Naming your objects
Now having a String named `java.lang.String` to uniquely address an object is probably not a great idea so lets name the object using `named(String)` method:
 ```java
import static org.yadi.core.DefaultContainer.create;
...
Container container = create((builder) -> {
   builder
     .define(String.class)    
     .addConstructorArg("hello world")
     .named("myString");
});
String myString = container.get("myString");
System.out.println("An non-empty string: "+myString);
```
Here we named the object `myString`.

### Setting Object values
So Strings are not very useful in a DI context, so lets create a bit more of a meaty object: `Person`. this class looks as follows:

```java
package org.yadi.core;

public class Person {

    String name;
    String surname;
    String fullName;
    Person spouse;


    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFullName() {
        return fullName;
    }

    public Person getSpouse() {
        return spouse;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    public void setup() {
        this.fullName = this.name + " " + this.surname;
    }
}

```
Lets create an instance of this Object called `jSmith` with name and surname `John Smith` respectively. To do this in Yadi we call the `set()` method(s) on the ObjectDefinition:

```java
import static org.yadi.core.DefaultContainer.create;
...
Container container = create((builder) -> {
   builder
     .define(Person.class)    
     .named("jSmith");
     .set(Person::setName, "John")
     .set(Person::setSurname, "Smith")
});
Person person = container.get("jSmith");
System.out.println(person.getName()+" "+person.getSurname());
```
### Using lifecycle methods
Person has a `fullName` property which is initialised from the name and the surname of the Person. However to do this we need to call the `setup()` method of the object. Yadi allows you to specify this by calling the `initWith()` method to specify which method you wish to call as follows:

 ```java
import static org.yadi.core.DefaultContainer.create;
...
Container container = create((builder) -> {
   builder
     .define(Person.class)    
     .named("jSmith");
     .set(Person::setName, "John")
     .set(Person::setSurname, "Smith")
     .initWith(Person::setup);

});
Person person = container.get("jSmith");
System.out.println(person.getFullName());
```

In addition to the `initWith` method, the following lifecycle methods are available:
* `createWith` : This provides a factory Lambda which can be used to create instances
* `destroyWith` : This provides a hook which can be used to 'destroy' the object at the end of it's life

### Injecting references to other objects in the Container
Finally the most powerful feature of a DI container is the ability to inject references to other objects in the container.

This is accomplished by calling the `inject` method on the ObjectDefinition. as follows:

 ```java
import static org.yadi.core.DefaultContainer.create;
...
Container container = create((builder) -> {
   builder
     .define(Person.class)    
     .named("sSmith");
     .set(Person::setName, "Sarah")
     .set(Person::setSurname, "Smith")
     .initWith(Person::setup);

   builder
     .define(Person.class)    
     .named("jSmith");
     .set(Person::setName, "John")
     .set(Person::setSurname, "Smith")
     .inject(Person::setSpouse, "sSmith")
     .initWith(Person::setup);


});
Person person = container.get("jSmith");
System.out.println(person.getSpouse().getFullName());
```

Here we have the Person 'sSmith' instance being injected into spouse property of 'jSmith'
 

