package restclt.lib

import groovy.transform.CompileStatic
import restclt.client.RestClient

@CompileStatic
abstract class ScriptDelegate extends Script {
    Feature feature
    RestClient client
    void feature(String feature) {
        println "feature name set to $feature"
        this.feature= new Feature(feature)
    }
    void scenario(String name, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ScenarioDelegate) Closure cl) {
        ScenarioDelegate scenario = new ScenarioDelegate(name)
        Closure code = cl.rehydrate(scenario, this, this)
        code.delegate = scenario
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
        this.feature.scenarios.add(code.getProperty("scenario") as Scenario)
    }
    abstract def runCode()
    @Override
    Object run() {
        println "Welcome to API testing framework"
        println "## Building test feature.."
        println "Use ${client} as RestClient"
        runCode()
        println this.feature
        println "## Build completed..."
        println "## Evaluating all scenarios"
        Boolean result = this.feature.eval(client)
        println "## Evaluation completed..."
        result
    }
}
