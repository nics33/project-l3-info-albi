var myjson;

function UpdateUser(){	
	$.ajax(
	{ //on log l'utilisateur
		type: "GET",
		url: "http://ititoulouse.appspot.com/Servlet_Update",// a cette url
		data:{ "ville": "toulouse","type": "sanisettes", "latlieu": "43.606552124", "lnglieu" : "1.45222043991", "lat" : "43.5923843384", "lng" :"1.45126569271" }, 
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
				TraitementListe();
				}
		}
	});
};

function TraitementListe() {
	 var Taille = myjson.donnees.length;
	 $('#listeami2').empty();
	 for (var i = 0; i<Taille; i++){
	 var mail= myjson.donnees[i].email;
	 $('#listeami2').append("<li onClick='SuivreAmi("+i.toString()+")'>"+mail+"</li>");
	 $("#listeami2").listview('refresh');
	 }
	}

function AffichageListeType(){
	if (navigator.geolocation)//navigator.geolocation renvoie un simple booléen valant vrai ou faux selon la capacité du navigateur à utiliser la géolocalisation
	{
		watchId = navigator.geolocation.getCurrentPosition(
				function(position) //fonction appelé par getcurrentposition permettant de récupérer les infos de localisation si elle a reussi
				{
					geocoder = new google.maps.Geocoder();
					var latitude = position.coords.latitude;
					var longitude = position.coords.longitude;
					var latlng = new google.maps.LatLng(latitude, longitude);

					geocoder.geocode({'latLng': latlng}, function(results, status) 
					{
						if (status == google.maps.GeocoderStatus.OK) 
						{
							if (results[1])
							{
							UserCity = results[1].address_components[1].long_name.toString();
							alert(results[1].address_components[1].long_name);
							donnees = '{ "ville":' + UserCity + ', "type":'+ "a"+ ', "liste":'+'['+']' +'}';
							$.ajax(
							{ //on importe le fichier
								type: "POST",
								url: "http://ititoulouse.appspot.com/Servlet_GetTypes",// a cette url
								data:{ donnees: donnees},
								dataType: "json",
								success: function(data)
								{
									alert("sa marche");
								}
							})
							} else 
							{
								alert('No results found');
							}
						} else 
						{
							alert('Geocoder failed due to: ' + status);
						}
					});
				}
				
		,errorHandler,{enableHighAccuracy : true});//fonction permettant d'obtenir sa localisation, si c'est un succé execute showlocation sinon errorHandler
	}
	else
		alert("Dommage... Votre navigateur ne prend pas en compte la géolocalisation HTML5");
	};