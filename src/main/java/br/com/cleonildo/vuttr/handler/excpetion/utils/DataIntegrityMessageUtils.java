package br.com.cleonildo.vuttr.handler.excpetion.utils;

/**
 * Utility class for handling data integrity exceptions.
 */
public class DataIntegrityMessageUtils {
    private DataIntegrityMessageUtils() {
    }

    /**
     * Returns the data integrity message for the given exception message.
     *
     * @param fullMessage the full exception message
     * @return the data integrity message
     */
    public static String getDataIntegrityMessage(String fullMessage) {
        var dataIntegrityKey = DataIntegrityKey.findByKey(fullMessage);
        return (dataIntegrityKey != null) ? dataIntegrityKey.getMessage() : fullMessage;
    }

    private enum DataIntegrityKey {
        USERS_EMAIL_KEY("users_email_key", "E-mail already registered"),
        USERS_CPF_KEY("users_cpf_key", "CPF already registered"),
        USERS_USERNAME_KEY("users_username_key", "Username already registered");

        private final String key;
        private final String message;

        DataIntegrityKey(String key, String message) {
            this.key = key;
            this.message = message;
        }

        public String getKey() {
            return key;
        }

        public String getMessage() {
            return message;
        }

        /**
         * Returns the data integrity key for the given key.
         *
         * @param key the key
         * @return the data integrity key
         */
        public static DataIntegrityKey findByKey(String key) {
            for (DataIntegrityKey value : values()) {
                if (value.key.equals(key)) {
                    return value;
                }
            }
            return null;
        }

    }
}
