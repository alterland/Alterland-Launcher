-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }
# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}
# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}
# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}
# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations
-dontnote kotlinx.serialization.SerializationKt
# Keep Serializers
-keep,includedescriptorclasses class ru.alterland.**$$serializer { *; }
-keepclassmembers class ru.alterland.** {
    *** Companion;
}
-keepclasseswithmembers class ru.alterland.** {
    kotlinx.serialization.KSerializer serializer(...);
}
# When kotlinx.serialization.json.JsonObjectSerializer occurs
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

#Okhttp
-dontwarn javax.annotation.**
-keeppackagenames okhttp3.internal.publicsuffix.*
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Logback (Custom rules, see https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-logback-android.pro)
# to ignore warnings coming from slf4j and logback
-keep class ch.qos.** { *; }
-dontwarn ch.qos.**
-keep class org.slf4j.** { *; }
-dontwarn org.slf4j.**
-dontwarn ch.qos.logback.core.net.*

-ignorewarnings