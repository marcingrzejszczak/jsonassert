package com.toomuchcoding.jsonassert

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import spock.lang.Issue
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static JsonAssertion.assertThat
import static JsonAssertion.assertThatJson
import static groovy.json.JsonOutput.toJson

/**
 * @author Marcin Grzejszczak
 */
class JsonAssertionSpec extends Specification {

    @Shared String json1 = '''
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
    '''

    @Unroll
    def 'should convert a json with a map as root to a map of path to value '() {
        expect:
            verifiable.jsonPath() == expectedJsonPath
        where:
            verifiable                                                                                           || expectedJsonPath
            assertThat(json1).field("some").field("nested").field("anothervalue").isEqualTo(4)                    || '''$.some.nested[?(@.anothervalue == 4)]'''
            assertThat(json1).field("some").field("nested").field("anothervalue")                                 || '''$.some.nested.anothervalue'''
            assertThatJson(json1).field("some").field("nested").field("anothervalue").isEqualTo(4)                || '''$.some.nested[?(@.anothervalue == 4)]'''
            assertThat(json1).field("some").field("nested").array("withlist").contains("name").isEqualTo("name1") || '''$.some.nested.withlist[*][?(@.name == 'name1')]'''
            assertThat(json1).field("some").field("nested").array("withlist").contains("name").isEqualTo("name2") || '''$.some.nested.withlist[*][?(@.name == 'name2')]'''
            assertThat(json1).field("some").field("nested").field("json").isEqualTo("with \"val'ue")              || '''$.some.nested[?(@.json == 'with "val\\'ue')]'''
            assertThat(json1).field("some", "nested", "json").isEqualTo("with \"val'ue")                          || '''$.some.nested[?(@.json == 'with "val\\'ue')]'''
    }

    @Shared String json2 =  '''{
            "property1": "a",
            "property2": "b"
        }'''

    @Unroll
    def "should generate assertions for simple response body"() {
        expect:
            verifiable.jsonPath() == expectedJsonPath
        where:
            verifiable                                         || expectedJsonPath
            assertThat(json2).field("property1").isEqualTo("a") || '''$[?(@.property1 == 'a')]'''
            assertThat(json2).field("property2").isEqualTo("b") || '''$[?(@.property2 == 'b')]'''
    }

    @Shared String json3 =  '''{
        "property1": "true",
        "property2": null,
        "property3": false
    }'''

    @Unroll
    def "should generate assertions for null and boolean values"() {
        expect:
            verifiable.jsonPath() == expectedJsonPath
        where:
            verifiable                                            || expectedJsonPath
            assertThat(json3).field("property1").isEqualTo("true") || '''$[?(@.property1 == 'true')]'''
            assertThat(json3).field("property2").isNull()          || '''$[?(@.property2 == null)]'''
            assertThat(json3).field("property3").isEqualTo(false)  || '''$[?(@.property3 == false)]'''
    }

    @Shared Map json4 =  [
            property1: 'a',
            property2: [
                    [a: 'sth'],
                    [b: 'sthElse']
            ]
    ]

    @Unroll
    def "should generate assertions for simple response body constructed from map with a list"() {
        expect:
            verifiable.jsonPath() == expectedJsonPath
        where:
            verifiable                                                                     || expectedJsonPath
            assertThat(toJson(json4)).field("property1").isEqualTo("a")                     || '''$[?(@.property1 == 'a')]'''
            assertThat(toJson(json4)).array("property2").contains("a").isEqualTo("sth")     || '''$.property2[*][?(@.a == 'sth')]'''
            assertThat(toJson(json4)).array("property2").contains("b").isEqualTo("sthElse") || '''$.property2[*][?(@.b == 'sthElse')]'''
    }

    @Shared Map json5 =  [
            property: [
                    14: 0.0,
                    7 : 0.0
            ]
    ]

    @Unroll
    def "should generate assertions for a response body containing map with integers as keys"() {
        expect:
            verifiable.jsonPath() == expectedJsonPath
        where:
            verifiable                                                          || expectedJsonPath
            assertThat(toJson(json5)).field("property").field(7).isEqualTo(0.0)  || '''$.property[?(@.7 == 0.0)]'''
            assertThat(toJson(json5)).field("property").field(14).isEqualTo(0.0) || '''$.property[?(@.14 == 0.0)]'''
    }

