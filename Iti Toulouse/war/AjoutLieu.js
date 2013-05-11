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
								tab.push( [c[1],
										c[0]]
								);
								
							});					
						});
						alert(tab);
						var ville = document.getElementById('VilleInsert').value;
						var type = document.getElementById('LieuInsert').value;
						alert(ville);
						alert(type);
						donnees = '{ "ville":' + ville + ', "type":'+ type + ', "liste":'+'['+ joinArray(tab)+']' +'}';
						$.ajax(
						{ //on importe le fichier
							type: "POST",
							url: "http://ititoulouse.appspot.com/Servlet_AddLieux",// a cette url
							data:{ donnees: donnees},
							dataType: "json",
							success: function(data)
							{
								alert(data);
							}
							
						});
    }   
    function SuppressionLieu(){
    	var ville = document.getElementById('VilleDel').value;
		var type = document.getElementById('LieuDel').value;
		alert(ville);
		alert(type);
		donnees = '{ "ville":' + ville + ', "type":'+ type + ', "liste":'+'['+']' +'}';
		$.ajax(
		{ //on importe le fichier
			type: "POST",
			url: "http://ititoulouse.appspot.com/Servlet_DelLieux",// a cette url
			data:{ donnees: donnees},
			dataType: "json",
			success: function(data)
			{
				alert("sa marche");
			}
    }