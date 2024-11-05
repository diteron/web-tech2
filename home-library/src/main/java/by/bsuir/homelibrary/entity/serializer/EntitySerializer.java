package by.bsuir.homelibrary.entity.serializer;

import java.lang.reflect.Field;

import by.bsuir.homelibrary.dao.FileDelimiter;

public class EntitySerializer {
    private final static String DELIMITER = FileDelimiter.DELIMITER;

    public static String serialize(Object obj) {
        StringBuilder builder = new StringBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);

                if (value instanceof Enum<?>) {
                    builder.append(((Enum<?>) value).toString()).append(DELIMITER);
                } else {
                    builder.append(value).append(DELIMITER);
                }
            }

            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }

        }
        catch (IllegalAccessException e) {
            throw new SerializerException("Failed to serialize object of type " + obj.getClass().getName(), e);
        }

        return builder.toString();
    }

    public static <T> T deserialize(String data, Class<T> clazz) {
        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            String[] values = data.split(DELIMITER);
            Field[] fields = clazz.getDeclaredFields();
        
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Class<?> type = fields[i].getType();
        
                if (type.isEnum()) {
                    String enumValue = values[i];
                    Object enumConstant = type.getMethod("fromString", String.class).invoke(null, enumValue);
                    fields[i].set(obj, enumConstant);
                }
                else if (type == int.class || type == Integer.class) {
                    fields[i].set(obj, Integer.parseInt(values[i]));
                }
                else if (type == double.class || type == Double.class) {
                    fields[i].set(obj, Double.parseDouble(values[i]));
                }
                else if (type == boolean.class || type == Boolean.class) {
                    fields[i].set(obj, Boolean.parseBoolean(values[i]));
                }
                else {
                    fields[i].set(obj, values[i]);
                }
            }

            return obj;
        }
        catch (Exception e) {
            throw new SerializerException("Failed to deserialize data to " + clazz.getName(), e);
        }
    }
}
