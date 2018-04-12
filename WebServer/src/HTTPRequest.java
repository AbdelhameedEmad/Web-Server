

public class HTTPRequest {
	
	String Method;
	String URL;			//The path of the file
	String Version;		//The version of the HTTP request
	String Host;		//The Name of the Website
	String AcceptedFormat;		//The format of the file
	String Connection;		//The State of the connection between the server and the Client
	String ip;			//The IP address of the client
	
	public HTTPRequest(){
		this.Method = "";
		this.URL = "";
		this.Version = "";
		this.Host = "";
		this.AcceptedFormat = "";
		this.Connection = "";
		this.ip = "";
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIp() {
		return ip;
	}
	public String getMethod() {
		return Method;
	}
	public void setMethod(String method) {
		Method = method;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
	public String getHost() {
		return Host;
	}
	public void setHost(String host) {
		Host = host;
	}
	public String getAcceptedFormat() {
		return AcceptedFormat;
	}
	public void setAcceptedFormat(String acceptedFormat) {
		AcceptedFormat = acceptedFormat;
	}
	public String getConnection() {
		return Connection;
	}
	public void setConnection(String connection) {
		Connection = connection;
	}
}
