package myjvm

import html4k.*
import html4k.consumers.PredicateResult
import html4k.consumers.delayed
import html4k.consumers.filter
import html4k.consumers.measureTime
import html4k.dom.*
import html4k.stream.*
import org.w3c.dom.Element
import java.io.PrintStream
import kotlin.dom.first

fun <T> TagConsumer<T>.buildMe() =
	html {
		body {
			a {
				+"wfwe"
			}
			div(listOf("block", "deprecated")) {
				a(href = "http://kotlinlang.org") {
					target = Targets._blank
					attributes["custom"] = "custom"
					+"test me"
				}
			}
			div(listOf("alive")) {
			}
		}
	}

fun main(args : Array<String>) {
	System.out.appendHTML().buildMe().println()
	System.out.appendHTML().filter { if (it.name == "div") SKIP else PASS }.buildMe().println()
	System.out.appendHTML().filter { if (it.name == "div") DROP else PASS }.buildMe().println()

	System.out.appendHTML().filter { if (it.name == "div" && it.attributes["class"]?.contains("deprecated") ?: false) SKIP else PASS }.buildMe().append("\n")

	System.out.appendHTML().measureTime().html {
		head {
			title("Welcome page")
		}
		body {
			div {
				+"<<<special chars & entities goes here>>>"
			}
			div {
				CDATA("Here is my content")
			}
		}
	}.let {
		it.first.println()
		it.first.println("Generated in ${it.second} ms")
	}

	val document = document {
		val html = buildHTML().html {
			body {
				div {
					a("http://kotlinlang.org") {
						target = Targets._blank
						+"me here"
					}
				}
			}
		}
		appendChild(html)
	}

	System.out.println(document.serialize())

	println(document {
		appendChild(
			buildHTML().filter { if (it.name == "div") SKIP else PASS  }.html {
				body {
					div {
						a { +"link1" }
					}
					a { +"link2" }
				}
			}
		)
	}.serialize())

	document.getElementsByTagName("div").item(0).buildAndAppendChild {
		div {
			+"aaa"
		}
	}

	System.out.println(document.serialize())
}