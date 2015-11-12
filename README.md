# Melody-Chat-Server
Chat Server made in Java. Only works on UDP as of now.
A testing client is also available but is not fully featured.

###Starting the Server
Can be started as java -jar MelodyChat <port>

###Registering with server
Clients who wish to register with  the server need to  send a connection request in the format
/c/name.   eg: /c/Tom

Server reads the message, generates a unique ID for the client and adds the client to it's subscribed clients list.

A unique id is sent to client in a response message /c/uid. Client needs to store the uid for subsequent message exchanges.
eg: /c/1567

###Messages
The messages from client that needs to be relayed to all the online clients have to be in the format /m/message.
Server reads the /m/ messages and sends them to all the other clients by appending the name of the originating client
to the message.
eg: /m/Hey!

###Ping
Server sends /p/ping messages every 3 seconds to clients to check if the client is alive. Server expects the client to
respond with a /p/ message. If there is no responce from the client for 5 consequetive /p/ messages from the server, the client 
is removed from the client list. This will send a notification to all online clients that the client has timed out.
eg:/p/ping from server. Client has to respond with /p/

###Disconnection
Client may send /d/uid message to server to inform the server about disconnection. This will send a notification to all online
clients that the client has disconnected. Server sends a /d/ message to all connected clients when shutting down.
eg: /d/uid

###Admin options
Following command line options are available for the server admin. 

/users - List all the online users along with a id.  eg 0 Tom  1 Foo

/kick <id> - Kicks the user corresponding to id.

/quit - Shuts down the server. Sends a /d/ message to all connected clients.



######Known Issues:
Client:
Client UI is buggy. Login form is boring. Client window is not resizeable. 

Server:
Server doesn't provide any api to client to query online users. No 1 to 1 chat available yet.