    @Shared String json6 =  '''[
        {
            "property1": "a"
        },
        {
            "property2": "b"
        }]'''

    @Unroll
    def "should generate assertions for array in response body"() {
        expect:
            verifiable.jsonPath() == expectedJsonPath
        where:
            verifiable                                                    || expectedJsonPath
            assertThat(json6).array().contains("property1").isEqualTo("a") || '''$[*][?(@.property1 == 'a')]'''
            assertThat(json6).array().contains("property2").isEqualTo("b") || '''$[*][?(@.property2 == 'b')]'''
    }

    @Shared String json7 =  '''{
        "property1": [
        { "property2": "test1"},
        { "property3": "test2"}
        ]
    }'''

    @Unroll
    def "should generate assertions for array inside response body element"() {
        expect:
            verifiable.jsonPath() == expectedJsonPath
        where:
            verifiable                                                                   || expectedJsonPath
            assertThat(json7).array("property1").contains("property2").isEqualTo("test1") || '''$.property1[*][?(@.property2 == 'test1')]'''
            assertThat(json7).array("property1").contains("property3").isEqualTo("test2") || '''$.property1[*][?(@.property3 == 'test2')]'''
    }

    @Shared String json8 =  """{
        "property1": "a",
        "property2": {"property3": "b"}
    }"""

    def "should generate assertions for nested objects in response body"() {
        expect:
            verifiable.jsonPath() == expectedJsonPath
        where:
            verifiable                                                            || expectedJsonPath
            assertThat(json8).field("property2").field("property3").isEqualTo("b") || '''$.property2[?(@.property3 == 'b')]'''
            assertThat(json8).field("property1").isEqualTo("a")                    || '''$[?(@.property1 == 'a')]'''
    }

    @Shared Map json9 =  [
            property1: "a",
            property2: 123
    ]

    @Unroll
    def "should generate regex assertions for map objects in response body"() {
        expect:
            verifiable.jsonPath() == expectedJsonPath
        where:
            verifiable                                                      || expectedJsonPath
            assertThat(toJson(json9)).field("property2").matches("[0-9]{3}") || '''$[?(@.property2 =~ /[0-9]{3}/)]'''
            assertThat(toJson(json9)).field("property1").isEqualTo("a")      || '''$[?(@.property1 == 'a')]'''
    }

    def "should generate escaped regex assertions for string objects in response body"() {
        given:
        Map json =  [
                property2: 123123
        ]
        expect:
            def verifiable = assertThat(toJson(json)).field("property2").matches("\\d+")
            verifiable.jsonPath() == '''$[?(@.property2 =~ /\\d+/)]'''
    }

    @Shared Map json10 =  [
            errors: [
                    [property: "bank_account_number",
                     message: "incorrect_format"]
            ]
    ]

    @Unroll
    def "should work with more complex stuff and jsonpaths"() {
        expect:
            verifiable.jsonPath() == expectedJsonPath
        where:
            verifiable                                                                                     || expectedJsonPath
            assertThat(toJson(json10)).array("errors").contains("property").isEqualTo("bank_account_number") || '''$.errors[*][?(@.property == 'bank_account_number')]'''
            assertThat(toJson(json10)).array("errors").contains("message").isEqualTo("incorrect_format")     || '''$.errors[*][?(@.message == 'incorrect_format')]'''
    }

    @Shared String json11 = '''
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
                        '''

    @Unroll
    def "should manage to parse a double array"() {
        expect:
            verifiable.jsonPath() == expectedJsonPath
        where:
            verifiable                                                                                                                           || expectedJsonPath
            assertThat(json11).array().field("place").field("bounding_box").array("coordinates").array().arrayField().contains(38.995548).value()  || '''$[*].place.bounding_box.coordinates[*][*][?(@ == 38.995548)]'''
            assertThat(json11).array().field("place").field("bounding_box").array("coordinates").array().arrayField().contains(-77.119759).value() || '''$[*].place.bounding_box.coordinates[*][*][?(@ == -77.119759)]'''
            assertThat(json11).array().field("place").field("bounding_box").array("coordinates").array().arrayField().contains(-76.909393).value() || '''$[*].place.bounding_box.coordinates[*][*][?(@ == -76.909393)]'''
            assertThat(json11).array().field("place").field("bounding_box").array("coordinates").array().arrayField().contains(38.791645).value()  || '''$[*].place.bounding_box.coordinates[*][*][?(@ == 38.791645)]'''

    }

