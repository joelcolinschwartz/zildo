

# Introduction #

Here, we will briefly describe the class hierarchy, from top view, and going into the model more precisely, with details about exchanged data.


# General view #

Here is the client/server separation, with more representative classes. All links mean **composition**.

http://lh5.ggpht.com/_q_5wHG9LsPk/SxJqR2Ev-eI/AAAAAAAABfM/fPn_krYwemA/classView.PNG

Client and server don't manage the same data. Client has a basic view on everything it has to render, as server has to handle all the world. We'll see later that there's some exceptions, designed for performance issues.

## Concept ##

The general idea is that clients are just terminals, which only send basic orders to server. Like key pressed, essentially, but other events too, in order to display correctly things according to the client's speed.

On the other side, server is responsible for the general state of the world. It only sends to client what they need. For example, a client doesn't know if a given sprite is an enemy or a NPC. He doesn't know neither how much money has the Zildo he's controlling. He just have simplified informations about each rendered sprite, each tile of the current map,etc. We'll discuss the exhaustivity later.

## Data protocol ##

To communicate between server and clients, we only use UDP because we don't need all TCP security checks. We need more speed to have acceptable pings on all clients.

A mini framework exists in Zildo to handle such send/receive operations. Here is a brief representation of it, where all links mean **unheritance**:

http://lh6.ggpht.com/_q_5wHG9LsPk/SxJwFQtiDiI/AAAAAAAABfQ/c_jmfokB8f4/zildonetwork.PNG

The main class is [TransferObject](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/fwk/net/TransferObject.java). It's the most low-level one. It handles socket creation, channel opening, and broadcasting.

The second one, [NetSend](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/fwk/net/NetSend.java) handles send/receive operations by encapsulating them in Packet classes. We'll see that in the next part.

And the more evoluated objects are designed for client and server purpose. They contains all precise interactions between such actors. We got an [InternetClient](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/fwk/net/InternetClient.java) class, opposed to [NetClient](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/fwk/net/NetClient.java), which are respectively the www client, and the LAN client.

# Packet presentation #

There is a class hierarchy, where all links mean **unheritance**.

http://lh6.ggpht.com/_q_5wHG9LsPk/SxJ0JMhzd1I/AAAAAAAABfU/tBMZ55svWdA/packetView.PNG

Let's see which are the goal of each packet:

  * ## `ServerPacket` [(SVN)](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/fwk/net/packet/ServerPacket.java) ##

> This packet signal that a server exists on a given IP adress. In local area network,  it's sent over a given delay in order to clients can see that a server is running.
> On an internet game, it's a nonsense. Client just have to pick up the server's IP to connect to. So this packet is never used in internet game.

  * ## `ConnectPacket` [(SVN)](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/fwk/net/packet/ConnectPacket.java) ##

> Client asks server to connect into its game.

  * ## `AcceptPacket` [(SVN)](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/fwk/net/packet/AcceptPacket.java) ##

> Server sends an acknowledgement to client for him to join the game, returning to him his zildo's ID by the way.

  * ## `AskPacket` [(SVN)](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/fwk/net/packet/AskPacket.java) ##

> With this packet, a client can ask server for a given resource : map, sprite ...

  * ## `GetPacket` [(SVN)](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/fwk/net/packet/GetPacket.java) ##

> This packet contains a resource (see **ResourceType** enum in [AskPacket](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/fwk/net/packet/AskPacket.java) class to have the entire resource types). It can be a set of same kind of resources, or unique one.

  * ## `EventPacket` [(SVN)](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/fwk/net/packet/EventPacket.java) ##

> It's a packet specifically sent by client, to signal an identifiable event, like a dialog end, or something else, which is relevant to client.

# Dialog #

Let's see concretely the packets exchanged between server and his clients.

## Connection ##

### Find the server ###

As we already said previously, there is two different behaviors between LAN and www game :

  * **LAN** : server sends repeateadly a **ServerPacket** with broadcast on all IP addresses with this form x.x.x.255 where the 'x' are server's IP adress members. It means that every client with same three members is targeted.

  * **www** : server sends nothing. Client has to select the server in a list.

### Connect the client ###

On a LAN game, client should receive the **ServerPacket**, so he got the server's address.

In both cases, client sends a **ConnectPacket** to the server, who answers by a **AcceptPacket** if he's ok (he always is).

Just after that, the client requires several things : map and entities. So he asks for via an **AskPacket** completed with resource type.

Then the server will return the asked resources.

**Note:** Every entity is sent at this time, because client hasn't any of them at the beginning. We'll see that it will be different after that.

## During the game ##

At each frame, client sends :

  * keys pressed
  * **AskPacket** to get updated entities

Server deals with all **AskPacket** he received, and sends corresponding resources.

When he sends entities to clients, there is two noticeable things :
  * server only sends a delta between the current frame and the previous one. This is for performance issues. Without that, internet gaming was impossible.

  * there is some entities which are client specific. For example: the content of the client's inventory. These entities are not sent to other clients.