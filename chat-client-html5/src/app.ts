
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
		// router will take the website's root directory as cwd
		// but our app's root directory might be not the same as website's root directory
		let root = "";
		if (App.config.rootDir != undefined && App.config.rootDir != null
			&& App.config.rootDir.length > 0 && App.config.rootDir != ".") root = App.config.rootDir + "/";

		config.title = 'Chatroom';
		config.map([
			{ route: ['', 'login'],  moduleId: root + 'login', 	  name: 'login',    title: 'Login'},
			{ route: 'lobby', 		 moduleId: root + 'lobby', 	  name: 'lobby',    title: 'Lobby'},
			{ route: 'chatroom', 	 moduleId: root + 'chatroom', name: 'chatroom', title: 'Chatroom'}
		]);
		
		this.router = router;
	}
}
