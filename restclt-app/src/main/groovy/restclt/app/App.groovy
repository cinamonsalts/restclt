/*
 * This Groovy source file was generated by the Gradle 'init' task.
 */
package restclt.app

import groovy.cli.picocli.CliBuilder
import groovy.cli.picocli.OptionAccessor
import restclt.client.RestClient
import restclt.client.httpclient4.HttpClient4RestClient
import restclt.client.Response

import static restclt.lib.ScriptBuilder.runWebFeature

class App {
    String getGreeting() {
        return 'Hello world.'
    }

    static void main(String[] args) {
        CliBuilder cli = new CliBuilder()
        OptionAccessor options = cli.parse(args)

        if(options.arguments().size() < 1) {
            cli.usage()
            System.exit(-1)
        }
        RestClient client = new HttpClient4RestClient()
        runWebFeature(new File(options.arguments().head()), client)
    }
}
