
# MExecution (Method Execution)

Simplest library that logs how long a method took to execute

## Example of usages
```java
ORMUtils ormUtils = new ORMUtils();

Execution execution = Execution.create();

execution.logLevel(LogLevel.MAX);
execution.logReturn(true);
execution.timeFormat(TimeFormat.MILLISECONDS);

execution.single(() -> ormUtils.parse());
```
## Similar to:
```java
ORMUtils ormUtils = new ORMUtils();
        
Execution.create()
    .logLevel(LogLevel.MAX)
    .logReturn(true)
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
* `TimeFormat.MILLISECONDS`
* `TimeFormat.NANOSECONDS`

Time format is responsible for format in which execution time will be displayed in the log
```
Execution execution = Execution
    .create()
    .timeFormat(TimeFormat.SECONDS)
    .of(() -> callAllMethods());
```
Result: [Execution time: 4s]
```
execution
    .timeFormat(TimeFormat.NANOSECONDS)
    .of(() -> callAllMethods());
```
Result: [Execution time: 341ns]

`Default value: MILLISECONDS`
## Log return (`com.mexecution.Execution.logReturn()`)
By calling that method while `Execution` initialization you can controll that method must print result of callable method or not, example:
```
Execution execution = Execution
    .create()
    .logReturn(true);

execution.single(() -> ormUtils.parse());
```
Result log: `[Execution time: 34ms] [Return: User: Pedro, Id: 312]`

`Default value: false`

## Log time (`com.mexecution.Execution.logTime()`)
Tells `Execution` object that нщг need to enable/disable logging of execution time for each passed `Callable`
```java
Execution execution = Execution
    .create()
    .logTime(false)
    .of(() -> callAllMethods());
```
Result logs is empty

It can be used only in one case if you want to log only total execution time
```java
Execution execution = Execution
    .create()
    .logTime(false)
    .logTotalTime(true)
    .timeFormat(TimeFormat.SECONDS)
    .of(() -> callAllMethods());
```
Result: `[Total execution time: 32s]`

`Default value: true`

## Log total time (`com.mexecution.Execution.logTotalTime()`)
Enabled logging total execution time of all passed methods
```java
Execution execution = Execution
    .create()
    .logTotalTime(true)
    .timeFormat(TimeFormat.NANOSECONDS)
    .of(() -> callAllMethods());
```
Result:

`[Execution time: 32ms] [Start time: 1721609364111] [End time: 1721609364143]`

`[Execution time: 98ms] [Start time: 1721609364144] [End time: 1721609364242]`

`[Execution time: 11ms] [Start time: 1721609364243] [End time: 1721609364254]`

**[Total execution time: 141ms]**
## `Execution` methods
```java
void single(Callable<T> callable) // Operates with single method
void of(Callable<T>... callables) // Operates with array of methods
void of(List<Callable<?>> callables) // Operates with list of methods
void of(Collection<Callable<T>> callables) // Operates with collection of methods
```
