package ua.valeriishymchuk.reporter.server.packet;

import ua.valeriishymchuk.reporter.network.Packet;
import ua.valeriishymchuk.reporter.network.model.ConnectionInfo;

import java.util.Optional;
import java.util.function.BiFunction;

public class PacketHandler<I extends Packet> {

    private final BiFunction<I, ConnectionInfo, ? extends Packet> function;
    private final Class<I> iClass;
    public PacketHandler(Class<I> iClass, BiFunction<I, ConnectionInfo, ? extends Packet> function) {
        this.function = function;
        this.iClass = iClass;
    }

    public Packet execute(I packet, ConnectionInfo info) {
        return function.apply(packet, info);
    }

    @SuppressWarnings("unchecked")
    public Packet executeRaw(Packet packet, ConnectionInfo info) {
        return function.apply((I) packet, info);
    }

    public boolean isAssignable(Class<?> clazz) {
        return iClass.isAssignableFrom(clazz);
    }

    @SuppressWarnings("unchecked")
    public Optional<? extends Packet> executeIfAssignable(Class<?> clazz, Packet packet, ConnectionInfo info) {
        System.out.println(isAssignable(clazz) + " " + clazz.getName() + " " + iClass.getName());
        if(!isAssignable(clazz)) return Optional.empty();
        return Optional.of(execute((I) packet, info));
    }

    public Class<I> getInputClass() {
        return iClass;
    }
}
