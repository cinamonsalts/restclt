package restclt.client.httpclient4

import groovy.json.JsonOutput
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import groovy.util.logging.Log
import org.apache.http.Header
import org.apache.http.HttpHost
import org.apache.http.HttpRequest
import org.apache.http.NameValuePair
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import org.apache.http.message.BasicNameValuePair
import restclt.client.Request
import restclt.client.Response
import restclt.client.RestClient


@Log
@TypeChecked
class HttpClient4RestClient implements RestClient{

    @Override
    Response get(Request req) {
        URIBuilder uriBuilder = new URIBuilder(req.url)
        List<NameValuePair> params = req.params.collect {param ->
            log.fine("Add query param: ${param.key} => ${param.value}")
            new BasicNameValuePair(param.key, param.value) as NameValuePair
        }
        if(params.size()>0) uriBuilder.parameters = params
        URI requestURI = uriBuilder.build()
        HttpGet httpGet = new HttpGet(requestURI)
        println httpGet.URI
        httpGet.headers = req.headers.collect {header ->
            new BasicHeader(header.key, header.value) as Header
        }.toArray() as Header[]
        return doRequest(requestURI.host+":"+requestURI.port, httpGet)
    }

    @Override
    Response put(Request req) {
        return new Response()
    }

    @Override
    Response delete(Request req) {
        return new Response()
    }

    @Override
    Response post(Request req) {
        URIBuilder uriBuilder = new URIBuilder(req.url)
        List<NameValuePair> params = req.params.collect {param ->
            log.fine("Add query param: ${param.key} => ${param.value}")
            new BasicNameValuePair(param.key, param.value) as NameValuePair
        }
        if(params.size()>0) uriBuilder.parameters = params
        URI requestURI = uriBuilder.build()
        HttpPost httpPost = new HttpPost(requestURI)
        httpPost.headers = req.headers.collect {header ->
            new BasicHeader(header.key, header.value) as Header
        }.toArray() as Header[]
        String body = JsonOutput.toJson(req.body)
        httpPost.entity = new StringEntity(body, ContentType.parse(req.headers.getOrDefault("Content-Type", "application/json;utf-8")))
        return doRequest(requestURI.host+":"+requestURI.port, httpPost)
    }

    @Override
    Response head(Request req) {
        return null
    }

    @Override
    Response connect(Request req) {
        return null
    }

    @Override
    Response patch(Request req) {
        return null
    }

    @Override
    Response trace(Request req) {
        return null
    }

    @Override
    Response options(Request req) {
        return null
    }

    private Response doRequest(String host, HttpRequest req) {
        Response res = null
        CloseableHttpClient client = HttpClients.createDefault()
        CloseableHttpResponse response = client.execute(HttpHost.create(host), req)
        try {
            res = convert(response)
        } finally {
            response.close()
            client.close()
        }
        return res
    }

    private Response convert(CloseableHttpResponse httpResponse) {
        Response response = new Response()
        response.status = httpResponse.getStatusLine().statusCode
        Object headerMap = Arrays.asList(httpResponse.allHeaders).inject(new HashMap<String, List<String>>()) { Map<String, List<String>> result, Header header ->
            log.fine("Header: ${header.name} -> ${header.value}")
            List<String> valuesList = result.getOrDefault("${header.name}",[])
            valuesList.push("${header.value}" as String)
            result.put("${header.name}" as String, valuesList)
        }
        response.headers = headerMap as Map
        response.body = org.apache.http.util.EntityUtils.toString(httpResponse.entity)
        return response
    }
}
