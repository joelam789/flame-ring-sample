

export class Chatroom {
    name: string = "";
    users: Array<string> = new Array<string>();
}

export class RoomSummary {
    roomName: string = "";
    userCount: number = 0;
}

export class ChatMessage {
    room: string = "";
    user: string = "";
    target: string = "";
    words: string = "";
    isPrivate: boolean = false;
    fromSystem: boolean = false;
}

export class ChatState {

    currentPage: string = "";
    currentLang: string = "en";

    currentRoom: Chatroom = new Chatroom();
    rooms: Array<RoomSummary> = new Array<RoomSummary>();

    userName: string = "";
    userToken: string = "";

    serverAddress: string = "";
}
