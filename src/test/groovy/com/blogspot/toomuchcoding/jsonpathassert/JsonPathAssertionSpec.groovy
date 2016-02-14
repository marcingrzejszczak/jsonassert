package com.blogspot.toomuchcoding.jsonpathassert

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.util.regex.Pattern

import static com.blogspot.toomuchcoding.jsonpathassert.JsonPathAssertion.assertThat
import static groovy.json.JsonOutput.toJson

/**
 * @author Marcin Grzejszczak
 */
public class JsonPathAssertionSpec extends Specification {

	def 'should work'() {
		expect:
		assertThat('body').field('foo').isEqualTo('bar')
		assertThat('body').field('foo').array('nested').contains('withlist').isEqualTo('bar')
	}

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
			verifiable.check()
		where:
			verifiable                                                                                           || expectedJsonPath
			assertThat(json1).field("some").field("nested").field("anothervalue").isEqualTo(4)                    || '''$.some.nested[?(@.anothervalue == 4)]'''
			assertThat(json1).field("some").field("nested").array("withlist").contains("name").isEqualTo("name1") || '''$.some.nested.withlist[*][?(@.name == 'name1')]'''
			assertThat(json1).field("some").field("nested").array("withlist").contains("name").isEqualTo("name2") || '''$.some.nested.withlist[*][?(@.name == 'name2')]'''
			assertThat(json1).field("some").field("nested").field("json").isEqualTo("with \"val'ue")              || '''$.some.nested[?(@.json == 'with "val\\'ue')]'''
	}

	@Shared String json2 =  '''{
			"property1": "a",
			"property2": "b"
		}'''

	@Unroll
	def "should generate assertions for simple response body"() {
		expect:
			verifiable.jsonPath() == expectedJsonPath
			verifiable.check()
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
			verifiable.check()
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
			verifiable.check()
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
			verifiable.check()
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
			verifiable.check()
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
			verifiable.check()
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
			verifiable.check()
		where:
			verifiable                                                            || expectedJsonPath
			assertThat(json8).field("property2").field("property3").isEqualTo("b") || '''$.property2[?(@.property3 == 'b')]'''
			assertThat(json8).field("property1").isEqualTo("a")                    || '''$[?(@.property1 == 'a')]'''
	}

	@Shared Map json9 =  [
			property1: "a",
			property2: Pattern.compile('[0-9]{3}')
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
				property2: Pattern.compile('\\d+')
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
			verifiable.check()
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
			verifiable.check()
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

}