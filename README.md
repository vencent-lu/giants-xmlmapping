# giants-xmlmapping

[![Maven Central](https://img.shields.io/maven-central/v/com.github.vencent-lu/giants-xmlmapping.svg?label=Maven%20Central)](https://search.maven.org/artifact/com.github.vencent-lu/giants-xmlmapping)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![JDK](https://img.shields.io/badge/JDK-1.7%2B-orange.svg)](https://www.oracle.com/java/technologies/downloads/)
[![dom4j](https://img.shields.io/badge/dom4j-2.1.4-green.svg)](https://dom4j.github.io/)

基于注解的 XML 与 Java 对象双向映射工具。通过在 POJO 上声明注解，即可把 XML 文档解析成对象，或把对象序列化成 XML，无需手写 dom4j 解析代码。

底层基于 [dom4j](https://dom4j.github.io/) 实现，映射配置由注解在运行时反射解析生成。

## 特性

- 注解驱动：用 `@XmlEntity`、`@XmlAttribute`、`@XmlElement` 等注解描述映射关系，零 XML 配置文件。
- 双向映射：既能把 XML 解析为对象（load），也能把对象生成为 XML（write / buildXmlString）。
- 多来源加载：支持从 `InputStream`、classpath 资源路径加载 XML。
- 嵌套与集合：支持嵌套子对象、`List` / `Set` 集合以及自引用结构。
- 主键索引：通过 `@XmlIdKey` 为对象建立 ID 索引，可按 ID 快速检索。
- 类型自动转换：自动完成字符串与常见基本类型（含包装类型）及 `Class` 之间的转换。

## 环境要求

- JDK 1.7 及以上
- dom4j 2.1.4
- giants-common 1.3.0

## Maven 引入

```xml
<dependency>
    <groupId>com.github.vencent-lu</groupId>
    <artifactId>giants-xmlmapping</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 快速开始

### 1. 定义映射实体

```java
@XmlEntity(name = "catalog")
public class Catalog {

    @XmlAttribute
    private String version;

    @XmlManyElement(elementClass = Book.class)
    private List<Book> books;

    // getters / setters ...
}

@XmlEntity(name = "book")
public class Book {

    @XmlIdKey(name = "id")
    private String id;

    @XmlAttribute
    private String title;

    @XmlAttribute
    private Double price;

    @XmlElement
    private Author author;

    // getters / setters ...
}

@XmlEntity(name = "author")
public class Author {

    @XmlAttribute
    private String name;

    // getters / setters ...
}
```

对应的 XML 结构：

```xml
<catalog version="1.0">
    <book id="b1" title="Effective Java" price="59.0">
        <author name="Joshua Bloch"/>
    </book>
    <book id="b2" title="Clean Code" price="49.0">
        <author name="Robert C. Martin"/>
    </book>
</catalog>
```

### 2. 解析 XML 为对象

```java
InputStream in = getClass().getResourceAsStream("/catalog.xml");
XmlMappingData mappingData = new XmlMappingData(
        new Class<?>[] { Catalog.class, Book.class, Author.class }, in);

Catalog catalog = mappingData.getDataModule(Catalog.class).get();

// Book 定义了 @XmlIdKey，可按 ID 检索
Book book = mappingData.getDataModule(Book.class).get("b1");
```

### 3. 对象生成 XML

```java
XmlMappingData mappingData = new XmlMappingData(
        Catalog.class, Book.class, Author.class);
mappingData.loadObject(catalog);

// 生成为字符串
String xml = mappingData.buildXmlString(Catalog.class);

// 或直接写入文件 / 输出流
mappingData.writeXml(Catalog.class, "/tmp/catalog.xml");
```

## 注解说明

| 注解 | 作用目标 | 说明 |
| --- | --- | --- |
| `@XmlEntity(name)` | 类 | 声明该类为可映射实体。`name` 为对应 XML 元素名，缺省时取类名首字母小写。 |
| `@XmlIdKey(name)` | 字段 | 声明主键字段，用于建立 ID 索引。类型必须是 `java.lang` 包下的类型。`name` 缺省取字段名。 |
| `@XmlAttribute(name)` | 字段 | 映射为 XML 属性。类型须为基本类型或 `java.lang` 类型。`name` 缺省取字段名。 |
| `@XmlElement` | 字段 | 映射为单个子元素（嵌套实体），字段类型须为另一个 `@XmlEntity`。 |
| `@XmlManyElement(elementClass)` | 字段 | 映射为一组子元素，字段类型须为 `List` 或 `Set`。泛型可推断元素类型时 `elementClass` 可省略。 |

## 核心 API

`XmlMappingData` 是框架入口，`XmlDataModule<T>` 用于访问解析后的对象集合。完整方法说明、类型转换规则、解析细节与异常体系请参阅使用手册。

## 使用手册

详细用法见 [docs/使用手册.md](docs/使用手册.md)。

## License

本项目基于 [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt) 发布。
