package restclt.lib

import groovy.transform.CompileStatic
import groovy.util.logging.Log
import restclt.client.Request
import restclt.client.Response
import restclt.client.RestClient

@CompileStatic
@Log
class Feature {
    String featureName
    List<Scenario> scenarios = new LinkedList<>()
    Feature(String featureName) {
        this.featureName = featureName
    }

    Boolean eval(RestClient client) {
        scenarios.collect{ it.eval(client) }.every()
    }

    @Override String toString() {
        return """\
Feature Information:
  name: ${featureName}
  Senarios: 
    ${scenarios.join("\n    ")}
"""
    }
}

@CompileStatic
@Log
class Scenario {
    String name
    List<Flow> flows = new ArrayList<>()
    Scenario(String name) {
        this.name = name
    }

    Boolean eval(RestClient client) {
        log.info("--- Evaluating scenario ${name}")
        flows.reverse().every {it.eval(client)}
    }

    @Override
    String toString() {
        return "Scenario: ${name}"
    }
}

@CompileStatic
class Given {
    String url
    Map headers
    Map cookies
    Map params
    Object data
    Given() {
        this.headers = new HashMap()
        this.cookies = new HashMap()
        this.params = new HashMap()
    }
    Given(Map params) {
        this()
        this.update(params)
    }
    void update(Map params) {
        if(params.containsKey("url")) this.url = params.get("url")
        if(params.containsKey("param")) this.params = params.get("param")
        if(params.containsKey("headers")) this.headers = params.get("headers")
        if(params.containsKey("cookies")) this.cookies = params.get("cookies")
        if(params.containsKey("data")) this.data = params.get("data")
    }
}

@CompileStatic
@Log
class Flow {
    Given given
    List<When> whens = new ArrayList<>()
    Boolean eval(RestClient client) {
        whens.collect {when ->
            log.info("    - evaluating case of ${when.method}...")
            Response response = when.exec(given, client)
            log.info("      calling assrtion through then for ${when.thens.size()}")
            when.thens.every {it.eval(response)}
        }.every()
    }
}

@CompileStatic
@Log
class When {
    String method
    List<Then> thens = new ArrayList<>()
    When(String method) {
        this.method = method
    }
    Response exec(Given given, RestClient client) {
        Request req = new Request()
        req.headers = given.headers
        req.url = given.url
        req.body = given.data
        req.params = given.params
        switch (this.method) {
            case "get":
                return client.get(req)
            case "head":
                return client.head(req)
            case "put":
                return client.put(req)
            case "post":
                return client.post(req)
            case "delete":
                return client.delete(req)
            default:
                return new Response()
        }
    }
}

@CompileStatic
@Log
class Then {
    Closure cl

    Then(Closure cl) {
        this.cl = cl
    }
    Boolean eval(Response response) {
        log.info("      calling assertion closure: ${cl.inspect()}")
        this.cl.putAt("response", response)
        return this.cl.call()
    }
}