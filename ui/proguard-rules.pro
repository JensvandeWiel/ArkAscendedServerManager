# Ignore duplicate resources that are common in JAR files
-dontnote **
-dontwarn **
-ignorewarnings

# Keep all public classes and methods for the main class
-keep public class ui.MainKt {
    public static void main(java.lang.String[]);
}

# Keep all classes that might be referenced via reflection
-keepclassmembers class * {
    @kotlin.jvm.JvmStatic <methods>;
}

# Preserve line numbers for debugging
-keepattributes SourceFile,LineNumberTable

# Keep all enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep serialization classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
