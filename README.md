# REST API test client framework

Framework allows you to test RESTful API with Groovy DSL

## Install

Clone repository and build a binary.

On Linux or Mac OS  
```bash
git clone https://github.com/cinamonsalts/restclt.git
cd restclt
./gradlew build
```

On Windows
```cmd
git clone https://github.com/cinamonsalts/restclt.git
cd restclt
./gradlew.bat build
```

## Usage

Run java command with a commandline option point to a scenario DSL file.

First of all, create your API test like this:
```groovy
// Feature of API service.
feature "My API service"

// Then, create a scenario.
scenario "Test GET API scenario", {
    // given allows you to specify URL, request parameter and request body
    given url: "https://www.google.com"
    andGiven param: [q: 'Groovy+DSL']
    // when specifies http method
    when 'get'
    // then establish a request and verify response in a closure
    then { response.status == 200}
}
```

```cmd
java -jar restclt-app/build/libs/restclt-app-0.1.0-all.jar web-scenario.groovy
```


[here]: https://github.io/CinamonSalt/downloads/latest