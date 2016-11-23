
import {autoinject, customElement} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {Router} from 'aurelia-router';

import {DialogService} from 'aurelia-dialog';
import {I18N} from 'aurelia-i18n';

import {CreateNewChatroomDialog} from './create-room';
import {ChatState, RoomSummary} from './chat-state';
import {Messenger} from './messenger';
import * as UI from './ui-messages';

@autoinject()
export class LobbyPage {

    userName: string = "";

    rooms: Array<RoomSummary> = [];

    alertMessage: string = null;

    refreshedRooms: boolean = false;

    refreshTimer: any = null;

    constructor(public dialogService: DialogService, public router: Router, 
                public i18n: I18N, public chatState: ChatState, 
                public messenger: Messenger, public eventChannel: EventAggregator) {

        this.eventChannel.subscribe(UI.GetRoomList, data => {
            this.rooms = JSON.parse(JSON.stringify(this.chatState.rooms));
            this.rooms.push(new RoomSummary()); // add a fake one
            this.refreshedRooms = true;
        });

        this.eventChannel.subscribe(UI.CreateNewRoom, data => {
            if (data.result.message.toLowerCase() == "ok") {
                console.log("created a new room: " + data.result.room);
                this.router.navigate("chatroom"); // go to chatroom
            } else {
                console.log("failed to create a new room: " + data.result.message);
                this.alertMessage = data.result.message;
            }
        });

        this.eventChannel.subscribe(UI.EnterRoom, data => {
            if (data.result.message.toLowerCase() == "ok") {
                console.log("jump to chatroom: " + data.result.room);
                this.router.navigate("chatroom"); // go to chatroom
            } else {
                console.log("failed to enter a room: " + data.result.message);
                this.alertMessage = data.result.message;
            }
        });

        this.eventChannel.subscribe(UI.Logout, data => {
            this.logout();
        });
        
    }

    activate(parameters, routeConfig) {

        this.chatState.currentPage = "lobby";
        
        this.changeLang(this.chatState.currentLang);

        this.userName = this.chatState.userName;

        this.rooms = JSON.parse(JSON.stringify(this.chatState.rooms));
        this.rooms.push(new RoomSummary()); // add a fake one

        if (this.rooms.length > 1) this.refreshedRooms = true;

        this.messenger.processPendingMessages("lobby");

        if (this.refreshTimer != null) {
            clearInterval(this.refreshTimer);
            this.refreshTimer = null;
        }
        this.refreshTimer = setInterval(() => {
            this.messenger.listRooms();
        }, 1000);
        
    }

    deactivate() {
        if (this.refreshTimer != null) {
            clearInterval(this.refreshTimer);
            this.refreshTimer = null;
        }
        this.rooms = [];
        this.refreshedRooms = false;
    }

    get canShowRooms() {
        return this.rooms.length > 0 && this.refreshedRooms;
    }

    get canEnterRooms() {
        return this.rooms.length > 0 && !this.messenger.isRequesting;
    }

    get isEmptyAlertMessage() {
        return this.alertMessage == null || this.alertMessage.length <= 0;
    }

    dismissAlertMessage() {
        this.alertMessage = null;
    }

    addRoom() {
        console.log("going to create a new room");
        this.dialogService.open({viewModel: CreateNewChatroomDialog, model: "" })
        .then(response => {
            console.log(response);
            if (!response.wasCancelled && response.output.length > 0) {
                console.log("going to add a new room: " + response.output);
                this.messenger.createNewRoom(response.output);
            } else {
                console.log('Give up creating a new room');
            }
        });
    }

    enterRoom(roomName: string) {
        console.log("going to enter room: " + roomName);
        this.messenger.enterRoom(roomName);
    }

    logout() {
        console.log("jump to login...");
        this.messenger.logout();
        this.router.navigate("login"); // go to login
    }

    changeLang(lang: string) {
        this.i18n.setLocale(lang)
        .then(() => {
            this.chatState.currentLang = this.i18n.getLocale();
            console.log(this.chatState.currentLang);
        });
    }
}
