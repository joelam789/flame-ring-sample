
import {autoinject, customElement} from 'aurelia-framework';
import {EventAggregator, Subscription} from 'aurelia-event-aggregator';
import {Router} from 'aurelia-router';

import {DialogService} from 'aurelia-dialog';
import {I18N} from 'aurelia-i18n';

import {ChatState, ChatMessage} from './chat-state';
import {Messenger} from './messenger';
import * as UI from './ui-messages';

@autoinject()
export class ChatroomPage {

    static readonly MAX_CHAT_HISTORY_LINE_COUNT = 32;

    chatHistory: Array<ChatMessage> = new Array<ChatMessage>();

    userName: string = "";
    roomName: string = "";

    chatMessage: string = "";

    selectedUser: string = "";
    selectedUserIndex: number = 0;

    selectedChatFlags: Array<string> = [];

    users: Array<string> = [];

    subscribers: Array<Subscription> = [];

    alertMessage: string = null;

    refreshTimer: any = null;

    pressKeyCallback = (event) => {
        if (event.which == 13 || event.keyCode == 13) {
            if (this.canSendChatMessage) this.sendChatMessage();
            return false;
        }
        return true;
    };

    constructor(public dialogService: DialogService, public router: Router, 
                public i18n: I18N, public chatState: ChatState, 
                public messenger: Messenger, public eventChannel: EventAggregator) {

        this.chatHistory = [];
        this.subscribers = [];
        
    }

    activate(parameters, routeConfig) {

        this.chatState.currentPage = "chatroom";
        
        this.changeLang(this.chatState.currentLang);

        this.userName = this.chatState.userName;
        this.roomName = this.chatState.currentRoom.name;

        this.updateUserList();

        window.addEventListener('keypress', this.pressKeyCallback, false);
        
    }

    deactivate() {
        window.removeEventListener('keypress', this.pressKeyCallback);
    }

    attached() {

        this.subscribers = [];

        this.subscribers.push(this.eventChannel.subscribe(UI.ExitRoom, data => {
            if (data.message.toLowerCase() == "ok") {
                console.log("back to lobby");
                this.router.navigate("lobby"); // back to lobby
            } else {
                this.alertMessage = data.message; // show error
            }
        }));

        this.subscribers.push(this.eventChannel.subscribe(UI.ComeIn, data => {
            this.updateUserList();
        }));

        this.subscribers.push(this.eventChannel.subscribe(UI.GetOut, data => {
            this.updateUserList();
        }));

        this.subscribers.push(this.eventChannel.subscribe(UI.Chat, data => {
            if (this.chatHistory.length >= ChatroomPage.MAX_CHAT_HISTORY_LINE_COUNT) {
                this.chatHistory.splice(0, 1);
            }
            this.chatHistory.push(data.message);
            setTimeout(() => { // make it a little bit delay because aurelia needs some time to apply changes to UI
                let elem = document.getElementById('chatbox');
                if (elem != undefined && elem != null) elem.scrollTop = elem.scrollHeight - elem.clientHeight;
            }, 80);
            
        }));

        this.subscribers.push(this.eventChannel.subscribe(UI.Logout, data => {
            this.logout();
        }));

        this.messenger.processPendingMessages("chatroom");

    }

    detached() {
        for (let item of this.subscribers) item.dispose();
        this.subscribers = [];
    }

    get isEmptyAlertMessage() {
        return this.alertMessage == null || this.alertMessage.length <= 0;
    }

    get tagForAnyone() {
        return this.i18n.tr("chatroom.all");
    }

    get canSendChatMessage() {
        return !this.messenger.isRequesting && !this.router.isNavigating && this.chatMessage.length > 0;
    }

    dismissAlertMessage() {
        this.alertMessage = null;
    }

    updateUserList() {
        let currentSelectedUser = "";
        if (this.selectedUserIndex >= 0 && this.selectedUserIndex < this.users.length) {
            currentSelectedUser = this.users[this.selectedUserIndex];
        }
        this.users = JSON.parse(JSON.stringify(this.chatState.currentRoom.users));
        this.users.splice(0, 0, "[ALL]");
        if (currentSelectedUser.length > 0) {
            let idx = this.users.indexOf(currentSelectedUser);
            if (idx >= 0) this.selectedUserIndex = idx;
            else this.selectedUserIndex = 0;
        } else this.selectedUserIndex = 0;
        this.selectedUser = this.users[this.selectedUserIndex];
    }

    exitRoom() {
        this.messenger.exitRoom(this.roomName);
    }

    logout() {
        this.messenger.logout();
        this.router.navigate("login"); // go to login
    }

    selectUser(idx: number) {
        this.selectedUserIndex = idx;
        this.selectedUser = this.users[this.selectedUserIndex];
    }

    sendChatMessage() {
        this.messenger.sendChatMessage(this.chatMessage, 
                                    this.selectedUserIndex <= 0 ? "" : this.selectedUser, 
                                    this.selectedChatFlags.indexOf("private") >= 0);
        this.chatMessage = "";
    }

    changeLang(lang: string) {
        this.i18n.setLocale(lang)
        .then(() => {
            this.chatState.currentLang = this.i18n.getLocale();
            console.log(this.chatState.currentLang);
        });
    }
}
