package restclt.lib

import groovy.transform.CompileStatic
import org.codehaus.groovy.control.CompilerConfiguration
import restclt.client.RestClient

@CompileStatic
class ScriptBuilder {
    static void runWebFeature(File dsl, RestClient client) {
        CompilerConfiguration config = new CompilerConfiguration()
        config.scriptBaseClass = ScriptDelegate.class.name
        GroovyShell shell = new GroovyShell(System.classLoader, config)
        ScriptDelegate result = shell.parse(dsl.text) as ScriptDelegate
        result.client = client
        println result.run()
    }
}
