var latitude;
var longitude;
var latitudeAmi;
var longitudeAmi;
var latitudeLieu;
var longitudeLieu;
var directionsDisplay;
var directionsService;
var map;

function showLocation(position) //fonction appelé par getcurrentposition permettant de récupérer les infos de localisation si elle a reussi
		{
			
			latitude = position.coords.latitude;
			longitude = position.coords.longitude;
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
			watchId = navigator.geolocation.watchPosition(showLocation,errorHandler,{enableHighAccuracy : true});//fonction permettant d'obtenir sa localisation, si c'est un succé execute showlocation sinon errorHandler
		}
		else
			alert("Dommage... Votre navigateur ne prend pas en compte la géolocalisation HTML5");
		};
		
		function SuivreAmi(i){
			latitudeAmi = parseFloat(myjson.donnees[i].lat);
			longitudeAmi = parseFloat(myjson.donnees[i].lng);
			latitudeLieu = parseFloat(myjson.donnees[i].latlieu);
			longitudeLieu = parseFloat(myjson.donnees[i].lnglieu);
			geolocalisation();
		}