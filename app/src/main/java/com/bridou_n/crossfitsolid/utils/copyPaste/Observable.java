package rx;

/**
 *
 * Have to provide fake Observable
 * Related with bug Jackson and RxJava
 * https://realm.io/docs/java/latest/#jackson-databind
 * https://github.com/FasterXML/jackson-databind/issues/1070
 */

public class Observable {
    public interface OnSubscribe {
    }
}