package ua.valeriishymchuk.utils.stream;

import ua.valeriishymchuk.utils.stream.model.CheckedBiConsumer;
import ua.valeriishymchuk.utils.stream.model.CheckedBiFunction;
import ua.valeriishymchuk.utils.stream.model.CheckedConsumer;
import ua.valeriishymchuk.utils.stream.model.CheckedFunction;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;

public class StreamUtils {

    public static <T> Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
        AtomicInteger counter = new AtomicInteger(0);
        return item -> consumer.accept(counter.getAndIncrement(), item);
    }

    public static <T> Predicate<T> withCounter(BiPredicate<Integer, T> predicate) {
        AtomicInteger counter = new AtomicInteger(0);
        return item -> predicate.test(counter.getAndIncrement(), item);
    }

    public static <T> Consumer<T> checked(CheckedConsumer<T> consumer) {
        return obj -> {
            try {
                consumer.accept(obj);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };
    }

    public static <F, S> BiConsumer<F, S> checked(CheckedBiConsumer<F, S> biConsumer) {
        return (f, s) -> {
            try {
                biConsumer.accept(f, s);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };
    }

    public static <T, U> Function<T, U> checked(CheckedFunction<T, U> function) {
        return f -> {
            try {
                return function.apply(f);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        };
    }

}
