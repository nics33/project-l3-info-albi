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
				latitudeAmi = parseFloat(json.donnees[0].lat);
				alert(json.donnees.length);
				longitudeAmi = parseFloat(json.donnees[0].lng);
				latitudeLieu = parseFloat(json.donnees[0].latlieu);
				longitudeLieu = parseFloat(json.donnees[0].lnglieu);
				//$("#listeami2").html(ListeAmi).listview('refresh');
				}
		}
	});
};