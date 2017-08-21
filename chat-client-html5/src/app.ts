
import {Router, RouterConfiguration} from 'aurelia-router';
import {autoinject} from 'aurelia-framework';

import {Messenger} from './messenger';

@autoinject()
export class App {

	router: Router;

	private static _config: any = null;

	constructor(public messenger: Messenger) {
		App._config = (<any>window).mainAppConfig;
	}

	static get config(): any {
		return App._config;
	}

	configureRouter(config: RouterConfiguration, router: Router) {
		config.title = 'Chatroom';
		config.map([
			{ route: ['', 'login'],  moduleId: 'login', 	name: 'login',    title: 'Login'},
			{ route: 'lobby', 		 moduleId: 'lobby', 	name: 'lobby',    title: 'Lobby'},
			{ route: 'chatroom', 	 moduleId: 'chatroom',  name: 'chatroom', title: 'Chatroom'}
		]);
		
		this.router = router;
	}
}
