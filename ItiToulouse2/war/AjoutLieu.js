 var reader; //GLOBAL File Reader object for demo purpose only
	var output = "";
	var tab = [];
    /**
     * Check for the various File API support.
     */
	 function joinArray(a)
		{
			var b;
			b = "[" + a.join("],[") + "]";
			return(b);
		}
		
    function checkFileAPI() {
        if (window.File && window.FileReader && window.FileList && window.Blob) {
            reader = new FileReader();
            return true; 
        } else {
            alert('The File APIs are not fully supported by your browser. Fallback required.');
            return false;
        }
    }

    /**
     * read text input
     */
    function readText() {
    	filePath = document.getElementById('fileLieu');
         //placeholder for text output
        if(filePath.files && filePath.files[0]) {           
            reader.onload = function (e) {
                output = e.target.result;
                displayContents(output);
            };//end onload()
            reader.readAsText(filePath.files[0]);
        }//end if html5 filelist support
        else if(ActiveXObject && filePath) { //fallback to IE 6-8 support via ActiveX
            try {
                reader = new ActiveXObject("Scripting.FileSystemObject");
                var file = reader.OpenTextFile(filePath, 1); //ActiveX File Object
                output = file.ReadAll(); //text contents of file
                file.Close(); //close file "input stream"
                displayContents(output);
            } catch (e) {
                if (e.number == -2146827859) {
                    alert('Unable to access local files due to browser security settings. ' + 
                     'To overcome this, go to Tools->Internet Options->Security->Custom Level. ' + 
                     'Find the setting for "Initialize and script ActiveX controls not marked as safe" and change it to "Enable" or "Prompt"'); 
                }
            }       
        }
        else { //this is where you could fallback to Java Applet, Flash or similar
            return false;
        }       
        return true;
    }   

    /**
     * display content using a basic HTML replacement
     */
    function displayContents(txt) {
		xml = $.parseXML( txt );
		//alert(xml);
		$(xml).find('Folder').each(function()
						{
							//alert("Folder");
							$(this).find("Placemark").each(function()//pour chque element encapsul? dans des balise Placemark j'ex?cute la fnction suivante
							{
								nom = $(this).find("name").text(); // je recupere les 3 balises qui mint?r?sse et met leur contenu dans des variables
								//alert(nom);
								description = $(this).find("description").text();
								coords = $(this).find("coordinates").text();
								c = coords.split(","); // fais un tableau ou chaque case correspond ? la chaine coords s?par? par des virgules donc c[0] = x c[1] = y c[3] = 0
								tab.push( [c[0],
										c[1]]
								);
								
							});					
						});
						//alert(tab);
						var ville = document.getElementById('VilleInsert').value;
						var type = document.getElementById('LieuInsert').value;
						//alert(ville);
						//alert(type);
						donnees = '{ "ville":' + ville + ', "type":'+ type + ', "liste":'+'['+ joinArray(tab)+']' +'}';
						$.ajax(
						{ //on importe le fichier
							type: "POST",
							url: "/Servlet_AddLieux",// a cette url
							data:{ donnees: donnees},
							dataType: "json",
							success: function(data)
							{
								alert(data.status);
							}
							
						});
    }   
    function SuppressionLieu(){
            var ville = document.getElementById('VilleDel').value;
            var type = document.getElementById('LieuDel').value;
            donnees = '{ "ville":' + ville + ', "type":'+ type + ', "liste":'+'['+']' +'}';
            $.ajax(
            { //on importe le fichier
                    type: "POST",
                    url: "/Servlet_DelLieux",// a cette url
                    data:{ donnees: donnees},
                    dataType: "json",
                    success: function(data)
                    {
                    }
            })
}
	// retourne 1 si le friendmail n'est pas pr�sent dans la db, 0 si l'ajout r�ussi, 2 si un probleme dans la db est l'userID de l'ami est pr�sent 2 fois(ne dois pas arriver),3 si jamais l'ami est d�ja pr�sent dans la liste d'ami,4 si l'utilisateur s'ajoute lui m�me en ami
    function AjoutAmi(){
    	var mailAmi = document.getElementById('MailAmi').value;
		$("#popupMenu" ).popup("close");

    	
    	//alert(mailAmi);
    	$.ajax(
    			{ //on importe le fichier
    				type: "GET",
    				url: "/Servlet_AddFriend",// a cette url
    				data:{ "email": mailAmi},
    				dataType: "json",
    				success: function(data)
    				{
    					switch(data.status)
    					{
    					case 1:
    						message = "l'utilisateur n'utilise pas l'application";
    						break;
    					case 0:
    						message = "demande envoy�";
    						break;
    					case 3:
    						message = "Utilisateur d�ja pr�sent dans votre liste d'ami";
    						break;
    					case 4:
    						message = "Vous ne pouvez vous ajouter vous-m�me en ami";
    						break;
    					}
    					$("#textesuivreami3").html(message);
    					$("#popupLogin" ).popup({
    						popupafterclose: function() {
    				            setTimeout( function(){ $( '#popupsuivi3' ).popup( 'open' ) }, 200 );
    				        }});
    					
    					$("#popupLogin" ).popup("close");
						console.log(message);
    				}
    	})
    	
    }