    @Unroll
    def 'should convert a json with list as root to a map of path to value'() {
        expect:
            assertThat(json).array().field("some").field("nested").field("json").isEqualTo("with value").jsonPath() == '''$[*].some.nested[?(@.json == 'with value')]'''
            assertThat(json).array().field("some").field("nested").field("anothervalue").isEqualTo(4).jsonPath() == '''$[*].some.nested[?(@.anothervalue == 4)]'''
            assertThat(json).array().field("some").field("nested").array("withlist").contains("name").isEqualTo("name1").jsonPath() == '''$[*].some.nested.withlist[*][?(@.name == 'name1')]'''
            assertThat(json).array().field("some").field("nested").array("withlist").contains("name").isEqualTo("name2").jsonPath() == '''$[*].some.nested.withlist[*][?(@.name == 'name2')]'''
            assertThat(json).array().field("some").field("nested").array("withlist").field("anothernested").field("name").isEqualTo("name3").jsonPath() == '''$[*].some.nested.withlist[*].anothernested[?(@.name == 'name3')]'''
        where:
        json << [
                '''
                        [ {
                                "some" : {
                                    "nested" : {
                                        "json" : "with value",
                                        "anothervalue": 4,
                                        "withlist" : [
                                            { "name" :"name1"} , {"name": "name2"}, {"anothernested": { "name": "name3"} }
                                        ]
                                    }
                                }
                            },
                            {
                                "someother" : {
                                    "nested" : {
                                        "json" : "with value",
                                        "anothervalue": 4,
                                        "withlist" : [
                                            { "name" :"name1"} , {"name": "name2"}
                                        ]
                                    }
                                }
                            }
                        ]
    ''',
                '''
                            [{
                                "someother" : {
                                    "nested" : {
                                        "json" : "with value",
                                        "anothervalue": 4,
                                        "withlist" : [
                                            { "name" :"name1"} , {"name": "name2"}
                                        ]
                                    }
                                }
                            },
                         {
                                "some" : {
                                    "nested" : {
                                        "json" : "with value",
                                        "anothervalue": 4,
                                        "withlist" : [
                                             {"name": "name2"}, {"anothernested": { "name": "name3"} }, { "name" :"name1"}
                                        ]
                                    }
                                }
                            }
                        ]''']
    }

    def "should run json path when provided manually"() {
        given:
            String json = """{
            "property1": "a",
            "property2": {"property3": "b"}
        }"""
        and:
            String jsonPath = '''$[?(@.property1 == 'a')]'''
        expect:
            assertThat(json).matchesJsonPath(jsonPath)
    }

    def "should throw exception when json path is not matched"() {
        given:
            String json = """{
            "property1": "a",
            "property2": {"property3": "b"}
        }"""
        and:
            String jsonPath = '''$[?(@.property1 == 'c')]'''
        when:
            assertThat(json).matchesJsonPath(jsonPath)
        then:
            IllegalStateException illegalStateException = thrown(IllegalStateException)
            illegalStateException.message.contains("Parsed JSON")
            illegalStateException.message.contains("doesn't match")
    }

    def "should not throw exception when json path is not matched and system prop overrides the check"() {
        given:
            String json = """{
            "property1": "a",
            "property2": {"property3": "b"}
        }"""
        and:
            String jsonPath = '''$[?(@.property1 == 'c')]'''
        when:
            assertThat(json).withoutThrowingException().matchesJsonPath(jsonPath)
        then:
            noExceptionThrown()
    }

    def "should generate escaped regex assertions for boolean objects in response body"() {
        given:
        Map json =  [
                property2: true
        ]
        expect:
        def verifiable = assertThat(toJson(json)).field("property2").matches('true|false')
        verifiable.jsonPath() == '''$[?(@.property2 =~ /true|false/)]'''
    }

    def "should generate escaped regex assertions for numbers objects in response body"() {
        given:
        Map json =  [
                property2: 50
        ]
        expect:
        def verifiable = assertThat(toJson(json)).field("property2").matches('[0-9]{2}')
        verifiable.jsonPath() == '''$[?(@.property2 =~ /[0-9]{2}/)]'''
    }

