var socket; 

function GetToken(){
	$.ajax(
			{ //on log l'utilisateur
				type: "POST",
				url: "http://ititoulouse.appspot.com/Servlet_Connection",// a cette url
				dataType: "json",
				success: function(json)
				{
					
					if(json.status =="10")
						{
						document.location.href = json.donnees;
						}
					else
						{
						alert("trololo");
						token = json.token;
						alert(token);
						openChannel(token);
						}
				}
			});
};

function openChannel(token) {
	alert("J'ouvre le channel");
	var channel = new goog.appengine.Channel(token);
	alert("le channel est ouvert");
	socket = channel.open();
	socket.onopen = onSocketOpen;
	socket.onmessage = onSocketMessage;
	socket.onerror = onSocketError;
	socket.onclose = onSocketClose;
};

function closeChannel(){
	socket.close()
}

onSocketError = function(error){
	alert("Error is <br/>"+error.description+" <br /> and HTML code"+error.code);
};

onSocketOpen = function() {
	alert("Socket Ouverte");
};

onSocketClose = function() {
	alert("Socket Connection closed");
};

onSocketMessage = function(message) {
	alert("Message Reçu");
};

