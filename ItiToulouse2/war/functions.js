var myjson;
var jsonType;
var test;
var test2;
var listeVille = [];
var adresseLieu = "";

function admin()
{
        estAdmin=myjson.admin;
        if(estAdmin==1)
                {
                        $(".admin").show();
                }
}

function back()
{
        navigator.geolocation.clearWatch(watchId);
         latitudeAmi = 0.0;
         longitudeAmi = 0.0;
         latitudeLieu = 0.0;
         longitudeLieu = 0.0;
         latitudeLieuAmi = 0.0;
         longitudeLieuAmi = 0.0;
        var Nb_boutons=jsonType.donnees.length;
        $("#content").empty();
        $("#content").append("<ul data-role='listview' data-inset='true' data-theme='c' id='listeBouton'></ul>");
        $("#content").trigger("create");
        for(i=0;i<Nb_boutons;i++)
        {
        $("#listeBouton").append("<li onClick='ChoisirLieuPlusProche(\"" + jsonType.donnees[i]+"\")'>"+jsonType.donnees[i]+"</li>");
        $("#listeBouton").listview('refresh');
        }
        
}

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
        if (navigator.geolocation)//navigator.geolocation renvoie un simple bool�en valant vrai ou faux selon la capacit� du navigateur � utiliser la g�olocalisation
        {
                watchId = navigator.geolocation.getCurrentPosition(
                                function(position) //fonction appel� par getcurrentposition permettant de r�cup�rer les infos de localisation si elle a reussi
                                {
                                        geocoder = new google.maps.Geocoder();
                                        latitude = position.coords.latitude;
                                        longitude = position.coords.longitude;
                                        var latlng = new google.maps.LatLng(latitude, longitude);

                                        geocoder.geocode({'latLng': latlng}, function(results, status) 
                                        {
                                                if (status == google.maps.GeocoderStatus.OK) 
                                                {
                                                        if (results[1])
                                                        {
                                                        UserCity = results[1].address_components[1].long_name.toString();
                                                        //alert(results[1].address_components[1].long_name);
                                                        donnees = '{ "ville":' + UserCity + ', "type":'+ "a"+ ', "liste":'+'['+']' +'}';
                                                        $.ajax(
                                                        { //on importe le fichier
                                                                type: "POST",
                                                                url: "/Servlet_GetTypes",// a cette url
                                                                data:{ donnees: donnees},
                                                                dataType: "json",
                                                                success: function(data)
                                                                {
                                                                        jsonType = data;
                                                                        var Nb_boutons=jsonType.donnees.length;
                                                                        $("#content").empty();
                                                                        $("#content").append("<ul data-role='listview' data-inset='true' data-theme='c' id='listeBouton'></ul>");
                                                                        $("#content").trigger("create");
                                                                        for(i=0;i<Nb_boutons;i++)
                                                                        {
                                                                        $("#listeBouton").append("<li onClick='ChoisirLieuPlusProche(\"" + jsonType.donnees[i]+"\")'>"+jsonType.donnees[i]+"</li>");
                                                                        $("#listeBouton").listview('refresh');
                        
                                                                        }
                                                                        
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
                                
                ,errorHandler,{enableHighAccuracy : true});//fonction permettant d'obtenir sa localisation, si c'est un succ� execute showlocation sinon errorHandler
        }
        else
                alert("Dommage... Votre navigateur ne prend pas en compte la g�olocalisation HTML5");
        };
        
        
        function ChoisirLieuPlusProche(TypeLieu)
        {
                monLieu = TypeLieu;
                donnees = '{ "ville":' + UserCity + ', "type":'+ TypeLieu + ', "liste":'+'['+']' +'}';
                $.ajax(
                { //on importe le fichier
                        type: "POST",
                        url: "/Servlet_GetEntities",// a cette url
                        data:{ donnees: donnees},
                        dataType: "json",
                        success: function(data)
                        {
                                calcul_distance_plusieurs_points(data);
                        }
                })
        }
        
        function calcul_distance_plusieurs_points(data)//passage en parametre du tab de la fonction pr�c�dente et de la location de l'utilisateur
        {
                test2=data;
                var myLocation= new google.maps.LatLng(latitude, longitude);
                //alert("ma latitude :" + latitude + ", ma longitude :" + longitude);
                var i = 0;
                var tabmin = [];
                var minPoint ={
                "num" : 0,
                "dist" : 0,
                "dur" : 0,
                "adr" : "",
                };//tableau comprenant l'id du point le plus proche,la dur�e pr y acc�d� et sa distance
                var nbiterance = 0; // va permettre de connaitre le nobre de fois que l'on allons devoir utiliser le service google MATRIX vu qu'il est limit� a 25 destinations
                if((data.donnees.length/25) != Math.round(data.donnees.length/25)) nbiterance = Math.floor(data.donnees.length/25)+1; else nbiterance = Math.floor(data.donnees.length/25);
                //alert(nbiterance);
                //alert(data.donnees.length);
                for (var h =1;h<=nbiterance;h++)
                {
                        var tabdestinations = []//tableau d'objets google.maps.LatLng contenant les coordonn�es de tous les points du fichier kml trait� pr�c�d�ment
                        for (; i < 25*h && i<data.donnees.length; i++)//boucle for pour remplir le tableau mis a 50 pr test normalement a i < tabKML.length
                        {
                                //alert("i = " + i);
                                var lat = data.donnees[i].lat;
                                var lng = data.donnees[i].lng;
                                var destination = new google.maps.LatLng(lat,lng);
                                tabdestinations.push(destination);
                        }
                        //alert(tabdestinations);
                        var service = new google.maps.DistanceMatrixService();//d�marrage du service googleDistanceMatrix
                        service.getDistanceMatrix(
                        {
                                origins:[myLocation],//point de d�part soit la localisation de l'utilisateur
                                destinations: tabdestinations,// les destinations
                                travelMode: google.maps.TravelMode.WALKING//mode de d�placement dans notre cas a pied
                        }, 
                        function(response,status)
                        {
                                test = response;
                                if (status == google.maps.DistanceMatrixStatus.OK) 
                                {
                                        var destinations = response.destinationAddresses;
                                        var results = response.rows[0].elements;
                                        //alert(results);
                                        
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
                                                        minPoint.adr = response.destinationAddresses[j];
                                                }
                                        }
                                        //alert(minPoint.num + " , "  + minPoint.dur+ " , "+ minPoint.dist);
                                        tabmin.push({
                                                        "num" : minPoint.num,
                                                        "dist" : minPoint.dist,
                                                        "dur" : minPoint.dur,
                                                        "adr" : minPoint.adr
                                                        });
                                        if(tabmin.length == nbiterance)
                                        {
                                               // console.log("Point :" + tabmin[0].num + " , "+tabmin[0].dur+" , " + tabmin[0].dist);
                                                //console.log("Point :" + tabmin[1].num + " , "+tabmin[1].dur+" , " + tabmin[1].dist);
                                                //console.log("Point :" + tabmin[2].num + " , "+tabmin[2].dur+" , " + tabmin[2].dist);
                                                minPoint.dist=0;
                                                minPoint.dur=0;
                                                minPoint.num=0;
                                                minPoint.adr ="";
                                                for(var k = 0; k<tabmin.length;k++)
                                                {
                                                		console.log("Point :" + tabmin[k].num + ","+tabmin[k].dist);
                                                        //alert("taille tab min : "+tabmin.length);
                                                        //alert("Point :" + tabmin[0].num + ","+tabmin[0].dur);
                                                        //alert("Point :" + tabmin[1].num + ","+tabmin[1].dur);
                                                        //alert("Point :" + tabmin[2].num + ","+tabmin[2].dur);
                                                        if(tabmin[k].dist<minPoint.dist || k ==0)
                                                        {
                                                                
                                                                minPoint.dist=tabmin[k].dist;
                                                                minPoint.dur=tabmin[k].dur;
                                                                minPoint.adr=tabmin[k].adr;
                                                               // minPoint.num=tabmin[k].num+(25*k);
                                                                console.log("Pointif :" + minPoint.num + ","+minPoint.dist + ', k = ' + k);
                                                        }
                                                }
                                                //METTRE LA SUITE DU CODE ICI DU AU FAIT QUE L'API SOIT ASYNCHRONE
                                                //alert("Point mini :" + minPoint.num + ","+minPoint.dur);
                                                //mise en place du markeur
                                                //var idDestination = minPoint.num;
                                                latitudeLieu = 0.0;
                                                longitudeLieu = 0.0;      
                                                adresseLieu = minPoint.adr;
                                                //alert(idDestination + "," + latitudeLieu+ "," + longitudeLieu);
                                                if (navigator.geolocation)//navigator.geolocation renvoie un simple bool�en valant vrai ou faux selon la capacit� du navigateur � utiliser la g�olocalisation
                                                {
                                                        initialize();
                                                        //AFFICHER LES MARKEURS ICI
                                                        displayMarker(data);
                                                        watchId = navigator.geolocation.watchPosition(showLocation2,errorHandler,{enableHighAccuracy : true, maximumAge : 5000});//fonction permettant d'obtenir sa localisation, si c'est un succ� execute showlocation sinon errorHandler
                                                }
                                                else
                                                        alert("Dommage... Votre navigateur ne prend pas en compte la g�olocalisation HTML5");
                                                //map.panTo(positionDestination);
                                                //map.panTo(positionDestination); positionDestination ne fonctionne pas pour aucune raison aparente alors que les mm coordonn�es utilis� dans la fonctyion ready fonctionne.
                                                //affichageChemin(location_temp,positionDestination);

                                        }        
                                }
                                else alert(status);
                        });
                }
        }
        
         function createMarker(latlng, id)
         {
            var marker= new google.maps.Marker({
                  position: latlng, map: map,
                  });
            
              google.maps.event.addListener(marker, "click", function() {
                    latitudeLieu = latlng.lat();
                    longitudeLieu = latlng.lng();
                    
                    navigator.geolocation.clearWatch(watchId);
                                watchId = navigator.geolocation.watchPosition(showLocation2,errorHandler,{enableHighAccuracy : true, maximumAge : 5000});//fonction permettant d'obtenir sa localisation, si c'est un succ� execute showlocation sinon errorHandler

                    
                    
                    
                    
                  });
           
                  
            return marker;
         }
        
        function displayMarker(data){
                
         var markers = [];
         for( i = 0; i < data.donnees.length; i++ ) {

          markers [i] = createMarker(new google.maps.LatLng(data.donnees[i].lat, data.donnees[i].lng),
                                        i);         
         }
}      
        
        function showLocation2(position) //fonction appel� par getcurrentposition permettant de r�cup�rer les infos de localisation si elle a reussi
        {
                if(latitudeLieu !=0.0 && longitudeLieu != 0.0)
                	{
                    end = new google.maps.LatLng(latitudeLieu, longitudeLieu);
                	}
                else end = adresseLieu;
                console.log(end);
                latitude = position.coords.latitude;
                longitude = position.coords.longitude;
                start = new google.maps.LatLng(latitude, longitude);
                var request = {
                              origin: start,
                              destination: end,
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
        
        function affichageListeTypes(ville){
            
            
            //alert(results[1].address_components[1].long_name);
            donnees = '{ "ville":' + ville + ', "type":'+ "a"+ ', "liste":'+'['+']' +'}';
            $.ajax(
            { //on importe le fichier
                    type: "POST",
                    url: "/Servlet_GetTypes",// a cette url
                    data:{ donnees: donnees},
                    dataType: "json",
                    success: function(data)
                    {
                    		
                            jsonType = data;
                            var Nb_boutons=jsonType.donnees.length;
                            $("#listeTypes").empty();
                            $('#listeTypes').append(' <legend> Choisissez un paquet :</legend>');
                      
                            
                            for(i=0;i<Nb_boutons;i++)
                            {
                                    var type = data.donnees[i]
                                    
                                    $('#listeTypes').append(' <input type="radio" name="choixRadioT" onClick=SuppressionLieu('+ville+','+type+') id="choixRadioT'+i+'" value="'+type+'"  /> ');
                                $('#listeTypes').append('<label for="choixRadioT'+i+'">'+type+'</label>');
                                     
                                     
                                $("#listeTypes").listview('refresh');

                            }
                            
                    }
            })
               
               
               
               
       }

       
       //a tester
       function affichageListeVille() {
               
               
    
            $.ajax(
            { //on importe le fichier
                    type: "GET",
                    url: "/Servlet_GetVille",// a cette url
                    data:{ donnees: donnees},
                    dataType: "json",
                    success: function(data)
                    {    					
       					$("#popupMenu" ).popup({
    						popupafterclose: function() {
    				            setTimeout( function(){ $( '#popuplistevilles' ).popup( 'open' ) }, 500 );
    				        }});
    				   	$("#popupMenu" ).popup("close");
                           $('#listeVilles').empty();
                           
                           $('#listeVilles').append(' <legend><h3> Choisissez une ville :<h3></legend>');
                           
                           for (var i = 0; i<data.donnees.length; i++){
                           var nomVille = data.donnees[i];
                           $('#listeVilles').append(' <input type="radio" name="choixRadio" onClick="affichageListeType('+nomVille+')" id="choixRadio'+i+'" value="'+nomVille+'"  > ');
                           $('#listeVilles').append('<label for="choixRadio'+i+'">'+nomVille+'</label>');
                                
                                
                           $("#listeVilles").checkboxradio('refresh');
                           console.log(nomVille);
                           }

                    }
            })
            
       }
