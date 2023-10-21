export namespace server {
	
	export class Server {
	    id: number;
	    serverAlias: string;
	    serverName: string;
	    serverPassword: string;
	    adminPassword: string;
	    spectatorPassword: string;
	
	    static createFrom(source: any = {}) {
	        return new Server(source);
	    }
	
	    constructor(source: any = {}) {
	        if ('string' === typeof source) source = JSON.parse(source);
	        this.id = source["id"];
	        this.serverAlias = source["serverAlias"];
	        this.serverName = source["serverName"];
	        this.serverPassword = source["serverPassword"];
	        this.adminPassword = source["adminPassword"];
	        this.spectatorPassword = source["spectatorPassword"];
	    }
	}

}

