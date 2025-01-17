/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.core.security

import io.javalin.Javalin
import io.javalin.core.plugin.Plugin
import io.javalin.core.util.Header
import io.javalin.http.UnauthorizedResponse

// adds a filter that runs before every http request (does not apply to websocket upgrade requests)
class BasicAuthFilter(private val username: String, private val password: String) : Plugin {

    override fun apply(app: Javalin) {
        app.before { ctx ->
            val matched = runCatching { ctx.basicAuthCredentials() }
                .fold({ (user, pass) -> (user == username && pass == password) }, { false })

            if (matched.not()) {
                ctx.header(Header.WWW_AUTHENTICATE, "Basic")
                throw UnauthorizedResponse()
            }
        }
    }

}
