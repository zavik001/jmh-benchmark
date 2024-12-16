# Benchmark Report

## Results

| Benchmark                                | Mode | Cnt | Score   | Error   | Units   |
|-----------------------------------------|------|-----|---------|---------|---------|
| MethodInvocationBenchmark.directAccess  | avgt |  30 |  0.642  | ± 0.004 | ns/op   |
| MethodInvocationBenchmark.lambdaMetafactory | avgt |  30 |  0.917  | ± 0.012 | ns/op   |
| MethodInvocationBenchmark.methodHandles | avgt |  30 |  5.146  | ± 0.035 | ns/op   |
| MethodInvocationBenchmark.reflection    | avgt |  30 |  7.718  | ± 0.117 | ns/op   |

## Explanation

### Columns
- **Benchmark**: The name of the test method.
- **Mode**: Measurement mode. `avgt` refers to average time per operation.
- **Cnt**: The number of iterations performed.
- **Score**: The average execution time per operation (in nanoseconds).
- **Error**: The margin of error for the score.
- **Units**: Units of measurement (nanoseconds per operation).

### Test Cases
1. **Direct Access**
   - **Score**: 0.642 ns/op
   - This is the fastest method, as it directly accesses the method without intermediaries.

2. **LambdaMetafactory**
   - **Score**: 0.917 ns/op
   - Slightly slower than direct access, as it involves generating a lambda expression to call the method.

3. **Method Handles**
   - **Score**: 5.146 ns/op
   - Slower than LambdaMetafactory due to the additional complexity in method resolution.

4. **Reflection**
   - **Score**: 7.718 ns/op
   - The slowest method because it requires permission checks and other overheads during method invocation.

## Suggestions

1. **Direct Access** should be used whenever possible for the best performance, especially if the structure is known.
2. For dynamic cases where flexibility is required, **LambdaMetafactory** should be the preferred approach, as it offers a good balance of performance and adaptability.
3. **Reflection** is best avoided in performance-critical code, as it introduces significant overhead.
4. **MethodHandles** can be used when advanced flexibility is required, despite its performance being inferior to LambdaMetafactory.