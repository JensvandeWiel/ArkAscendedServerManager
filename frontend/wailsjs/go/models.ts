export namespace server {
	
	export class Server {
	    id: number;
	    test: string;
	
	    static createFrom(source: any = {}) {
	        return new Server(source);
	    }
	
	    constructor(source: any = {}) {
	        if ('string' === typeof source) source = JSON.parse(source);
	        this.id = source["id"];
	        this.test = source["test"];
	    }
	}

}

