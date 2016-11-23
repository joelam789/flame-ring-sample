
export class LoginRequest {
    msg: string = "LoginRequest";
    user_name: string = "";
    user_token: string = "";
    password: string = "";
}

export class EnterLobbyRequest {
    msg: string = "EnterLobbyRequest";
    user_name: string = "";
    user_token: string = "";
}

export class GetRoomListRequest {
    msg: string = "GetRoomListRequest";
    user_name: string = "";
    user_token: string = "";
}

export class CreateRoomRequest {
    msg: string = "CreateRoomRequest";
    user_name: string = "";
    user_token: string = "";
    room_name: string = "";
}

export class EnterRoomRequest {
    msg: string = "EnterRoomRequest";
    user_name: string = "";
    user_token: string = "";
    room_name: string = "";
}

export class ExitRoomRequest {
    msg: string = "ExitRoomRequest";
    user_name: string = "";
    user_token: string = "";
    room_name: string = "";
}

export class SendMessageRequest {
    msg: string = "SendMessageRequest";
    user_name: string = "";
    user_token: string = "";
    room_name: string = "";
    target: string = "";
    words: string = "";
    is_private: boolean = false;
}