    def "should escape regular expression properly"() {
        given:
        String json = """
            {
                "path" : "/api/12",
                "correlationId" : 123456
            }
        """
        expect:
        DocumentContext parsedJson = JsonPath.parse(json)
        def verifiable = assertThatJson(parsedJson).field("path").matches("^/api/[0-9]{2}\$")
        verifiable.jsonPath() == '''$[?(@.path =~ /^\\/api\\/[0-9]{2}$/)]'''
    }

    @Issue("Accurest#193")
    def "should escape single quotes in a quoted string"() {
        given:
        String json = """
            {
                "text" : "text with 'quotes' inside"
            }
        """
        expect:
        DocumentContext parsedJson = JsonPath.parse(json)
        def verifiable = assertThatJson(parsedJson).field("text").isEqualTo("text with 'quotes' inside")
        verifiable.jsonPath() == '''$[?(@.text == 'text with \\'quotes\\' inside')]'''
    }
    
    @Issue("Accurest#193")
    def "should escape double quotes in a quoted string"() {
        given:
            String json = """
                {
                    "text" : "text with \\"quotes\\" inside"
                }
            """
        expect:
            DocumentContext parsedJson = JsonPath.parse(json)
            def verifiable = assertThatJson(parsedJson).field("text").isEqualTo('''text with "quotes" inside''')
            verifiable.jsonPath() == '''$[?(@.text == 'text with "quotes" inside')]'''
    }

    def 'should resolve the value of JSON via JSON Path'() {
        given:
            String json =
                    '''
                            [ {
                                    "some" : {
                                        "nested" : {
                                            "json" : "with value",
                                            "anothervalue": 4,
                                            "withlist" : [
                                                { "name" :"name1"} ,
                                                {"name": "name2"},
                                                {"anothernested": { "name": "name3"} }
                                            ]
                                        }
                                    }
                                },
                                {
                                    "someother" : {
                                        "nested" : {
                                            "json" : true,
                                            "anothervalue": 4,
                                            "withlist" : [
                                                { "name" :"name1"} , {"name": "name2"}
                                            ],
                                            "withlist2" : [
                                                "a", "b"
                                            ]
                                        }
                                    }
                                }
                            ]
        '''
        expect:
            com.toomuchcoding.jsonassert.JsonPath.builder(json).array().field("some").field("nested").field("json").read(String) == 'with value'
            com.toomuchcoding.jsonassert.JsonPath.builder(json).array().field("some").field("nested").field("anothervalue").read(Integer) == 4
            assertThat(json).array().field("some").field("nested").array("withlist").field("name").read(List) == ['name1', 'name2']
            assertThat(json).array().field("someother").field("nested").array("withlist2").read(List) == ['a', 'b']
            assertThat(json).array().field("someother").field("nested").field("json").read(Boolean) == true
    }

    def 'should assert json with only top list elements'() {
        given:
            String json = '''["Java", "Java8", "Spring", "SpringBoot", "Stream"]'''
        expect:
            assertThatJson(json).arrayField().contains("Java8").value()
            assertThatJson(json).arrayField().contains("Spring").value()
            assertThatJson(json).arrayField().contains("Java").value()
            assertThatJson(json).arrayField().contains("Stream").value()
            assertThatJson(json).arrayField().contains("SpringBoot").value()
    }

    @Issue("#9")
    def 'should match array containing an array of primitives'() {
        given:
            String json =  '''{"first_name":"existing",
                                  "partners":[
                                      { "role":"AGENT",
                                        "payment_methods":[ "BANK", "CASH" ]
                                      }
                                   ]
                                }'''
        expect:
            def verifiable = assertThatJson(json).array("partners").array("payment_methods").arrayField().isEqualTo("BANK").value()
            verifiable.jsonPath() == '''$.partners[*].payment_methods[?(@ == 'BANK')]'''
    }

    @Issue("#9")
    def 'should match pattern in array'() {
        given:
            String json =  '''{ "authorities": ["ROLE_ADMIN"] }'''
        expect:
            def verifiable = assertThatJson(json).array("authorities").arrayField().matches("^[a-zA-Z0-9_\\- ]+\$").value()
            verifiable.jsonPath() == '''$.authorities[?(@ =~ /^[a-zA-Z0-9_\\- ]+$/)]'''
    }

