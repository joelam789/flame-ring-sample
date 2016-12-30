    
import * as nprogress from 'nprogress';
import {bindable, noView} from 'aurelia-framework';

//@noView(['nprogress/nprogress.css'])
//@noView() // we would load nprogress.css from global res folder
export class LoadingIndicator {
    @bindable loading = false;
    loadingChanged(newValue) {
        if (newValue) {
            nprogress.start();
        } else {
            nprogress.done();
        }
    }
}
