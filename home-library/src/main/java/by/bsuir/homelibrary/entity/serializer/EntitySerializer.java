package by.bsuir.homelibrary.entity.serializer;

import java.lang.reflect.Field;

import by.bsuir.homelibrary.dao.FileDelimiter;

/**
 * Utility class for serializing and deserializing objects.
 * <p>
 * The {@code EntitySerializer} class provides static methods to convert an object
 * into a string representation and to reconstruct an object from its serialized form.
 * The class uses reflection to access and manipulate object fields.
 * </p>
 * <p>
 * The default delimiter for serialized data is defined in {@code FileDelimiter.DELIMITER}.
 * </p>
 */
public class EntitySerializer {
    private final static String DELIMITER = FileDelimiter.DELIMITER;

    /**
     * Serializes the given object to a delimited string.
     * <p>
     * Each field in the object is converted to a string representation, with enum values
     * using their {@code toString()} method. Fields are separated by a specified delimiter.
     * </p>
     *
     * @param obj the object to serialize
     * @return a string representing the serialized form of the object
     * @throws SerializerException if an error occurs during serialization
     */
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

    /**
     * Deserializes a delimited string into an object of the specified class.
     * <p>
     * Fields are set according to the order they appear in the serialized data.
     * Enum fields require a static {@code fromString(String)} method in the enum type
     * to convert strings back into enum instances.
     * </p>
     *
     * @param <T>   the type of the object to deserialize
     * @param data  the serialized string representation of the object
     * @param clazz the class of the object to deserialize
     * @return an instance of {@code T} with fields populated from the serialized data
     * @throws SerializerException if an error occurs during deserialization
     */    
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
