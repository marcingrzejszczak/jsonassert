package com.toomuchcoding.jsonassert

import com.jayway.jsonpath.JsonPath
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static JsonAssertions.assertThat
/**
 * @author Marcin Grzejszczak
 */
class JsonAssertionWithAssertJJava8Spec extends Specification {

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
            assertThat(JsonPath.parse(json1)).field("some").field("nested").field("anothervalue").isEqualTo(4)
            assertThat(JsonPath.parse(json1)).field("some").field("nested").array("withlist").contains("name").isEqualTo("name1")
            assertThat(JsonPath.parse(json1)).field("some").field("nested").array("withlist").contains("name").isEqualTo("name2")
            assertThat(JsonPath.parse(json1)).field("some").field("nested").field("json").isEqualTo("with \"val'ue")
    }

    @Shared String json2 =  '''{
            "property1": "a",
            "property2": "b"
        }'''

    @Unroll
    def "should generate assertions for simple response body"() {
        expect:
            assertThat(JsonPath.parse(json2)).field("property1").isEqualTo("a")
            assertThat(JsonPath.parse(json2)).field("property2").isEqualTo("b")
    }

    @Shared String json3 =  '''{
        "property1": "true",
        "property2": null,
        "property3": false
    }'''

    @Unroll
    def "should generate assertions for null and boolean values"() {
        expect:
            assertThat(JsonPath.parse(json3)).field("property1").isEqualTo("true")
            assertThat(JsonPath.parse(json3)).field("property2").isNull()
            assertThat(JsonPath.parse(json3)).field("property3").isEqualTo(false)
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
            assertThat(JsonPath.parse(json6)).array().contains("property1").isEqualTo("a")
            assertThat(JsonPath.parse(json6)).array().contains("property2").isEqualTo("b")
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
            assertThat(JsonPath.parse(json7)).array("property1").contains("property2").isEqualTo("test1")
            assertThat(JsonPath.parse(json7)).array("property1").contains("property3").isEqualTo("test2")
    }

    @Shared String json8 =  """{
        "property1": "a",
        "property2": {"property3": "b"}
    }"""

    def "should generate assertions for nested objects in response body"() {
        expect:
            assertThat(JsonPath.parse(json8)).field("property2").field("property3").isEqualTo("b")
            assertThat(JsonPath.parse(json8)).field("property1").isEqualTo("a")
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
            assertThat(JsonPath.parse(json11)).array().field("place").field("bounding_box").array("coordinates").array().arrayField().contains(38.995548).value()
            assertThat(JsonPath.parse(json11)).array().field("place").field("bounding_box").array("coordinates").array().arrayField().contains(-77.119759).value()
            assertThat(JsonPath.parse(json11)).array().field("place").field("bounding_box").array("coordinates").array().arrayField().contains(-76.909393).value()
            assertThat(JsonPath.parse(json11)).array().field("place").field("bounding_box").array("coordinates").array().arrayField().contains(38.791645).value()

    }

    @Unroll
    def 'should convert a json with list as root to a map of path to value'() {
        expect:
            assertThat(JsonPath.parse(json)).array().field("some").field("nested").field("json").isEqualTo("with value")
            assertThat(JsonPath.parse(json)).array().field("some").field("nested").field("anothervalue").isEqualTo(4)
            assertThat(JsonPath.parse(json)).array().field("some").field("nested").array("withlist").contains("name").isEqualTo("name1")
            assertThat(JsonPath.parse(json)).array().field("some").field("nested").array("withlist").contains("name").isEqualTo("name2")
            assertThat(JsonPath.parse(json)).array().field("some").field("nested").array("withlist").field("anothernested").field("name").isEqualTo("name3")
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
            assertThat(JsonPath.parse(json)).matchesJsonPath(jsonPath)
    }

    def "should throw exception when json path is not matched against provided json path"() {
        given:
            String json = """{
            "property1": "a",
            "property2": {"property3": "b"}
        }"""
        and:
            String jsonPath = '''$[?(@.property1 == 'c')]'''
        when:
            assertThat(JsonPath.parse(json)).matchesJsonPath(jsonPath)
        then:
            AssertionError assertionError = thrown(AssertionError)
            assertionError.message.contains("Expected JSON to match JSON Path")
    }

    def "should throw exception when json path is not matched"() {
        given:
            String json = """{
            "property1": "a",
            "property2": {"property3": "b"}
        }"""
        when:
            assertThat(JsonPath.parse(json)).field("property2").field("property3").isEqualTo("c")
        then:
            AssertionError assertionError = thrown(AssertionError)
            assertionError.message == '''Expected JSON to match JSON Path <$.property2[?(@.property3 == 'c')]> but it didn't'''
    }


}