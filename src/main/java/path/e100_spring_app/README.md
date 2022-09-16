# 100 - Using the Path SDK in a Spring application

This example shows how to use the SDK in a Spring application. A fake origination flow has been implemented and can be accessed
through HTTP requests.

## GatewayManager

In this example we use a Gateway Manager to load the gateway.yaml configuration and provide an API to access a Gateway by
a client ID. The step happens on application initialization so that all Gateways are configured and accessible before any requests
hit the app.

_Note: this is not an SDK standard and can be set up however you'd like in your own application._

## ExampleOriginationAccessor

The ExampleOriginationAccessor implements the `start` and `answerChallenge` endpoints. The challenge responses are semi-validated
by ensuring that all questions have a response. In a production application you'd want to verify the responses based on the
prompt type.

## Facilities

Two facilities are implemented and configured in the gateway.yaml: MemoryStore and Des3EncryptionService. These facilities
are implemented in this project, but they could be pulled in via Gradle dependencies if desired.

## OriginationsController

The OriginationsController is a Spring controller that interacts the Gateway SDK to provide the actual functionality. The
OriginationGateway is unique in that it handles session management. Other Gateway-controllers would likely just forward calls to the
SDK and return the response.

## SessionFilter & MdxRequestContextFilter

The SessionFilter and MdxRequestContextFilter are Spring filters that handle inflating and registering the `Session` and
`RequestContext`. You could put this behavior somewhere else if desired, but Spring filters are a natural fit.

## Interacting with the application

You can start an origination flow by making a `POST` request to `/originations/start`:

```shell
curl --location --request POST 'localhost:8080/demo/originations/start'
```

You should get back a response with a set of challenges:

```shell
{
    "origination": {
        "id": "70cfc7f9-9f23-43dd-9f8d-85934ea6ae09",
        "challenges": [
            {
                "id": "basicState",
                "prompt": "Let's start with some basic info",
                "title": "HELLO THERE!",
                "questions": [
                    {
                        "id": "First Name",
                        "prompt": "First Name",
                        "prompt_type": "TEXT"
                    },
                    {
                        "id": "Last Name",
                        "prompt": "Last Name",
                        "prompt_type": "TEXT"
                    },
                    {
                        "id": "Email",
                        "prompt": "Email",
                        "prompt_type": "EMAIL"
                    },
                    {
                        "id": "Mobile Phone Number",
                        "prompt": "Mobile Phone Number",
                        "prompt_type": "PHONE"
                    }
                ]
            }
        ]
    }
}
```

_Note: you should also see a header in the response called `mx-session-key`. You will need this for future requests._

You can respond to a challenge by making a `PUT` request to `/originations/:id/challenges/:challengeId` with the challenge
filled out:

```shell
{
    "id": "basicState",
    "prompt": "Let's start with some basic info",
    "title": "HELLO THERE!",
    "questions": [
        {
            "id": "First Name",
            "prompt": "First Name",
            "prompt_type": "TEXT",
            "answer": "Robert"
        },
        {
            "id": "Last Name",
            "prompt": "Last Name",
            "prompt_type": "TEXT",
            "answer": "Hanna"
        },
        {
            "id": "Email",
            "prompt": "Email",
            "prompt_type": "EMAIL",
            "answer": "robert@example.com"
        },
        {
            "id": "Mobile Phone Number",
            "prompt": "Mobile Phone Number",
            "prompt_type": "PHONE",
            "answer": "555-555-5555"
        }
    ]
}
```

You should then receive a response with the next challenge to be completed. You can keep submitting challenge responses
until no more are left. You will know you are finished when you get a `204`.