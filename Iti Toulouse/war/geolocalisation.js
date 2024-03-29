var latitude;
var longitude;
var latitudeAmi;
var longitudeAmi;
var latitudeLieu;
var longitudeLieu;
var directionsDisplay;
var directionsService;
var map;

function showLocation(position) //fonction appel� par getcurrentposition permettant de r�cup�rer les infos de localisation si elle a reussi
		{
			
			latitude = position.coords.latitude;
			longitude = position.coords.longitude;
			alert(latitude);
			alert(longitude);
			start = new google.maps.LatLng(latitude, longitude);
			end = new google.maps.LatLng(latitudeLieu, longitudeLieu);
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
			 directionsService = new google.maps.DirectionsService(); // Service de calcul d'itin�raire
		     directionsService.route(request, function(response, status)
		    { // Envoie de la requ�te pour calculer le parcours
		            if(status == google.maps.DirectionsStatus.OK)
		            {
		            	directionsDisplay.setDirections(response); // Trace l'itin�raire sur la carte et les diff�rentes �tapes du parcours
		            }
		            if(status == google.maps.DirectionsStatus.OVER_QUERY_LIMIT)
		            {
		            	alert("trop de requete dans le temps imparti");
		            }
		            if(status == google.maps.DirectionsStatus.NOT_FOUND)
		            {
		            	alert("Coordonn�es Incorrect");
		            }
		            if(status == google.maps.DirectionsStatus.ZERO_RESULTS)
		            {
		            	alert("Aucuns r�sultats");
		            }		            
		            if(status == google.maps.DirectionsStatus.REQUEST_DENIED)
		            {
		            	alert("Requ�te refus�");
		            }
		            if(status == google.maps.DirectionsStatus.UNKNOWN_ERROR)
		            {
		            	alert("Erreur des serveur Google Maps");
		            }
		     });	
		};
		

		
		function errorHandler(error) // fonction appel� par getcurrentposition permettant de r�cup�r� le code erreur si jamais il n'y arrive pas
		{
			switch(error.code)
			{
				case error.PERMISSION_DENIED:
					alert("L'utilisateur n'a pas autoris� l'acc�s � sa position");
					break;      
				case error.POSITION_UNAVAILABLE:
					alert("L'emplacement de l'utilisateur n'a pas pu �tre d�termin�");
					break;
				case error.TIMEOUT:
					alert("Le service n'a pas r�pondu � temps");
					break;
			}
		};
		
		function stopWatch()
		{
			navigator.geolocation.clearWatch(watchId);
		};
		
		function initialize() 
		{
		  directionsDisplay = new google.maps.DirectionsRenderer();
	      panel    = document.getElementById('panel');
		  map = new google.maps.Map(document.getElementById("map_canvas"),//la map apparaitra dans le div "map canvas"
					{
						zoom : 19,
						center : new google.maps.LatLng(43.604619,1.444196),//centre la map sur le capitole a touliouse par d�fault
						mapTypeId : google.maps.MapTypeId.ROADMAP// je d�finie le type de carte sur ROADMAP soit une carte de base
						
					});
	      directionsService = new google.maps.DirectionsRenderer(
		  {
			  map   : map,
			  panel : panel // Dom element pour afficher les instructions d'itin�raire
			  });
	      directionsDisplay.setMap(map);
	      directionsDisplay.setPanel(document.getElementById('panel'));


		};
		
		function geolocalisation()
		{
		if (navigator.geolocation)//navigator.geolocation renvoie un simple bool�en valant vrai ou faux selon la capacit� du navigateur � utiliser la g�olocalisation
		{
			watchId = navigator.geolocation.watchPosition(showLocation,errorHandler,{enableHighAccuracy : true});//fonction permettant d'obtenir sa localisation, si c'est un succ� execute showlocation sinon errorHandler
		}
		else
			alert("Dommage... Votre navigateur ne prend pas en compte la g�olocalisation HTML5");
		};
		
		function SuivreAmi(i){
			latitudeAmi = parseFloat(myjson.donnees[i].lat);
			longitudeAmi = parseFloat(myjson.donnees[i].lng);
			latitudeLieu = parseFloat(myjson.donnees[i].latlieu);
			longitudeLieu = parseFloat(myjson.donnees[i].lnglieu);
			geolocalisation();
		}