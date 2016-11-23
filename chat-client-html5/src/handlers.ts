

import {ChatState, RoomSummary, ChatMessage} from './chat-state';
import {Messenger} from './messenger';
import * as Messages from './messages';
import * as UI from './ui-messages';

export interface MessageHandler {
    handle(messenger: Messenger, msg: any): boolean;
}

export class EnterLobby implements MessageHandler {
    handle(messenger: Messenger, msg: any): boolean {
        if (msg.msg == "EnterLobbyReply") {
            messenger.isRequesting = false;
            messenger.dispatch(new UI.EnterLobby(msg.result));
            return true;
        }
        return false;
    }
}

export class GetRoomList implements MessageHandler {
    handle(messenger: Messenger, msg: any): boolean {
        if (msg.msg == "GetRoomListReply") {
            messenger.chatState.rooms = [];
            for (let room of msg.rooms) {
                let newone = new RoomSummary();
                newone.roomName = room.room_name;
                newone.userCount = room.user_count;
                messenger.chatState.rooms.push(newone);
            }
            messenger.dispatch(new UI.GetRoomList(msg.result));
            return true;
        }
        return false;
    }
}

export class CreateRoom implements MessageHandler {
    handle(messenger: Messenger, msg: any): boolean {
        if (msg.msg == "CreateRoomReply") {
            messenger.isRequesting = false;
            if (msg.result.toLowerCase() == "ok") {
                messenger.chatState.rooms = [];
                for (let room of msg.rooms) {
                    let newone = new RoomSummary();
                    newone.roomName = room.room_name;
                    newone.userCount = room.user_count;
                    messenger.chatState.rooms.push(newone);
                }
                messenger.chatState.currentRoom.name = msg.room_name;
                messenger.chatState.currentRoom.users = JSON.parse(JSON.stringify(msg.users));
            }

            messenger.dispatch(new UI.CreateNewRoom({
                room: msg.room_name,
                message: msg.result
            }));
            return true;
        }
        return false;
    }
}

export class EnterRoom implements MessageHandler {
    handle(messenger: Messenger, msg: any): boolean {
        if (msg.msg == "EnterRoomReply") {
            messenger.isRequesting = false;
            if (msg.result.toLowerCase() == "ok") {
                messenger.chatState.currentRoom.name = msg.room_name;
                messenger.chatState.currentRoom.users = JSON.parse(JSON.stringify(msg.users));
            }
            messenger.dispatch(new UI.EnterRoom({
                room: msg.room_name,
                message: msg.result
            }));
            return true;
        }
        return false;
    }
}

export class ExitRoom implements MessageHandler {
    handle(messenger: Messenger, msg: any): boolean {
        if (msg.msg == "ExitRoomReply") {
            messenger.isRequesting = false;
            if (msg.result.toLowerCase() == "ok") {
                messenger.chatState.rooms = [];
                for (let room of msg.rooms) {
                    let newone = new RoomSummary();
                    newone.roomName = room.room_name;
                    newone.userCount = room.user_count;
                    messenger.chatState.rooms.push(newone);
                }
            }
            messenger.dispatch(new UI.ExitRoom(msg.result));
            return true;
        }
        return false;
    }
}

export class Chat implements MessageHandler {
    handle(messenger: Messenger, msg: any): boolean {
        if (msg.msg == "SendMessageReply") {
            if (msg.from_system == true) {
                if (msg.words.toLowerCase().indexOf("left") >= 0) {
                    let idx = messenger.chatState.currentRoom.users.indexOf(msg.user_name);
                    if (idx >= 0) messenger.chatState.currentRoom.users.splice(idx, 1);
                    messenger.dispatch(new UI.GetOut(msg.user_name), "chatroom", true);
                } else if (msg.words.toLowerCase().indexOf("entered") >= 0) {
                    let idx = messenger.chatState.currentRoom.users.indexOf(msg.user_name);
                    if (idx < 0) messenger.chatState.currentRoom.users.push(msg.user_name);
                    messenger.dispatch(new UI.ComeIn(msg.user_name), "chatroom", true);
                }
            }
            let chatmsg = new ChatMessage();
            chatmsg.user = msg.user_name;
            chatmsg.room = msg.room_name;
            chatmsg.target = msg.target;
            chatmsg.words = msg.words;
            chatmsg.isPrivate = msg.is_private;
            chatmsg.fromSystem = msg.from_system;
            messenger.dispatch(new UI.Chat(chatmsg));
            return true;
        }
        return false;
    }
}
