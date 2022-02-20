# spring-state-machine
Learning spring state machine

This project is a simple Spring boot example of how the state machine works. Spring Statemachine is a project developed by Spring team for developers to help them to use state machine concepts with Spring boot (https://spring.io/projects/spring-statemachine).

Spring state machine is very helpful when the application processes can be described as a number of finite states. for example using a debit card for payment - we can define some states: PRE_AUTHORIZATION, AUTHORIZATION, AUTHORIZATION_SUCCESS / AUTHORIZATION_FAILED, PAY_SUCCESS / PAY_FAILED, etc. Using state machine, we can describe exact state of the system at each stage and the exact transitions between states, which makes much easier the development and debugging of application than using traditional flows as if-then-else, etc.
