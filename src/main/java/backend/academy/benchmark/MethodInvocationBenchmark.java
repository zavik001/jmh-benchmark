package backend.academy.benchmark;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class MethodInvocationBenchmark {

    private Student student;
    private Method method;
    private MethodHandle methodHandle;
    private Function<Student, String> lambda;
    private final static String NAME = "name";

    @Setup
    public void setup() throws Throwable {
        // direct access
        student = new Student("Alexander", "Biryukov");

        // Reflection
        method = Student.class.getMethod(NAME);

        // MethodHandles
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        methodHandle = lookup.findVirtual(Student.class, NAME, MethodType.methodType(String.class));

        // LambdaMetafactory
        MethodType methodType = MethodType.methodType(String.class, Student.class);
        lambda = (Function<Student, String>) LambdaMetafactory.metafactory(
                lookup,
                "apply",
                MethodType.methodType(Function.class),
                methodType.generic(),
                methodHandle,
                methodType).getTarget().invokeExact();
    }

    @Benchmark
    public void directAccess(Blackhole bh) {
        String name = student.name();
        bh.consume(name);
    }

    @Benchmark
    public void reflection(Blackhole bh) throws Exception {
        String name = (String) method.invoke(student);
        bh.consume(name);
    }

    @Benchmark
    public void methodHandles(Blackhole bh) throws Throwable {
        String name = (String) methodHandle.invoke(student);
        bh.consume(name);
    }

    @Benchmark
    public void lambdaMetafactory(Blackhole bh) {
        String name = lambda.apply(student);
        bh.consume(name);
    }

    public record Student(String name, String surname) {
    }
}
