import {autoinject} from 'aurelia-framework';
import {DialogController} from 'aurelia-dialog';

@autoinject(DialogController)
export class CreateNewChatroomDialog {

    chatroomName: string = "";

    constructor(public controller: DialogController){
        //this.controller.settings.centerHorizontalOnly = true;
    }

    activate(defaultNewName = "") {
        this.chatroomName = defaultNewName;
    }
}
