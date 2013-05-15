var tabxml = [];
		var positionDestination = {};
		var direction;
		var panel;
		
		
		function affichageChemin(PointDepart,Destination)
		{
		    var request = {
            origin      : PointDepart,
            destination : Destination,
            travelMode  : google.maps.DirectionsTravelMode.WALKING // Mode de conduite
			}
        var directionsService = new google.maps.DirectionsService(); // Service de calcul d'itinéraire
        directionsService.route(request, function(response, status){ // Envoie de la requête pour calculer le parcours
            if(status == google.maps.DirectionsStatus.OK){
                direction.setDirections(response); // Trace l'itinéraire sur la carte et les différentes étapes du parcours
            }
        });	
		}
		
		function calcul_distance_plusieurs_points(map,tabKML,myLocation)//passage en parametre du tab de la fonction précédente et de la location de l'utilisateur
		{
			var i = 0;
			var tabmin = [];
			var minPoint ={
			"num" : 0,
			"dist" : 0,
			"dur" : 0,
			};//tableau comprenant l'id du point le plus proche,la durée pr y accédé et sa distance
			var nbiterance = 0; // va permettre de connaitre le nobre de fois que l'on allons devoir utiliser le service google MATRIX vu qu'il est limité a 25 destinations
			if((tabKML.length/25) != Math.round(tabKML.length/25)) nbiterance = Math.floor(tabKML.length/25)+1; else nbiterance = Math.floor(tabKML.length/25);
			//alert(nbiterance);
			//alert(tabKML.length);
			for (var h =1;h<=nbiterance;h++)
			{
				var tabdestinations = []//tableau d'objets google.maps.LatLng contenant les coordonnées de tous les points du fichier kml traité précédément
				for (; i < 25*h && i<tabKML.length; i++)//boucle for pour remplir le tableau mis a 50 pr test normalement a i < tabKML.length
				{
					//alert("i = " + i);
					var lat = tabKML[i].lat
					var lng = tabKML[i].lng
					var destination = new google.maps.LatLng(lat,lng);
					tabdestinations.push(destination);			
				}
				var service = new google.maps.DistanceMatrixService();//démarrage du service googleDistanceMatrix
				service.getDistanceMatrix(
				{
					origins:[myLocation],//point de départ soit la localisation de l'utilisateur
					destinations: tabdestinations,// les destinations
					travelMode: google.maps.TravelMode.WALKING//mode de déplacement dans notre cas a pied
				}, 
				function(response,status)
				{
					if (status == google.maps.DistanceMatrixStatus.OK) 
					{
						var destinations = response.destinationAddresses;
						var results = response.rows[0].elements;
						for (var j = 0; j < results.length; j++) 
						{
							var element = results[j];
							var distance = element.distance.value;
							var duration = element.duration.value;
							if (duration<minPoint.dur || j==0)
							{
								minPoint.dist=distance;
								minPoint.dur=duration;
								minPoint.num=j;
							}
						}
						//alert(minPoint.num + " , "  + minPoint.dur+ " , "+ minPoint.dist);
						tabmin.push({
								"num" : minPoint.num,
								"dist" : minPoint.dist,
								"dur" : minPoint.dur
								});
						if(tabmin.length == nbiterance)
						{
							//alert("Point :" + tabmin[0].num + " , "+tabmin[0].dur+" , " + tabmin[0].dist);
							//alert("Point :" + tabmin[1].num + " , "+tabmin[1].dur+" , " + tabmin[1].dist);
							//alert("Point :" + tabmin[2].num + " , "+tabmin[2].dur+" , " + tabmin[2].dist);
							minPoint.dist=0;
							minPoint.dur=0;
							minPoint.num=0;
							for(var k = 0; k<tabmin.length;k++)
							{
								//alert("taille tab min : "+tabmin.length);
								//alert("Point :" + tabmin[0].num + ","+tabmin[0].dur);
								//alert("Point :" + tabmin[1].num + ","+tabmin[1].dur);
								//alert("Point :" + tabmin[2].num + ","+tabmin[2].dur);
								if(tabmin[k].dur<minPoint.dur || k ==0)
								{
									alert("Point :" + tabmin[k].num + ","+tabmin[k].dur);
									minPoint.dist=tabmin[k].dist;
									minPoint.dur=tabmin[k].dur;
									minPoint.num=tabmin[k].num+(25*k);
								}
							}
							//METTRE LA SUITE DU CODE ICI DU AU FAIT QUE L'API SOIT ASYNCHRONE
							alert("Point mini :" + minPoint.num + ","+minPoint.dur);
							//mise en place du markeur
							var idDestination = minPoint.num;
							var latDestination = tabKML[idDestination].lat;
							var lngDestination = tabKML[idDestination].lng;	
							alert(idDestination + "," + latDestination+ "," + lngDestination);
							positionDestination = new google.maps.LatLng(latDestination, lngDestination);
							map.panTo(positionDestination);
							//map.panTo(positionDestination); positionDestination ne fonctionne pas pour aucune raison aparente alors que les mm coordonnées utilisé dans la fonctyion ready fonctionne.
							affichageChemin(location_temp,positionDestination);

						}	
					}
					else alert(status);
				});
			}
		}
		//$(document).ready(  //on attend que la page soit chargé pour lancer le script (document symbolyqe la page entiere et la fonction .ready attend donc qu'elle soit chargé
		//	function()
		//	{
		//		calcul_distance_plusieurs_points(map,tabxml,location_temp);
		//		
		//		
		//	});
