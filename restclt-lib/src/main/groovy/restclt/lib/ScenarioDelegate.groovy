package restclt.lib

import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.transform.CompileStatic
import groovy.util.logging.Log

@CompileStatic
@Log
class ScenarioDelegate extends Script {
    Scenario scenario

    Scenario run() {
        log.info("Return built scenario")
        scenario
    }

    // Constractor
    ScenarioDelegate(String scenarioName) {
        log.info("new Scenario created for $scenarioName")
        this.scenario = new Scenario(scenarioName)
    }

    Given given(Map givenParams){
        Flow flow = new Flow()
        Given result = new Given(givenParams)
        flow.given = result
        this.scenario.flows.push(flow)
        log.fine("Given method called with Map: ${givenParams}")
        result
    }
    void andGiven(Map param) {
        Flow flow = this.scenario.flows.pop()
        flow.given.update(param)
        log.fine("Updated given clause as: ${flow.given}")
        this.scenario.flows.push(flow)
    }
    void andThen(Closure cl) {
        Flow flow = this.scenario.flows.pop()
        When when = flow.whens.pop()
        when.thens.add(new Then(cl))
        flow.whens.push(when)
        log.fine("push andThen clause to current when: ${when.method}")
        this.scenario.flows.push(flow)
    }
    void when(Map<String, String> whenParams){
        Flow flow = this.scenario.flows.pop()
        flow.whens.push(new When(whenParams.get('method')))
        this.scenario.flows.push(flow)
    }
    void then(Closure cl){
        Flow flow = this.scenario.flows.pop()
        When when = flow.whens.pop()
        when.thens.add(new Then(cl))
        flow.whens.push(when)
        log.fine("push then clause to current when: ${when.method}")
        this.scenario.flows.push(flow)
    }
    String json(Map obj){
        JsonOutput.toJson(obj)
    }
}
