package ua.valeriishymchuk.reporter.network;

import ua.valeriishymchuk.utils.ObjectByteSerializer;

import java.io.Serializable;

public abstract class Packet implements Serializable {

    public byte[] serialize() {
        return ObjectByteSerializer.toArray(this);
    }

}