    @Issue("#10")
    def 'should manage to parse array with string values'() {
        given:
            String json =  '''{ "some_list" : ["name1", "name2"] }'''
        expect:
            def v1 = assertThat(JsonPath.parse(json)).array("some_list").arrayField().isEqualTo("name1")
            def v2 = assertThat(JsonPath.parse(json)).array("some_list").arrayField().isEqualTo("name2")
        and:
            v1.jsonPath() == '''$.some_list[?(@ == 'name1')]'''
            v2.jsonPath() == '''$.some_list[?(@ == 'name2')]'''
    }

    def 'should parse an array of arrays that are root elements'() {
        given:
            String json =  '''[["Programming", "Java"], ["Programming", "Java", "Spring", "Boot"]]'''
        expect:
            def v1 = assertThatJson(JsonPath.parse(json)).array().arrayField().isEqualTo("Java").value()
        and:
            v1.jsonPath() == '''$[*][?(@ == 'Java')]'''
    }

    @Issue('#11')
    def 'should allow to check array size'() {
        given:
            String json =  '''{ "some_list" : ["name1", "name2"] }'''
        expect:
            assertThat(json).array("some_list").hasSize(2)
        when:
            assertThat(json).array("some_list").hasSize(5)
        then:
            def ex = thrown(RuntimeException)
            ex instanceof IllegalStateException
            ex.message == '''Parsed JSON <{"some_list":["name1","name2"]}> doesn't have the size <5> for JSON path <$.some_list[*]>. The size is <2>'''
    }

    @Issue('#11')
    def 'should allow to check size of root array'() {
        given:
            String json =  '''["name1", "name2"]'''
        expect:
            assertThat(json).hasSize(2)
        when:
           assertThat(json).hasSize(5)
        then:
            def ex = thrown(RuntimeException)
            ex instanceof IllegalStateException
            ex.message == '''Parsed JSON <["name1","name2"]> doesn't have the size <5> for JSON path <$>. The size is <2>'''
    }

    @Issue('#11')
    def 'should allow to check size of a nested array'() {
        given:
        String json =
                '''
                            [ {
                                    "some" : {
                                        "nested" : {
                                            "json" : "with value",
                                            "anothervalue": 4,
                                            "withlist" : [
                                                { "name" :"name1"} ,
                                                {"name": "name2"},
                                                {"anothernested": { "name": "name3"} }
                                            ]
                                        }
                                    }
                                },
                                {
                                    "someother" : {
                                        "nested" : {
                                            "json" : true,
                                            "anothervalue": 4,
                                            "withlist" : [
                                                { "name" :"name1"} , {"name": "name2"}
                                            ],
                                            "withlist2" : [
                                                "a", "b"
                                            ]
                                        }
                                    }
                                }
                            ]'''
        expect:
            assertThat(json).array().field("someother").field("nested").array("withlist2").hasSize(2)
        when:
            assertThat(json).array().field("someother").field("nested").array("withlist2").hasSize(5)
        then:
            def ex = thrown(RuntimeException)
            ex instanceof IllegalStateException
            ex.message == '''Parsed JSON <[{"some":{"nested":{"json":"with value","anothervalue":4,"withlist":[{"name":"name1"},{"name":"name2"},{"anothernested":{"name":"name3"}}]}}},{"someother":{"nested":{"json":true,"anothervalue":4,"withlist":[{"name":"name1"},{"name":"name2"}],"withlist2":["a","b"]}}}]> doesn't have the size <5> for JSON path <$[*].someother.nested.withlist2[*]>. The size is <2>'''
    }

    @Issue('#11')
    def 'should allow to check size of a named array'() {
        given:
        def json = [
                property1: 'a',
                property2: [
                [a: 'sth'],
                [b: 'sthElse']
            ]
        ]
        expect:
            assertThat(toJson(json)).array("property2").hasSize(2)
        when:
            assertThat(toJson(json)).array("property2").hasSize(5)
        then:
            def ex = thrown(RuntimeException)
            ex instanceof IllegalStateException
            ex.message == '''Parsed JSON <{"property1":"a","property2":[{"a":"sth"},{"b":"sthElse"}]}> doesn't have the size <5> for JSON path <$.property2[*]>. The size is <2>'''
    }

