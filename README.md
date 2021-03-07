# HttpServerLoom
## Using Project Loom with embedded HttpServer
This project tries to integrate Project Loom with HttpServer.

On first sight, this seems to be an ideal http server to use virtual threads. So I wrapped the whole thing up using Virtual Threads.

The initial tests worked ok (up to a few thousands requests in parallel), but I noticed instabilities as soon as I started to scale up to 10,000.
Requests are failing beyond this threshold. I have not investigated the cause, but please go ahead if you want to give it a shot. If would be great if you are able to fix it. 

HttpServer is very simple. I made it a little bit easier to turn it into a real server, adding a router and dividing the HttpServer into the server itself and 
the context that is created from the server, very much like most servlet engines.

I was hoping to get this server to work neatly with project Loom, but it seems not to be trivial.

Please note: this is just a try-out, a play-ground. It would be nice if Oracle would make the embedded HttpServer suitable for virtual threads.
