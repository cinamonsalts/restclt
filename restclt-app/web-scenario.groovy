feature "Simple web request feature"

scenario "Get localhost API service", {
    given url: "http://localhost:8080/hello"
    andGiven param: [q: "Groovy+curry"]
    when method: 'get'
    then {response.status == 200}
    andThen {response.body == json([message: "hello, world"])}
    when method: 'post'
    andGiven param: [:]
    andGiven headers: ["Content-Type": "application/json;utf-8"]
    andGiven data: [name: "John"]
    then {response.body == json([message: "hello, John"])}
}

scenario "Get something to web site", {
    given url: "https://www.google.co.jp"
    when method: 'get'
    then {response.status == 200}
}
