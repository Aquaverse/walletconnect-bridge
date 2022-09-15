FROM amazoncorretto:17

EXPOSE 8887

WORKDIR /app
ADD target/bridge.jar target/dependency/* lib/

CMD java -cp "/app/lib/*" com.aktionariat.bridge.BridgeServer

