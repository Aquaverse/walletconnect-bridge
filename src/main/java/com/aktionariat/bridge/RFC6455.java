package com.aktionariat.bridge;

import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.extensions.IExtension;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.HandshakeBuilder;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.protocols.IProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * RFC6455
 *
 * @author chzhong
 * @since 2022/9/16
 **/
public class RFC6455 extends Draft_6455 {

    private static final Logger LOG = LoggerFactory.getLogger(BridgeServer.class);

    /**
     * Handshake specific field for the upgrade
     */
    private static final String UPGRADE = "Upgrade";

    /**
     * Handshake specific field for the connection
     */
    private static final String CONNECTION = "Connection";

    private static final String WEBSOCKET = "websocket";

    /**
     * Handshake specific field for the key
     */
    private static final String SEC_WEB_SOCKET_KEY = "Sec-WebSocket-Key";

    private static boolean isWebSocketHandshake(ClientHandshake request) {
        return WEBSOCKET.equalsIgnoreCase(request.getFieldValue(UPGRADE))
                && request.hasFieldValue(SEC_WEB_SOCKET_KEY);
    }

    public RFC6455() {
    }

    public RFC6455(IExtension inputExtension) {
        super(inputExtension);
    }

    public RFC6455(List<IExtension> inputExtensions) {
        super(inputExtensions);
    }

    public RFC6455(List<IExtension> inputExtensions, List<IProtocol> inputProtocols) {
        super(inputExtensions, inputProtocols);
    }

    public RFC6455(List<IExtension> inputExtensions, int inputMaxFrameSize) {
        super(inputExtensions, inputMaxFrameSize);
    }

    public RFC6455(List<IExtension> inputExtensions, List<IProtocol> inputProtocols, int inputMaxFrameSize) {
        super(inputExtensions, inputProtocols, inputMaxFrameSize);
    }

    @Override
    public HandshakeBuilder postProcessHandshakeResponseAsServer(ClientHandshake request, ServerHandshakeBuilder response) throws InvalidHandshakeException {
        HandshakeBuilder builder = super.postProcessHandshakeResponseAsServer(request, response);
        String connectionHeaderValue = request.getFieldValue(CONNECTION);
        if (isWebSocketHandshake(request) && !UPGRADE.equalsIgnoreCase(connectionHeaderValue)) {
            LOG.warn("RFC6455 Protocol: Connection Header should be '" + UPGRADE + "' but is '" + connectionHeaderValue + "', fixing...");
            builder.put(CONNECTION, UPGRADE);
        }
        return builder;
    }

    @Override
    public Draft copyInstance() {
        ArrayList<IExtension> newExtensions = new ArrayList<>();
        for (IExtension knownExtension : getKnownExtensions()) {
            newExtensions.add(knownExtension.copyInstance());
        }
        ArrayList<IProtocol> newProtocols = new ArrayList<>();
        for (IProtocol knownProtocol : getKnownProtocols()) {
            newProtocols.add(knownProtocol.copyInstance());
        }
        return new RFC6455(newExtensions, newProtocols, getMaxFrameSize());
    }
}
