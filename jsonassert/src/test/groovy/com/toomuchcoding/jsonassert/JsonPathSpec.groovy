package com.toomuchcoding.jsonassert

import spock.lang.Specification
/**
 * @author Marcin Grzejszczak
 */
class JsonPathSpec extends Specification {

    def "should generate proper JSON paths"() {
        expect:
            jsonPath == expectedJsonPath
        where:
            jsonPath                                                                                                          || expectedJsonPath
            JsonPath.builder().field("some").field("nested").field("anothervalue").isEqualTo(4).jsonPath()                    || '''$.some.nested[?(@.anothervalue == 4)]'''
            JsonPath.builder().field("some").field("nested").field("anothervalue").isEqualTo(4).jsonPath()                    || '''$.some.nested[?(@.anothervalue == 4)]'''
            JsonPath.builder().field("some").field("nested").array("withlist").contains("name").isEqualTo("name1").jsonPath() || '''$.some.nested.withlist[*][?(@.name == 'name1')]'''
            JsonPath.builder().field("some").field("nested").array("withlist").contains("name").isEqualTo("name2").jsonPath() || '''$.some.nested.withlist[*][?(@.name == 'name2')]'''
            JsonPath.builder().field("some").field("nested").field("json").isEqualTo("with \"val'ue").jsonPath()              || '''$.some.nested[?(@.json == 'with "val\\'ue')]'''
    }

}
