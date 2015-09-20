## Signature hints


```java
public interface Specification<T> {
  boolean isSatisfiedBy(T value);
}
```

```java
public interface QueryMatcher {
  Specification<BookEntry> matcherFor(double price);
}
```


## Gherkin hints
