package restclt.client

import groovy.transform.CompileStatic
import restclt.client.Response

@CompileStatic
interface RestClient {
    Response get(Request req)
    Response head(Request req)
    Response put(Request req)
    Response post(Request req)
    Response delete(Request req)
    Response connect(Request req)
    Response options(Request req)
    Response trace(Request req)
    Response patch(Request req)
}
