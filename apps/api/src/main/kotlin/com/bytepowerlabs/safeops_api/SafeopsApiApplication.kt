package com.bytepowerlabs.safeops_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SafeopsApiApplication

fun main(args: Array<String>) {
	runApplication<SafeopsApiApplication>(*args)
}
