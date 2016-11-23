// for now, we are using jquery (coz jquery has been included for bootstrap already) to send http requests...
// we might change to use "axios" or "whatwg-fetch(github/fetch)" or aurelia-fetch-client" if do not need jquery anymore

export class HttpClient {
    
    static getJSON(url: string, callback: (json: any)=>void = null, onerror: ()=>void = null) {
        $.getJSON(url, (data) => {
            if (callback != null) callback(data);
        })
        .fail(() => {
            if (onerror != null) onerror();
        });
    }

    static postText(url: string, text: string, callback: (reply: any)=>void = null, onerror: ()=>void = null) {
        $.post(url, text, (response) => {
            if (callback != null) callback(response);
        }, 'text')
        .fail(function (jqxhr, textStatus, error) {
            if (onerror != null) onerror();
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
