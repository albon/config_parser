# 背景

为了解决配置解析中的一些痛点，开发了一个配置解析框架。我们先说一下，老的的配置解析方式有哪些问题？老的配置解析代码示例，如下所示：

```java
	private List<String> personList;
    private int age;
    private double rate;

    public void reloadConfig(Map<String, String> configMap) {
        try {
            LOG.info("personList before reload: {}", personList);
            personList = Splitter.on(",").trimResults().omitEmptyStrings()
                    .splitToList(configMap.get("person.list"));
            LOG.info("personList after reload: {}", personList);

            LOG.info("age before reload: {}", age);
            age = Integer.valueOf(configMap.get("age"));
            LOG.info("age after reload: {}", age);

            LOG.info("rate before reload: {}", rate);
            rate = Double.valueOf(configMap.get("rate"));
            LOG.info("rate after reload: {}", rate);
        } catch (Exception e) {
            LOG.error("reload config error", e);
            throw new RuntimeException("reload config error", e);
        }
    }
```

每一个配置解析需要三行：打印配置之前的日志、根据配置更新变量、打印配置之后的日志。这段代码有啥不好的呢？

1. 当配置很多时，各种解析代码放在一起，reload 方法会变得又臭又长，可读性差。
2. 相似代码很多，比如打印变量更新前后的值。

为了解决上面提到的问题，我们写了一个框架，来简化配置解析工作。

# 设计与实现

## 注解标记关键信息

Java 中的注解十分简单，却又无比强大。注解可以用在 Class、Field、Method 等多种类型上面，用来向编译器传递一些信息，可以看做注释的“强力升级版”。注解是一种“被动”的信息，必须由编译器或虚拟机来“主动”解析它，它才能发挥自己的作用。

配置解析框架的基本原理是利用注解 ParseInfo 来标记配置解析信息，其中最基本的是变量对应的配置 key。

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParseInfo {
    // field 对应的配置的 key。
    String key();
 
    // 解析类型，单个kv解析，或者一组kv解析。
    ParserType type() default ParserType.SINGLE;
 
    // 对应的解析类，默认为 InvalidFieldParser，则根据变量类型和解析类泛型类型自动匹配。
    Class<? extends FieldParser> parser() default NullFieldParser.class;
}
```

下面的代码表示 personList 变量对应的配置的 key 是 person.list，自动匹配类型和解析器。

```java
    @ParseInfo(key = "person.list", type = ParserType.SINGLE)
    private List<String> personList;
```

大多数时候，变量和配置是一对一行的关系，有时候也会有一个变量对应多行配置的情况，所以还需要在注解中标记这个对应关系。比如下面的代码表示变量 personAgeMap 对应多行配置，每一行配置都带有 person_age_ 关键字，采用 AgeMapParser 类来解析配置。

```java
    @ParseInfo(key = "person_age_", type = ParserType.MULTI, parser = AgeMapParser.class)
    private Map<String, Integer> personAgeMap = Maps.newHashMap();
```

## 自定义配置解析规则

如何将配置中 String 类型的 value 赋值于 Java 中的一个对象呢？如果对象是 int、boolean、long 等基本类型，一般都有约定俗成的规则，比如使用 Integer.valueOf 来实现 int 类型对象的赋值。如果对象是自定义的复杂类型，如何转化呢？

为了方便用户自定义配置解析规则，我们提供了一套配置解析的抽象类。如下所示，分别是配置解析接口 FieldParser，处理一个 key/value 对应一个对象情况的 SingleKeyFieldParser，处理多个 key/value 对应一个对象情况的 MultiKeyFieldParser。

```java
public interface FieldParser {
}
 
// TypeCapture 的存在是为了在运行时拿到泛型参数类型
public abstract class SingleKeyFieldParser<T> extends TypeCapture<T> implements FieldParser {
    /**
     * 输入一个配置value，返回解析后的对象
     *
     * @param value 输入参数
     * @return
     */
    public abstract T parse(String value);
}
 
 
public abstract class MultiKeyFieldParser<T> extends TypeCapture<T> implements FieldParser {
    /**
     * 输入多个配置参数，返回一个解析后的结果
     *
     * @param configMap 配置参数map
     * @param key 配置关键字
     * @return
     */
    public abstract T parse(Map<String, String> configMap, String key);
}
```

解析模板的具体用法，可以看下面 Integer 类型字段的解析。像 int、boolean、String 等基本类型的解析方法，在不同系统中大多都是一样的，因此可以由框架来提供一些默认的解析类实现。

```java
public class IntFieldParser extends SingleKeyFieldParser<Integer> {
    @Override
    public Integer parse(String value) {
        return Integer.valueOf(value);
    }
}
```

**以框架来强制不同的解析代码放到不同的类里，可以让解析代码更加的高内聚，更清晰。**

默认情况下，框架会根据变量类型，和解析类里的泛型参数类型 T 自动匹配变量对应的解析类型。但是，万一出现多个匹配的解析类时，怎么办呢？可以通过注解 ParseInfo 里的 parser 属性来指定，以下代码表示以 AgeMapParser 类来解析配置。

```java
    @ParseInfo(key = "person_age_", type = ParserType.MULTI, parser = AgeMapParser.class)
    private Map<String, Integer> personAgeMap = Maps.newHashMap();
