// for now, we are using jquery (coz jquery has been included for bootstrap already) to send http requests...
// we might change to use "axios" or "whatwg-fetch(github/fetch)" or aurelia-fetch-client" if do not need jquery anymore

export class HttpClient {
    
    static getJSON(url: string, data?: any, callback?: (json: any)=>void, onerror?: (errmsg: string)=>void) {
        $.getJSON(url, data, (ret) => {
            if (callback != null) callback(ret);
        })
        .fail((jqxhr, textStatus, error) => {
            console.log(jqxhr);
            console.log(textStatus);
            console.log(error);
            if (onerror != null) onerror(textStatus);
        });
    }

    static postText(url: string, text: string, callback: (reply: any)=>void = null, onerror: (errmsg: string)=>void = null) {
        $.post(url, text, (response) => {
            if (callback != null) callback(response);
        }, 'text')
        .fail((jqxhr, textStatus, error) => {
            console.log(jqxhr);
            console.log(textStatus);
            console.log(error);
            if (onerror != null) onerror(textStatus);
        });
    }

    /*
    static get(url: string, config?: any, callback?: (data: any)=>void, onerror?: (err: any)=>void) {
        axios.get(url, config)
        .then(function (response) {
            if (callback) callback(response);
        })
        .catch(function (error) {
            console.log(error);
            if (onerror) onerror(error);
        });
    }
    
    static fetch(url: string, config?: any, callback?: (data: any)=>void, onerror?: (err: any)=>void) {
        fetch(url, config)
        .then(function(response) {
            if (callback) callback(response);
        }).catch(function(ex) {
            console.log(ex);
            if (onerror) onerror(ex);
        });
    }
    */
    
}
