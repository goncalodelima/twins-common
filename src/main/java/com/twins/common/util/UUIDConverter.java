package com.twins.common.util;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDConverter {

    public static byte[] convert(UUID uuid) {

        if (uuid == null) {
            return null;
        }

        return ByteBuffer.wrap(new byte[16])
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits())
                .array();
    }

    public static UUID convert(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

}
