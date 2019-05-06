package restclt.client

import groovy.transform.CompileStatic

@CompileStatic
class Response {
    int status
    String body
    Map<String, List<String>> headers = null
}
