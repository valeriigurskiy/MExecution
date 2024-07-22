
# MExecution (Method Execution)

Simplest library that logs how long a method took to execute

## Example of usages
```java
ORMUtils ormUtils = new ORMUtils();

Execution execution = Execution.create();
        
execution.logLevel(LogLevel.MAX);
execution.printResult();
execution.timeFormat(TimeFormat.MILLISECONDS);
        
execution.single(() -> ormUtils.parse());
```
## Similar to:
```java
ORMUtils ormUtils = new ORMUtils();
        
Execution.create()
    .logLevel(LogLevel.MAX)
    .printResult()
    .timeFormat(TimeFormat.SECONDS)
    .single(() -> ormUtils.parse());
```

## Log level (`com.mexecution.LogLevel`)
* `LogLevel.MIN`

Log sample: `[Execution time: 1236ms]`
* `LogLevel.MAX`

Log sample: `[Execution time: 3123ms] [Start time: 1721609364111] [End time: 1721609365348]`

`Default value: MIN`

## Time format (`com.mexecution.TimeFormat`)
* `TimeFormat.MINUTES`
* `TimeFormat.MILLOSECONDS`
* `TimeFormat.NANOS`

Time format is responsible for format in which execution time will be displayed in the log
```
[Execution time: 847ms]
or
[Execution time: 36s]
```

`Default value: MILLISECONDS`
## Print result (`com.mexecution.Execution.printResult()`)
By calling that method while `Execution` initialization you can controll that method must print result of callable method or not, example:
```
Execution execution = Execution
    .create()
    .printResult();

execution.single(() -> ormUtils.parse());
```
Expected log: `[Execution time: 34ms] [Result: User: Pedro, Id: 312]`

In case you don’t need to log result of executed method, just don’t call `printResult()` in the initialization chain :)

## `Execution` methods
```java
void single(Callable<T> callable) // Operates with single method
void of(Callable<T>... callables) // Operates with array of methods
void of(List<Callable<T>> callables) // Operates with list of methods
```
