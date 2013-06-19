var socket; 
var messagerecu;
var listeAmiNick = [];
var listeAmiId = [];
var listeAmiAttente = [];
var listeAmiSuivi = [];
var notifami;

function GetToken(){
	$.ajax(
			{ //on log l'utilisateur
				type: "POST",
				url: "/Servlet_Connection",// a cette url
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
						var listeDemandeAmi = json.demandeami;
						NotificationDemandeAmi(listeDemandeAmi);
						}
				}
			});
};

function NotificationDemandeAmi(listedemandeami){
	if(listedemandeami.length != 0)
		{
		
			message = " Voulez-vous accepter " + listedemandeami[0].nickname+" en tant qu'ami ?"
			console.log(message);
			$("#textedemandeami").html(message);
			$("#boutonaccepterinvitation").click(function(){
				console.log("j'accepte linvitation");
				listedemandeami.splice(0,1);
				$("#popupdemandeami" ).on('popupafterclose', function() {
					setTimeout( function(){NotificationDemandeAmi(listedemandeami);}, 100 );
					$('#popupdemandeami').unbind('popupafterclose');
				});
				$("#popupdemandeami" ).popup("close");	
			});
			$("#boutonrefuserinvitation").click(function(){
				console.log("je refuse linvitation");
				listedemandeami.splice(0,1);
				$("#popupdemandeami" ).on('popupafterclose', function() {
					setTimeout( function(){NotificationDemandeAmi(listedemandeami);}, 100 );
					$('#popupdemandeami').unbind('popupafterclose');
				});
				$("#popupdemandeami" ).popup("close");	
			});
			$("#popupdemandeami" ).popup("open");	
			
		}
};

function openChannel(token) {
	console.log("J'ouvre le channel");
	var channel = new goog.appengine.Channel(token);
	console.log("le channel est ouvert");
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
				url: "/Servlet_Disconnection",// a cette url
				dataType: "json",
				async: false,
				success: function(json)
				{
				}
			});
}

onSocketError = function(error){
	console.log("Error is <br/>"+error.description+" <br /> and HTML code"+error.code);
};

onSocketOpen = function() {
	console.log("Socket Ouverte");
};

onSocketClose = function() {
	console.log("Socket Connection closed");
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
		
	case "FollowMe":
		nickami = listeAmiNick[listeAmiId.indexOf(messageJSON.from)];
		message = "Voulez vous Suivre " + nickami;
	    $( "#popupsuivi2" ).popup({
	    	afterclose: function()
	    	{
	            $('#boutonsuivi2').unbind('click');
	    	}
	    });
		$("#textesuivreami2").html(message);
	    $("#popupsuivi2" ).popup("open");
	    $("#boutonsuivi2").click(function(){
	    	console.log("jetesuis");
	    //on envoie la notification à l'ami concerné
	    //j'envoie le message au channel
	    $.ajax(
	    		{ //on log l'utilisateur
	    			type: "POST",
	    			url: "/Servlet_NotifJeTeSuis",// a cette url
					data:{ "to" : messageJSON.from }, 
	    			dataType: "text",
	    			success: function(json)
	    			{
	    			}
	    		});
	    });

		break;
		 
		
	case "IFollowYou":
		nickami = listeAmiNick[listeAmiId.indexOf(messageJSON.from)];

		listeAmiAttente.splice(listeAmiAttente.indexOf(messageJSON.from),1); //
		listeAmiSuivi.push(messageJSON.from);
		message =  nickami + "a accepté votre demande de suivi";
		$("#popupsuivi3" ).popup();
		$("#textesuivreami3").html(message);
	    $("#popupsuivi3" ).popup("open");
        $.ajax(
                { //on log l'utilisateur
                        type: "POST",
                        url: "/Servlet_Message",// a cette url
                        data:{"type" : "Send", "latlieu": latitudeLieu, "lnglieu" : longitudeLieu, "lat" : latitude, "lng" : longitude,"to" : messageJSON.from }, 
                        dataType: "text",
                        success: function(text)
                        {
                        }
                });
	    break;		

	case "SendCoord":
		latitudeAmi = messageJSON.lat;
		longitudeAmi = messageJSON.lng;
		latitudeLieuAmi = messageJSON.latlieu;
		longitudeLieuAmi = messageJSON.lnglieu;
		geolocalisation();
		break;

	case "UpdateCoord":
		latitudeAmi = messageJSON.lat;
		longitudeAmi = messageJSON.lng;
		latitudeLieuAmi = messageJSON.latlieu;
		longitudeLieuAmi = messageJSON.lnglieu;
		break;
		
	}
};

function CreationListeAmi(){
	var Taille = myjson.donnees.length;
	for (var i = 0; i<Taille; i++){
		var nickname = myjson.donnees[i].nickname;
		var id = myjson.donnees[i].id;
		console.log(id);
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

function SuivreAmi(i){
	nickami = listeAmiNick[i];
	var notifami = listeAmiId[i];
	message = "Voulez vous demander à " + nickami + " de vous suivre?";
    $( "#popupsuivi" ).popup({
    	afterclose: function()
    	{
            $('#boutonsuivi').unbind('click');
    	}
    });
	$("#textesuivreami").html(message);
    $( "#popupsuivi" ).popup("open");

    $("#boutonsuivi").click(function(){
    	//on envoie la notification à l'ami concerné
    	//j'envoie le message au channel
    	$.ajax(
    			{ //on log l'utilisateur
    				type: "POST",
    				url: "/Servlet_NotifSuisMoi",// a cette url
					data:{ "to" : notifami }, 
    				dataType: "text",
    				success: function(text)
    				{
    					console.log(notifami);
    					console.log("1");
    					listeAmiAttente.push(notifami);
    				}
    			});    	
    });

    //String lat = request.getParameter("lat");
   // String lng = request.getParameter("lng");
    //String latlieu = request.getParameter("latlieu");
   //String lnglieu = request.getParameter("lnglieu");
   // String friend = request.getParameter("to")

	//latitudeAmi = parseFloat(myjson.donnees[i].lat);
	//longitudeAmi = parseFloat(myjson.donnees[i].lng);
	//latitudeLieuAmi = parseFloat(myjson.donnees[i].latlieu);
	//longitudeLieuAmi = parseFloat(myjson.donnees[i].lnglieu);
	//geolocalisation();
}

function UpdateUsers(){
	for(i=0;i<listeAmiSuivi.length;i++)
		{
		friendid = listeAmiSuivi[i];
                $.ajax(
                                { //on log l'utilisateur
                                        type: "POST",
                                        url: "/Servlet_Message",// a cette url
                                        data:{"type" : "Update" , "latlieu": latitudeLieu, "lnglieu" : longitudeLieu, "lat" : latitude, "lng" : longitude,"to" : friendid }, 
                                        dataType: "text",
                                        success: function(text)
                                        {
                                        }
                                });
		}
};
               /* $.ajax(
                                { //on log l'utilisateur
                                        type: "GET",
                                        url: "http://ititoulouse.appspot.com/Servlet_Update",// a cette url
                                        data:{ "ville": UserCity,"type": monLieu , "latlieu": latitudeLieu.toString(), "lnglieu" : longitudeLieu.toString(), "lat" : latitude.toString(), "lng" : longitude.toString() }, 
                                        dataType: "json",
                                        success: function(json)
                                        {
                                                
                                                if(json.status =="10")
                                                        {
                                                        document.location.href = json.donnees;
                                                        }
                                                else
                                                        {
                                                        myjson = json;
                                                        admin();
                                                        TraitementListe();
                                                        }
                                        }
                                });*/


