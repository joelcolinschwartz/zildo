/**
 * The Land of Alembrum
 * Copyright (C) 2006-2016 Evariste Boussaton
 * 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package zildo.fwk.net;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import zildo.client.gui.menu.PlayerNameMenu;
import zildo.fwk.ZUtils;
import zildo.fwk.file.EasyBuffering;
import zildo.fwk.input.KeyboardInstant;
import zildo.fwk.net.Packet.PacketType;
import zildo.fwk.net.packet.AcceptPacket;
import zildo.fwk.net.packet.AskPacket;
import zildo.fwk.net.packet.AskPacket.ResourceType;
import zildo.fwk.net.packet.ConnectPacket;
import zildo.fwk.net.packet.EventPacket;
import zildo.fwk.net.packet.GetPacket;
import zildo.fwk.net.www.NetMessage;
import zildo.fwk.net.www.NetMessage.Command;
import zildo.fwk.net.www.WorldRegister;
import zildo.monde.WaitingSound;
import zildo.monde.dialog.WaitingDialog;
import zildo.monde.map.Area;
import zildo.monde.map.Case;
import zildo.monde.sprites.SpriteEntity;
import zildo.monde.util.Point;
import zildo.resource.Constantes;
import zildo.server.EngineZildo;
import zildo.server.MapManagement;
import zildo.server.Server;
import zildo.server.SpriteManagement;
import zildo.server.state.ClientState;

/**
 * Network server engine
 * 
 * Here we deals with packet between clients and server.
 * 
 * @author tchegito
 *
 */
public class NetServer extends NetSend {

	Server server;
	int counter;
	boolean lan;	// TRUE=LAN network / FALSE=Internet (no broadcast)
	
	// Default server name : player name
	String name=PlayerNameMenu.loadPlayerName();
	
	WorldRegister worldRegister;
	
	public static final int DEFAULT_SERVER_PORT = 1234;
	
	int nFrame=0;
	
	public NetServer(Server p_server, boolean p_lan) {
		super(null, DEFAULT_SERVER_PORT);
		server=p_server;
		lan=p_lan;
	    if (!lan) {
	    	// Launch the worldRegister
	    	worldRegister=new WorldRegister();
	    	worldRegister.start();
	    	registerServer();
	    }
	}
	
	protected void addClient(TransferObject client) {
	}
	