```

## 支持 Bean Validation

框架内置了参数校验功能，实现上使用了 Apache BVal， Apache BVal 是实体数据验证 Java Bean Validation 的一个简单易用的实现。直接在变量上使用参数校验的注解时，在解析配置时，会先做参数校验，如果校验失败，则不会真正的给变量赋值，会抛出异常，终止配置解析。

下面的例子，表示年龄 age 最小值为 1。

```java
@Min(1)
@ParseInfo(key = "age")
private int age;
```

## ConfigParser 解析器

ConfigParser 负责最终的解析配置和赋值，主要包含一个解析方法 parse：

```java
public class ConfigParser {
    /**
     * 解析配置和赋值
     * @param configMap             配置 map
     * @param configObject          待赋值的属性所在的类
     * @throws ConfigParseException
     */
    public static void parse(Map<String, String> configMap, Object configObject) throws ConfigParseException;
}
```

具体用法如下所示：

```java
// 全局配置类
public class CommonConfig {
    private static final CommonConfig INSTANCE = new CommonConfig();

    @ParseInfo(key = "person_age_", type = ParserType.MULTI, parser = AgeMapParser.class)
    private Map<String, Integer> personAgeMap = Maps.newHashMap();

    @Min(1)
	@ParseInfo(key = "age")
	private int age;
}
// 监听配置更新并解析的类
public class ConfigListener {
    public void reload(Map<String, String> configMap) throws ConfigParseException {
        ConfigParser.parse(configMap, CommonConfig.getInstance());
    }
}
```

## 类型自动匹配

如何根据待解析字段类型找到对应的 FieldParser 解析实现类呢？如何自动匹配字段类型与解析实现类的泛型参数类型呢？

1. 可以通过注解处理器在编译时获取泛型真实类型信息，具体可以看[这篇文章](https://blog.csdn.net/hustspy1990/article/details/78011301)。
2. 对于解析实现类的泛型参数类型，可以参考 Jackson 反序列化的实现，
3. 通过类的 signature 属性获取类型信息。
	- Java泛型的擦除并不是对所有使用泛型的地方都会擦除的，部分地方会保留泛型信息。比如 java.lang.reflect.Field 类中有一个 signature 属性保存了泛型的参数类型信息，通过 Field 的 getGenericType 方法即可得到。
	- 当然，这种方法仅限于类中的属性，对于方法中的局部变量无能为力。

更详细的解决办法，可以看文章[深入理解 Java 泛型：类型擦除、通配符、运行时参数类型获取](https://blog.csdn.net/hustspy1990/article/details/78048493#%E8%BF%90%E8%A1%8C%E6%97%B6%E6%B3%9B%E5%9E%8B%E5%8F%82%E6%95%B0%E7%B1%BB%E5%9E%8B%E8%8E%B7%E5%8F%96)。

**如果某一类型的解析实现类出现了多个，那么对该类型的自动匹配就不会生效了，需要通过 ParserInfo 注解的 parser 属性手动指定。**

## 安全保障

如果配置改错了怎么办？

1. 在框架中，包含完整的日志和监控，方便找到问题原因，比如打印变量赋值前后的值，解析异常时的监控和日志。
2. 为了能够在解析异常时，尽快发现问题，解决问题，还可以给应用开发者发生报警信息（IM 或短信）。报警消息包含用于定位分析问题的关键信息：应用标识、机器 ip、异常配置行、异常堆栈。
3. 解析异常时，自动回滚配置到上一个版本，实现故障的自动恢复。

## 扩展功能

如果我们想要增加一个功能，在配置更新以后触发一些操作，比如用于标记线程池大小的配置更新后，触发线程池的变化，该怎么做呢？

只需要在注解 ParserInfo 上增加一个字段，描述更新后需要调用的接口，让 ConfigParser 解析器识别此接口并调用即可。

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParseInfo {
    // field 对应的配置的 key。
    String key();
 
    // 解析类型，单个kv解析，或者一组kv解析。
    ParserType type() default ParserType.SINGLE;
 
    // 对应的解析类，默认为 InvalidFieldParser，则根据变量类型和解析类泛型类型自动匹配。
    Class<? extends FieldParser> parser() default NullFieldParser.class;
    
    // 标记配置更新之后，需要触发的监听类，默认值 InvalidListener 表示没有要触发的监听类
    Class<? extends Listener> listener() default InvalidListener.class;
}
```

listener 的值必须是实现 Listener 接口的类：

```java
public interface Listener <T> {
	// process 的参数是配置变更前后的值
    public void process(T oldValue, T newValue);
}
```

# 总结

针对配置解析的痛点，我们做了一个小小的配置解析框架用于解析 key/value 形式的配置，有哪些优点呢？

1. 使用注解描述配置信息，代码更简洁。
2. 强制不同配置使用不同的 FieldParser 描述解析方式，可以让代码更加的高内聚，更清晰。
3. 支持参数校验 Bean Validation，使用相应注解标记校验规则后，在解析配置时，会先做校验，避免异常配置导致故障。
4. 配置解析异常时，自动回滚配置，并且发消息报警，及时通知，及时处理。
5. 利用注解强大的描述能力，很容易扩展出更高级的功能。
