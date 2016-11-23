
export class EnterLobby {
  constructor(public message) { }
}

export class GetRoomList {
  constructor(public message) { }
}

export class CreateNewRoom {
  constructor(public result) { }
}

export class EnterRoom {
  constructor(public result) { }
}

export class ExitRoom {
  constructor(public message) { }
}

export class ComeIn {
  constructor(public user) { }
}

export class GetOut {
  constructor(public user) { }
}

export class Chat {
  constructor(public message) { }
}

export class Logout {
  constructor(public message) { }
}
