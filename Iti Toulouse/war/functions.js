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