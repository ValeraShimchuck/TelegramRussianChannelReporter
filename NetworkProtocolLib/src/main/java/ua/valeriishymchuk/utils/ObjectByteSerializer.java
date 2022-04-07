package ua.valeriishymchuk.utils;


import java.io.*;

public final class ObjectByteSerializer {

    public static byte[] toArray(Object obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] byteArray = new byte[0];
        try {
            out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(obj);
            out.flush();
            byteArray = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
                if(out != null) out.close();
            } catch (Exception ignored) {}
        }
        return byteArray;
    }

    public static Object fromArray(byte[] byteArray) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInput objectInput = null;
        Object object = null;
        try {
            objectInput = new ObjectInputStream(byteArrayInputStream);
            object = objectInput.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(objectInput != null) objectInput.close();
            } catch (Exception ignored) {}
        }
        return object;
    }

    private ObjectByteSerializer() {
        throw new UnsupportedOperationException();
    }

}
