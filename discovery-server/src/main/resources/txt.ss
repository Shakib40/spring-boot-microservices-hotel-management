server:
  port: 8761

spring:
  application:
    name: discovery-service
  config:
    import: "optional:configserver:http://config-server:8888"

# eureka:
#   instance:
#     hostname: localhost
#   client:
#     register-with-eureka: false
#     fetch-registry: false
#     service-url:
#       defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
#   server:
#     enable-self-preservation: false

# management:
#   endpoints:
#     web:
#       exposure:
#         include: "*"

# logging:
#   level:
#     com.netflix.eureka: INFO
#     com.netflix.discovery: INFO