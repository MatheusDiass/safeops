package com.bytepowerlabs.safeops_api.config

import com.bytepowerlabs.safeops_api.config.properties.CorsProperties
import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(private val corsProperties: CorsProperties) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.cors {
        }.csrf {
            it.disable()
        }.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }.authorizeHttpRequests {
            it.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/identity/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/identity/auth/web/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/identity/auth/web/refresh").permitAll()
                .requestMatchers(HttpMethod.POST, "/identity/auth/web/logout").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/health/**").permitAll()
                .anyRequest().authenticated()
        }.oauth2ResourceServer {
            it.jwt { }
        }.formLogin { it.disable() }.httpBasic {
            it.disable()
        }.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = corsProperties.allowedOrigins
            allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            allowedHeaders = listOf("Authorization", "Content-Type")
            allowCredentials = true
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}
