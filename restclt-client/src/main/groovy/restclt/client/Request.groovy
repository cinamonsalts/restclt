package restclt.client

import groovy.transform.CompileStatic

@CompileStatic
class Request {
    String url
    Map<String, String> headers
    Map<String, String> params
    Object body
}
