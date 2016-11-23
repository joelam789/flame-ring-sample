

import {autoinject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';

import {ChatState, ChatMessage} from './chat-state';
import * as Messages from './messages';
import * as Handlers from './handlers';
import * as UI from './ui-messages';

@autoinject()
export class Messenger {

    wsClient: any = null;

    isRequesting: boolean = false;

    pendingMessageQueues: Map<string, Array<any>> = new Map<string, Array<any>>();
    pendingMessageQueueSize = 32;

    handlers: Array<Handlers.MessageHandler> = [];

    constructor(public chatState: ChatState, public eventChannel: EventAggregator) {
        this.handlers = [new Handlers.EnterLobby(), new Handlers.GetRoomList(),
                         new Handlers.CreateRoom(), new Handlers.EnterRoom(), new Handlers.ExitRoom(),
                         new Handlers.Chat()
                        ];
    }

    processPendingMessages(pageName: string = null) {
        if (pageName == null) {
            this.pendingMessageQueues.forEach((pendingMessages, page) => {
                while (pendingMessages.length > 0) {
                    let msg = pendingMessages.shift();
                    if (msg != null) this.eventChannel.publish(msg);
                }
            });
        } else {
            let pendingMessages = this.pendingMessageQueues.get(pageName);
            if (pendingMessages == undefined || pendingMessages == null) return;
            else {
                while (pendingMessages.length > 0) {
                    let msg = pendingMessages.shift();
                    if (msg != null) this.eventChannel.publish(msg);
                }
            }
        }
    }

    enqueueMessage(msg: any, pageName: string) {
        if (pageName == undefined || pageName == null || pageName.length <= 0) return;
        let pendingMessages = this.pendingMessageQueues.get(pageName);
        if (pendingMessages == undefined || pendingMessages == null) {
            this.pendingMessageQueues.set(pageName, []);
            pendingMessages = this.pendingMessageQueues.get(pageName);
        }
        pendingMessages.push(msg);
        if (pendingMessages.length > this.pendingMessageQueueSize) {
            while (pendingMessages.length > 0) {
                let msg = pendingMessages.shift();
                if (msg != null) this.eventChannel.publish(msg);
            }
        }
    }

    dispatch(msg: any, pageName: string = null, important: boolean = false) {
        if (pageName != null && pageName.length > 0) {
            if (pageName == this.chatState.currentPage) this.eventChannel.publish(msg);
            else {
                if (important) this.enqueueMessage(msg, pageName);
                else this.eventChannel.publish(msg);
            }
        } else {
            this.eventChannel.publish(msg);
        }
    }

    send(msg: any, cmd: string = "", needWaitForReply: boolean = false) {
        if (this.wsClient == null || this.wsClient.readyState != 1) return;
        if (needWaitForReply) this.isRequesting = true;
        this.wsClient.send(cmd + "/" + JSON.stringify(msg));
    }

    enterLobby() {
        console.log(this.chatState);
        if (this.chatState.serverAddress == null || this.chatState.serverAddress.length <= 0) return;
        if (this.chatState.userName == null || this.chatState.userName.length <= 0) return;
        if (this.chatState.userToken == null || this.chatState.userToken.length <= 0) return;
        if (this.wsClient != null) {
            this.wsClient.close();
            this.wsClient = null;
        }
        if (this.wsClient == null) {
            console.log("Connecting to server: " + this.chatState.serverAddress);
            this.isRequesting = true;
            this.wsClient = new WebSocket("ws://" + this.chatState.serverAddress);
            this.wsClient.onopen = () => {
                let reqmsg = new Messages.EnterLobbyRequest();
                reqmsg.user_name = this.chatState.userName;
                reqmsg.user_token = this.chatState.userToken;
                this.send(reqmsg, "lobby/enter", true);
            };

            this.wsClient.onmessage = (event) => {
                //console.log(event.data);
                let reply = JSON.parse(event.data);
                this.processServerMessage(reply);
            };

            this.wsClient.onclose = () => {
                let msg = "disconnected from server";
                console.log(msg);
                this.dispatch(new UI.Logout(msg));
            };
        }
    }

    listRooms() {
        let reqmsg = new Messages.GetRoomListRequest();
        reqmsg.user_name = this.chatState.userName;
        reqmsg.user_token = this.chatState.userToken;
        this.send(reqmsg, "lobby/list");
    }

    createNewRoom(newRoomName: string) {
        let reqmsg = new Messages.CreateRoomRequest();
        reqmsg.user_name = this.chatState.userName;
        reqmsg.user_token = this.chatState.userToken;
        reqmsg.room_name = newRoomName;
        this.send(reqmsg, "room/create", true);
    }

    enterRoom(roomName: string) {
        let reqmsg = new Messages.EnterRoomRequest();
        reqmsg.user_name = this.chatState.userName;
        reqmsg.user_token = this.chatState.userToken;
        reqmsg.room_name = roomName;
        this.send(reqmsg, "room/enter", true);
    }

    exitRoom(roomName: string) {
        let reqmsg = new Messages.ExitRoomRequest();
        reqmsg.user_name = this.chatState.userName;
        reqmsg.user_token = this.chatState.userToken;
        reqmsg.room_name = roomName;
        this.send(reqmsg, "room/exit", true);
    }

    logout() {
        if (this.wsClient != null) {
            this.wsClient.close();
        }
        this.wsClient = null;
    }

    sendChatMessage(msg: string, toUser: string = "", isPrivate: boolean = false) {
        let req = new Messages.SendMessageRequest();
        req.is_private = isPrivate;
        req.room_name = this.chatState.currentRoom.name;
        req.target = toUser;
        req.user_name = this.chatState.userName;
        req.user_token = this.chatState.userToken;
        req.words = msg;
        this.send(req, "room/chat");
    }

    processServerMessage(msg: any) {

        for (let handler of this.handlers) {
            if (handler.handle(this, msg)) return;
        }

        // print unknown messages
        console.log(JSON.stringify(msg));
        console.log(msg);

    }

}
