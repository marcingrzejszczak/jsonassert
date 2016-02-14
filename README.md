[![Build Status](https://travis-ci.org/marcingrzejszczak/jsonassert.svg?branch=master)](https://travis-ci.org/marcingrzejszczak/jsonassert)
[![Join the chat at https://gitter.im/marcingrzejszczak/jsonassert](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/marcingrzejszczak/jsonassert?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

JSON Assert
===============

Small library for those who have a hard time understanding the complexity of JSON Path.

## Rationale

Have you ever met such a JSON Path expression?

```
$[*].place.bounding_box.coordinates[*][*][?(@ == 38.791645)]
```

Pretty isn't it? Wouldn't it be better to just read:

```
assertThat(jsonAsString).array().field("place").field("bounding_box").array("coordinates").array().contains(38.791645).value()
```

JSON Assert to the rescue!

## Fast intro

The library has two main classes. One is `JsonAssertion` that gives you public static methods:

```java
public static JsonVerifiable assertThat(String body)
```

NOTE! The aforementioned version caches the `DocumentContext` for the provided JSON.

and

```java
public static JsonVerifiable assertThat(DocumentContext parsedJson)
```

Both these methods give return the public `JsonVerifiable` interface which is a fluent interface with which you can build your
JSON path expression. Please check the Javadocs of that file for more information.

## Dependencies

JSON Assert is really lightweight. It has only one dependency

```
compile "com.jayway.jsonpath:json-path:2.0.0"
```

## Examples

### Example 1

For the JSON

```json
 {
        "some" : {
            "nested" : {
                "json" : "with \\"val'ue",
                "anothervalue": 4,
                "withlist" : [
                    { "name" :"name1"} , {"name": "name2"}
                ]
            }
        }
    }
```

The following is true

JSON Assert expressions:

```
JsonAssertion.assertThat(json).field("some").field("nested").field("anothervalue").isEqualTo(4)
JsonAssertion.assertThat(json).field("some").field("nested").array("withlist").contains("name").isEqualTo("name1")
JsonAssertion.assertThat(json).field("some").field("nested").array("withlist").contains("name").isEqualTo("name2")
JsonAssertion.assertThat(json).field("some").field("nested").field("json").isEqualTo("with \"val'ue")
```

Respective JSON Path expressions:
```
$.some.nested[?(@.anothervalue == 4)]
$.some.nested.withlist[*][?(@.name == 'name1')]
$.some.nested.withlist[*][?(@.name == 'name2')]
$.some.nested[?(@.json == 'with "val\\'ue')]
```

### Example 2

For the JSON

```json
 [{
    "place":
    {
        "bounding_box":
        {
            "coordinates":
                [[
                    [-77.119759,38.995548],
                    [-76.909393,38.791645]
                ]]
        }
    }
}]
```

The following is true

JSON Assert expressions:

```
JsonAssertion.assertThat(json11).array().field("place").field("bounding_box").array("coordinates").array().arrayField().contains(38.995548).value()
JsonAssertion.assertThat(json11).array().field("place").field("bounding_box").array("coordinates").array().arrayField().contains(-77.119759).value()
JsonAssertion.assertThat(json11).array().field("place").field("bounding_box").array("coordinates").array().arrayField().contains(-76.909393).value()
JsonAssertion.assertThat(json11).array().field("place").field("bounding_box").array("coordinates").array().arrayField().contains(38.791645).value()
```

Respective JSON Path expressions:
```
$[*].place.bounding_box.coordinates[*][*][?(@ == 38.995548)]
$[*].place.bounding_box.coordinates[*][*][?(@ == -77.119759)]
$[*].place.bounding_box.coordinates[*][*][?(@ == -76.909393)]
$[*].place.bounding_box.coordinates[*][*][?(@ == 38.791645)]
```

### More examples

More examples can be found in the `JsonAssertionSpec` in the test sources

## How to add it

Just add it as your dependency (Example for Gradle)

```
testCompile `com.blogspot.toomuchcoding:jsonassert:0.0.1`
```

Contact
--------------------
[Gitter chat](https://gitter.im/marcingrzejszczak/jsonassert)
