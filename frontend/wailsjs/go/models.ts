export namespace server {
	
	export class Server {
	    id: number;
	    serverAlias: string;
	    serverName: string;
	    serverPassword: string;
	    adminPassword: string;
	    spectatorPassword: string;
	    ipAddress: string;
	    serverPort: number;
	    peerPort: number;
	    queryPort: number;
	    rconPort: number;
	
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
	        this.ipAddress = source["ipAddress"];
	        this.serverPort = source["serverPort"];
	        this.peerPort = source["peerPort"];
	        this.queryPort = source["queryPort"];
	        this.rconPort = source["rconPort"];
	    }
	}

}

