
import {autoinject} from 'aurelia-framework';
import {EventAggregator, Subscription} from 'aurelia-event-aggregator';
import {Router} from 'aurelia-router';

import {App} from './app';
import {ChatState} from './chat-state';
import {HttpClient} from './http-client';
import {Messenger} from './messenger';
import * as Messages from './messages';
import * as UI from './ui-messages';

@autoinject()
export class LoginPage {

    userName: string = "test";
    userPassword: string = "";
    loginUrl: string = "127.0.0.1:11091";
    alertMessage: string = null;

    subscribers: Array<Subscription> = [];

    constructor(public router: Router, public chatState: ChatState, 
                public messenger: Messenger, public eventChannel: EventAggregator) {
                    
        this.userName = App.config.defaultUserName;
        this.loginUrl = App.config.defaultLoginUrl;

        this.subscribers = [];

    }

    get canLogin() {
        return this.userName.length > 0 
                && this.userPassword.length > 0
                && this.loginUrl.length > 0
                && !this.messenger.isRequesting
                && !this.router.isNavigating;
    }

    get isEmptyAlertMessage() {
        return this.alertMessage == null || this.alertMessage.length <= 0;
    }

    activate(parameters, routeConfig) {
        console.log("done");
        this.chatState.currentPage = "login";

        this.chatState.currentRoom.name = "";
        this.chatState.currentRoom.users = [];
        this.chatState.rooms = [];

        document.getElementById('loading').style.display = 'none';
        document.getElementById('app').style.display = 'block';
    }

    attached() {
        this.subscribers = [];
        this.subscribers.push(this.eventChannel.subscribe(UI.EnterLobby, data => {
            if (data.message.toLowerCase() == "ok") {
                console.log("jump to lobby...");
                this.router.navigate("lobby"); // go to lobby
            } else {
                this.alertMessage = data.message;
            }
        }));
    }

    detached() {
        for (let item of this.subscribers) item.dispose();
        this.subscribers = [];
    }

    dismissAlertMessage() {
        this.alertMessage = null;
    }

    login() {
        
        console.log("start to login");

        let reqmsg = new Messages.LoginRequest();
        reqmsg.user_name = this.userName;
        
        HttpClient.postText(this.loginUrl + "/login/login", JSON.stringify(reqmsg), (replyText) => {
            let reply = JSON.parse(replyText);
            if (reply.result.toLowerCase() == "ok") {
                this.chatState.userName = this.userName;
                this.chatState.userToken = reply.user_token;
                this.chatState.serverAddress = reply.server_uri;
                this.messenger.enterLobby();
            }
        }, (errmsg) => {
            this.alertMessage = "Failed to login: " + errmsg;
        })
        
    }

}
