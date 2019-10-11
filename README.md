# java-error-manager
Trying an approach for handling errors in java
 
Beware ! The code is in an early stage an thus still a bit messy.

# Concept

## Problems
In java when you do a task that may fail, like division or looking 
for a value in a collection at a given index, there are two ways of 
dealing with the potential fail :

- throwing an exception
- returning null (but only when dealing with objects)

Those ways are okay, but I think there both have some problems.

For the exception case, if the exception is not caught, it can 
abruptly stop the program. There are of course checked exception
that help, but cactching can make the process way much heavyer,
like when dealing with division.

For the nil case, it first not always usable in java since 
primitives are not nullable (but I would say the problem is 
on java's side). Plus, is you have many acctions that can fail
you will have to check each time if you don't encounter nil,
which may be as heavier as capturing exception descripbed above.

## Solution ?
My proposal to this problem is made by two classes that are
united uder an abstract class. One is for describing success
and holds the result of the process, the other for errors and 
holds a `String` constaining the error message.

If you are familair with Scala, I think you could see this
as a variant of Either[A, String]. From an elm background this 
works a lot like the type Result.

Example case :
```java
public class Main {
    public static void main(String[] args) {
        Result.begin(42)                            // starts with 42
            .run(elem -> divide(elem, 2))           // divides by 2
            .show()                                 // show the result
            .run(elem -> divide((Integer)elem, 0))  // tries to divide by 0 and fails
            .run(elem -> divide((Integer)elem, 3))  // since error is not captured, does nothing
            .capture(message -> 42)                 // captures the error and rolls back to 42
            .run(elem -> divide((Integer)elem, 3))  // divides by 3
            .show();                                // finally show the result
    }

    public static Result<Integer> divide(Integer f, Integer s) {
        if (s != 0) {
            return new Success(f / s);
        }
        else {
            return new Error("Dividing by Zero");
        }
    }
}
```