	public void run() {
		TransferObject source=null;
		try {
			if (isOpen()) {

				PacketSet packets=receiveAll();
				if (lan) {
					// Emits signal so then client could connect (every 20 frames)
					if (counter % 20 == 0) {
						sendPacket(PacketType.SERVER, null);
					}
					counter++;
				}
				
				Packet clientConnect=packets.getUniqueTyped(PacketType.CLIENT_CONNECT);
				if (clientConnect != null) {
					ConnectPacket conPacket=(ConnectPacket)clientConnect;
					boolean in=conPacket.isJoining();
					source=clientConnect.getSource();
					if (in) {
					    if (conPacket.getVersion() != Constantes.CURRENT_VERSION) {
						log("Serveur:Le client n'a pas la m�me version que le serveur ("+Constantes.CURRENT_VERSION+") : impossible de l'accepter");
					    } else {
						// Client is coming
						log("Serveur:Un client est arriv� !"+source.address.getHostName()+" port:"+source.address.getPort()+" named "+conPacket.getPlayerName());
	
						int zildoId=server.connectClient(source, conPacket.getPlayerName());
	
						AcceptPacket accept=new AcceptPacket(zildoId);
						sendPacket(accept, source);
					    }
					} else {
						// Client is leaving
						server.disconnectClient(source);
					}
					
				}

				if (server.isClients())  {
                    // We got clients
                    prepareSendEntities();
					PacketSet asks=packets.getTyped(PacketType.ASK_RESOURCE);
					for (Packet packet : asks) {
						AskPacket askPacket = (AskPacket) packet;
						//log("Somebody ask for a resource:"+askPacket.resourceType);
						
						// Send the resource
						switch (askPacket.resourceType) {
						case MAP:
							sendMap(askPacket.getSource());
							break;
                        case ENTITY:
                            sendEntities(askPacket.getSource(), askPacket.entire);
                            break;
							
						default:
								throw new RuntimeException("This resource type is not managed yet.");
						}
                    }
					sendSounds();
					
					sendMapChanges();
					
					sendDialogs();
					
					receiveKeyboards(packets.getTyped(PacketType.GET_RESOURCE));
					
					receiveEvents(packets.getTyped(PacketType.EVENT));
					
					if (EngineZildo.multiplayerManagement.isNeedToBroadcast()) {
						sendClientInfos();
						EngineZildo.multiplayerManagement.setNeedToBroadcast(false);
					}
				}
				ZUtils.sleep(5);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Receive keyboard from this GetResource's packet set.
	 * @param p_packets
	 */
	private void receiveKeyboards(PacketSet p_packets) {
		// Receive resource from clients (keyboard commands)
		for (Packet packet : p_packets) {
			GetPacket getPacket=(GetPacket) packet;
            EasyBuffering buffer = new EasyBuffering(getPacket.getBuffer());
            KeyboardInstant i = KeyboardInstant.deserialize(buffer);
            // Update clients command
            TransferObject client=getPacket.getSource();
            server.updateClientKeyboard(client, i);
		}
	}

    private void prepareSendEntities() {
        entitiesBuffer = null; // Reset entities temporary buffer
        // Find only modified entities
    }
    
	/**
	 * Send entities location to client.
	 * @param p_client target
	 */
	private EasyBuffering entitiesBuffer;
	private EasyBuffering entitiesSent;
    private void sendEntities(TransferObject p_client, boolean p_entire) {
        SpriteManagement spriteManagement = EngineZildo.spriteManagement;
        if (entitiesBuffer == null) {
            entitiesBuffer = spriteManagement.serializeEntities(p_entire);
        }
        ClientState cl=Server.getClientState(p_client);
        if (cl != null) {
	        if (cl.zildo.isInventoring()) {
	        	// Extra sprites : only for this client
	        	List<SpriteEntity> sprites=cl.zildo.guiCircle.getSprites();
	        	entitiesSent=new EasyBuffering(entitiesBuffer, sprites.size() * 100);
	        	entitiesSent.put(spriteManagement.serializeEntities(sprites, true));
	        } else {
	        	entitiesSent=entitiesBuffer;
	        }
	        GetPacket getPacket = new GetPacket(ResourceType.ENTITY, entitiesSent.getAll(), null);
	        sendPacket(getPacket, p_client);
        }
        
    }
	
	/**
	 * Send map to a given client.
	 * @param p_client
	 */
	private void sendMap(TransferObject p_client) {
		MapManagement mapManagement=EngineZildo.mapManagement;
		GetPacket getPacket=null;
		Area area=mapManagement.getCurrentMap();
		EasyBuffering buffer = new EasyBuffering();
		area.serialize(buffer);
		
		getPacket=new GetPacket(ResourceType.MAP, buffer.getAll(), area.getName());
		
		sendPacket(getPacket, p_client);
		log("Sending map ("+getPacket.getSize()+" bytes)");
	}
	
	private void sendSounds() {
        List<WaitingSound> queue = EngineZildo.soundManagement.getQueue();
        EasyBuffering broadCastbuffer = new EasyBuffering();
        EasyBuffering singleClientbuffer = new EasyBuffering();

        for (WaitingSound snd : queue) {
        	ByteBuffer b=snd.serialize().getAll();
        	b.flip();
        	if (snd.broadcast) {
        		broadCastbuffer.put(b);
        	} else if (snd.client != null){
        		// Send sound to the given client
        		singleClientbuffer.put(b);
                GetPacket getPacket = new GetPacket(ResourceType.SOUND, singleClientbuffer.getAll(), null);
                sendPacket(getPacket, snd.client);
        	}
        }
        if (queue.size() != 0) {
            // Send the sound info packet
            GetPacket getPacket = new GetPacket(ResourceType.SOUND, broadCastbuffer.getAll(), null);
            broadcastPacketToAllCients(getPacket);
        }
        EngineZildo.soundManagement.resetQueue();
    }

    public void broadcastPacketToAllCients(Packet p_packet) {
        Set<TransferObject> clientsLocation = server.getClientsLocation();
        for (TransferObject cl : clientsLocation) {
            if (cl != null) {
                sendPacket(p_packet, cl);
            }
        }
    }
    
    private void sendMapChanges() {
		Area map=EngineZildo.mapManagement.getCurrentMap();
		if (map.isModified()) {
			// Map has changed, so we must diffuse to clients
			Collection<Point> changes=map.getChanges();
			EasyBuffering buffer = new EasyBuffering();
			for (Point p :changes) {
				Case c=map.get_mapcase(p.getX(), p.getY());
				buffer.put(p.getX());
				buffer.put(p.getY());
				c.serialize(buffer);
			}
            GetPacket getPacket = new GetPacket(ResourceType.MAP_PART, buffer.getAll(), null);
            broadcastPacketToAllCients(getPacket);
			
			map.resetChanges();
		}
	}
	
	/**
	 * Send all waiting dialogs to clients (1 per client)
	 */
    private void sendDialogs() {
		List<WaitingDialog> dialogQueue=EngineZildo.dialogManagement.getQueue();
		EasyBuffering buffer = new EasyBuffering();
		if( dialogQueue.size() !=0) {
			for (WaitingDialog dial : dialogQueue) {
				if (dial.client != null || dial.console) {
					dial.serialize(buffer);
					
			        GetPacket getPacket = new GetPacket(ResourceType.DIALOG, buffer.getAll(), null);
			        if (!dial.console) {
			        	sendPacket(getPacket, dial.client);
			        } else {
			        	broadcastPacketToAllCients(getPacket);
			        }
			        buffer.clear();
				}
			}
			EngineZildo.dialogManagement.resetQueue();
		}
	}

    /**
     * Send a disconnect order to all clients (when server is leaving).
     */
    public void kill() {
    	// Notify end to clients
    	ConnectPacket p=new ConnectPacket(false, null, 0);
    	broadcastPacketToAllCients(p);
    	
    	unregisterServer();
    }
    
    /**
     * 
     * @param p_events
     */
    private void receiveEvents(PacketSet p_events) {
    	for (Packet p : p_events) {
    		EventPacket event=(EventPacket) p;
    		TransferObject source=event.getSource();
    		switch (event.type) {
    		case DIALOG_ENDED:
    			ClientState client=Server.getClientState(source);
    			EngineZildo.dialogManagement.stopDialog(client, false);
    			break;
    		default:
    			throw new RuntimeException("This kind of event ("+event.type+") is unknown.");
    		}
    	}
    }
    
    /**
     * Send the PlayerState objects to all clients to keep them aware of the score.
     */
    private void sendClientInfos() {
        Collection<ClientState> states = server.getClientStates();
        EasyBuffering buffer = new EasyBuffering();
        for (ClientState state : states) {
            state.serialize(buffer);
        }
        GetPacket getPacket = new GetPacket(ResourceType.CLIENTINFO, buffer.getAll(), null);
        broadcastPacketToAllCients(getPacket);
    }
    
    /**
     * Register server on the WORLD register !
     */
    private void registerServer() {
    	if (worldRegister != null) {
	    	NetMessage message=new NetMessage(Command.CREATE, name);
	    	worldRegister.askMessage(message, true);
    	}
    }
    
    /**
     * Unregister server on the WORLD register !
     */
    public void unregisterServer() {
    	if (worldRegister != null) {
    		NetMessage message=new NetMessage(Command.REMOVE, name);
    		worldRegister.askMessage(message, false);	// Synchronous because thread will die soon
    	}
    }
    
    /**
     * Update server on the WORLD register, especially about player's number.
     * @param p_nbPlayers
     */
    public void updateServer(int p_nbPlayers) {
    	if (worldRegister != null) {
	    	NetMessage message=new NetMessage(Command.UPDATE, name);
	    	message.getServerInfo().nbPlayers = p_nbPlayers;
	    	worldRegister.askMessage(message, true);
    	}
    }
}