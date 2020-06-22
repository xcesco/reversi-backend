package org.abubusoft.reversi.server.services;

/**
 * The job of the connection manager is to abstract the connection between two computers over the network.
 * It receives data from the layer above it, the stream manager, and transmits data to the layer below it,
 * the platform packet module.
 * The connection manager level is still unreliable. It does not guarantee delivery of data sent to it.
 */
public class ConnectionManager {
}
