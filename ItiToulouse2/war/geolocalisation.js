var UserCity = "";
var monLieu = "";
var latitude = 0.0;
var longitude = 0.0;
var latitudeAmi;
var longitudeAmi;
var latitudeLieu = 0.0;
var longitudeLieu = 0.0;
var latitudeLieuAmi;
var longitudeLieuAmi;
var directionsDisplay;
var directionsService;
var map;
var watchId;


function showLocation(position) //fonction appelé par getcurrentposition permettant de récupérer les infos de localisation si elle a reussi
		{
			
			latitude = position.coords.latitude;
			longitude = position.coords.longitude;
			start = new google.maps.LatLng(latitude, longitude);
			end = new google.maps.LatLng(latitudeLieuAmi, longitudeLieuAmi);
			var wayps = [{
	            location: new google.maps.LatLng(latitudeAmi, longitudeAmi),
	            stopover:true}];
			var request = {
				      origin: start,
				      destination: end,
				      waypoints: wayps,
				      optimizeWaypoints: true,
				      travelMode: google.maps.TravelMode.WALKING
			 };
			 directionsService = new google.maps.DirectionsService(); // Service de calcul d'itinéraire
		     directionsService.route(request, function(response, status)
		    { // Envoie de la requête pour calculer le parcours
		            if(status == google.maps.DirectionsStatus.OK)
		            {
		            	directionsDisplay.setDirections(response); // Trace l'itinéraire sur la carte et les différentes étapes du parcours
		            }
		            if(status == google.maps.DirectionsStatus.OVER_QUERY_LIMIT)
		            {
		            	alert("trop de requete dans le temps imparti");
		            }
		            if(status == google.maps.DirectionsStatus.NOT_FOUND)
		            {
		            	alert("Coordonnées Incorrect");
		            }
		            if(status == google.maps.DirectionsStatus.ZERO_RESULTS)
		            {
		            	alert("Aucuns résultats");
		            }		            
		            if(status == google.maps.DirectionsStatus.REQUEST_DENIED)
		            {
		            	alert("Requète refusé");
		            }
		            if(status == google.maps.DirectionsStatus.UNKNOWN_ERROR)
		            {
		            	alert("Erreur des serveur Google Maps");
		            }
		     });	
		};
		

	
function showLocationNonLieux(position) //fonction appelé par getcurrentposition permettant de récupérer les infos de localisation si elle a reussi
{
			
	latitude = position.coords.latitude;
	longitude = position.coords.longitude;
	start = new google.maps.LatLng(latitude, longitude);
	end = new google.maps.LatLng(latitudeAmi, longitudeAmi);

	var request = {
		      origin: start,
		      destination: end,
		      travelMode: google.maps.TravelMode.WALKING
	 };
	 directionsService = new google.maps.DirectionsService(); // Service de calcul d'itinéraire
     directionsService.route(request, function(response, status)
    { // Envoie de la requête pour calculer le parcours
            if(status == google.maps.DirectionsStatus.OK)
            {
            	directionsDisplay.setDirections(response); // Trace l'itinéraire sur la carte et les différentes étapes du parcours
            }
            if(status == google.maps.DirectionsStatus.OVER_QUERY_LIMIT)
            {
            	alert("trop de requete dans le temps imparti");
            }
            if(status == google.maps.DirectionsStatus.NOT_FOUND)
            {
            	alert("Coordonnées Incorrect");
            }
            if(status == google.maps.DirectionsStatus.ZERO_RESULTS)
            {
            	alert("Aucuns résultats");
            }		            
            if(status == google.maps.DirectionsStatus.REQUEST_DENIED)
            {
            	alert("Requète refusé");
            }
            if(status == google.maps.DirectionsStatus.UNKNOWN_ERROR)
            {
            	alert("Erreur des serveur Google Maps");
            }
     });	
};
		

		

		
		
		
		function errorHandler(error) // fonction appelé par getcurrentposition permettant de récupéré le code erreur si jamais il n'y arrive pas
		{
			switch(error.code)
			{
				case error.PERMISSION_DENIED:
					alert("L'utilisateur n'a pas autorisé l'accès à sa position");
					break;      
				case error.POSITION_UNAVAILABLE:
					alert("L'emplacement de l'utilisateur n'a pas pu être déterminé");
					break;
				case error.TIMEOUT:
					alert("Le service n'a pas répondu à temps");
					break;
			}
		};
		
		function stopWatch()
		{
			navigator.geolocation.clearWatch(watchId);
		};
		
		function initialize() 
		{
		  //mise en place de la map avec Jquery
		$("#content").empty();
		$("#content").append("<div id='map_canvas' class='ui-bar-c ui-corner-all ui-shadow' style='padding:1em;width:45%;float: left;'></div><div id='panel' style='width:45%;float: right;'></div>");	
		var taillebody=$("body").height();
		$("#map_canvas").height(taillebody*0.8);
		$("#panel").height(taillebody*0.8);
		$("#panel").css({"overflow-y": "scroll", "-webkit-overflow-scrolling": "touch"});
			
		  directionsDisplay = new google.maps.DirectionsRenderer();
	      panel    = document.getElementById('panel');
		  map = new google.maps.Map(document.getElementById("map_canvas"),//la map apparaitra dans le div "map canvas"
					{
						zoom : 19,
						center : new google.maps.LatLng(43.604619,1.444196),//centre la map sur le capitole a touliouse par défault
						mapTypeId : google.maps.MapTypeId.ROADMAP// je définie le type de carte sur ROADMAP soit une carte de base
						
					});
	      directionsService = new google.maps.DirectionsRenderer(
		  {
			  map   : map,
			  panel : panel // Dom element pour afficher les instructions d'itinéraire
			  });
	      directionsDisplay.setMap(map);
	      directionsDisplay.setPanel(document.getElementById('panel'));


		};
		
		function geolocalisation()
		{
		if (navigator.geolocation)//navigator.geolocation renvoie un simple booléen valant vrai ou faux selon la capacité du navigateur à utiliser la géolocalisation
		{
			
			if(latitudeLieuAmi == 0 && longitudeLieuAmi == 0){
				initialize();
				watchId = navigator.geolocation.watchPosition(showLocationNonLieux,errorHandler,{enableHighAccuracy : true, maximumAge : 5000});//fonction permettant d'obtenir sa localisation, si c'est un succé execute showlocation sinon errorHandler

			}
			else 
			{
				initialize();
				watchId = navigator.geolocation.watchPosition(showLocation,errorHandler,{enableHighAccuracy : true, maximumAge : 5000});//fonction permettant d'obtenir sa localisation, si c'est un succé execute showlocation sinon errorHandler
			}	
	}
		else
			alert("Dommage... Votre navigateur ne prend pas en compte la géolocalisation HTML5");
		};
		
		function SuivreAmi(i){
			latitudeAmi = parseFloat(myjson.donnees[i].lat);
			longitudeAmi = parseFloat(myjson.donnees[i].lng);
			latitudeLieuAmi = parseFloat(myjson.donnees[i].latlieu);
			longitudeLieuAmi = parseFloat(myjson.donnees[i].lnglieu);
			geolocalisation();
		}