    @Issue('#11')
    def 'should allow to check size of two nameless arrays'() {
        given:
            String json =  '''[["Programming", "Java"], ["Programming", "Java", "Spring", "Boot"], ["Programming", "Java", "Spring", "Boot", "Master"]]'''
        expect:
            assertThat(json).hasSize(3)
            assertThat(json).elementWithIndex(0).hasSize(2)
            assertThat(json).elementWithIndex(1).hasSize(4)
        when:
           assertThat(json).array().hasSize(4)
        then:
            def ex = thrown(RuntimeException)
            ex instanceof IllegalStateException
            ex.message == '''Parsed JSON <[["Programming","Java"],["Programming","Java","Spring","Boot"],["Programming","Java","Spring","Boot","Master"]]> doesn't have the size <4> for JSON path <$[*]>. The size is <3>'''
    }

    @Issue('#11')
    def 'should allow to check size of two nameless arrays in a nameless array'() {
        given:
            String json =  '''[[["Programming", "Java"], ["Programming", "Java", "Spring", "Boot"]]]'''
        expect:
            assertThat(json).hasSize(1)
            assertThat(json).elementWithIndex(0).elementWithIndex(0).hasSize(2)
            assertThat(json).elementWithIndex(0).elementWithIndex(1).hasSize(4)
        when:
           assertThat(json).elementWithIndex(0).elementWithIndex(1).hasSize(5)
        then:
            def ex = thrown(RuntimeException)
            ex instanceof IllegalStateException
            ex.message == '''Parsed JSON <[[["Programming","Java"],["Programming","Java","Spring","Boot"]]]> doesn't have the size <5> for JSON path <$[0][1]>. The size is <4>'''
    }

    @Issue('#11')
    def 'should allow to check array size of nameless array'() {
        given:
            String json =  '''{ "coordinates" : [[
                                            ["name1", "name2"],
                                            ["name3", "name4"]
                                        ]] }'''
        expect:
            assertThat(json).array("coordinates").array().hasSize(2)
        when:
            assertThat(json).array("coordinates").array().hasSize(5)
        then:
            def ex = thrown(RuntimeException)
            ex instanceof IllegalStateException
            ex.message == '''Parsed JSON <{"coordinates":[[["name1","name2"],["name3","name4"]]]}> doesn't have the size <5> for JSON path <$.coordinates[*][*]>. The size is <2>'''
    }

    @Unroll
    def 'should fail on non existent path'() {
        when:
            assertThat(json instanceof Map ? toJson(json) : json).field("non").field("existant").field("field").isEqualTo("imaginary value")
        then:
            def ex = thrown(RuntimeException)
            ex.cause instanceof PathNotFoundException
        where:
            json << [ json1, json2, json3, json4, json5, json6, json7, json8, json9, json10, json11 ]
    }

    @Issue('#14')
    def 'should allow to check if size is empty'() {
        given:
            String json =  '''{ "coordinates" : [], "foo": ["bar", "baz"] }'''
        expect:
            assertThat(json).array("coordinates").isEmpty()
        when:
            assertThat(json).array("foo").isEmpty()
        then:
            def ex = thrown(RuntimeException)
            ex instanceof IllegalStateException
            ex.message == '''Parsed JSON [{"coordinates":[],"foo":["bar","baz"]}] with the JSON path [$.foo[*]] is not empty!'''
    }

    @Issue('#16')
    def 'should throw exception when an empty array is returned'() {
        given:
            String json =  '''{}'''
        when:
            assertThatJson(json).field("doesNotExist").matches("[\\p{L}]*")
        then:
            def ex = thrown(RuntimeException)
            ex instanceof IllegalStateException
            ex.message == '''Parsed JSON [{}] doesn't match the JSON path [$[?(@.doesNotExist =~ /[\\p{L}]*/)]]'''
        when:
            assertThatJson(json).array("c").matches("[\\p{L}]*")
        then:
            ex = thrown(RuntimeException)
            ex instanceof IllegalStateException
            ex.message == '''Parsed JSON [{}] doesn't match the JSON path [$[?(@.c =~ /[\\p{L}]*/)]]'''
    }

}