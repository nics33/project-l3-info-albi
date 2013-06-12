var socket; 
var messagerecu;
var listeAmiNick = [];
var listeAmiId = [];

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
						token = json.token;
						openChannel(token);
						myjson = json;
						admin();
						CreationListeAmi();
						AffichageListeAmi();
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
	$.ajax(
			{ //on log l'utilisateur
				type: "POST",
				url: "http://ititoulouse.appspot.com/Servlet_Disconnection",// a cette url
				dataType: "json",
				async: false,
				success: function(json)
				{
				}
			});
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
	var messageJSON = JSON.parse(message.data);
	typemessage = messageJSON.type;
	switch(typemessage)
	{
	case "UpdateFriendlist":
		alert("Je rajoute l'utilisateur "+messageJSON.nickname+ " a la liste d'ami");
		AjoutListeAmi(messageJSON.id,messageJSON.nickname);
		AffichageListeAmi()
		break;
	case "DeleteFriend":
		alert("Je supprime l'utilisateur "+messageJSON.id+ " de la liste d'ami");
		SuppressionListeAmi(messageJSON.id);
		AffichageListeAmi();
		break;	
	}
};

function CreationListeAmi(){
	var Taille = myjson.donnees.length;
	for (var i = 0; i<Taille; i++){
		var nickname = myjson.donnees[i].nickname;
		var id = myjson.donnees[i].id;
		AjoutListeAmi(id,nickname)
	}
}

function AjoutListeAmi(id,nickname){
	listeAmiNick.push(nickname);
	listeAmiId.push(id);
}

function SuppressionListeAmi(id){
	var i = listeAmiId.indexOf(id);
	listeAmiId.splice(i,1);
	listeAmiNick.splice(i,1);
}

function AffichageListeAmi() {
	 var Taille = listeAmiNick.length;
	 $('#listeami2').empty();
	 for (var i = 0; i<Taille; i++){
	 var nickname = listeAmiNick[i];
	 $('#listeami2').append("<li onClick='SuivreAmi("+i.toString()+")'>"+nickname+"</li>");
	 $("#listeami2").listview('refresh');
	 }
	}


